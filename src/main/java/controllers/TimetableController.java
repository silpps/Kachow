package controllers;

import dao.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.*;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

//TODO: Järjestele ja siisti koodia
public class TimetableController implements Initializable {

    @FXML
    private VBox mondayColumn, tuesdayColumn, wednesdayColumn, thursdayColumn, fridayColumn, saturdayColumn, sundayColumn;


    @FXML
    private Label mondayDate, tuesdayDate, wednesdayDate, thursdayDate, fridayDate, saturdayDate, sundayDate;

    private TimeTableDAO timeTableDAO;
    private StudySessionDAO studySessionDAO;
    private ExamDAO examDAO;
    private AssignmentDAO assignmentDAO;
    private ClassScheduleDAO classScheduleDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeTableDAO = new TimeTableDAO();
        studySessionDAO = new StudySessionDAO();
        examDAO = new ExamDAO();
        assignmentDAO = new AssignmentDAO();
        classScheduleDAO = new ClassScheduleDAO();

        fetchAndDisplayCurrentWeeksData();
    }

    public void fetchAndDisplayCurrentWeeksData() {
        clearTimetable();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);

        // Format to dd/MM
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        // Set the dates to the labels
        mondayDate.setText(startOfWeek.format(formatter));
        tuesdayDate.setText(startOfWeek.plusDays(1).format(formatter));
        wednesdayDate.setText(startOfWeek.plusDays(2).format(formatter));
        thursdayDate.setText(startOfWeek.plusDays(3).format(formatter));
        fridayDate.setText(startOfWeek.plusDays(4).format(formatter));
        saturdayDate.setText(startOfWeek.plusDays(5).format(formatter));
        sundayDate.setText(startOfWeek.plusDays(6).format(formatter));

        // Fetch current week's data from database
        List<ClassSchedule> classSchedules = timeTableDAO.getClassSchedule(startOfWeek, endOfWeek);
        List<Assignment> assignments = timeTableDAO.getAssignmentSchedule(startOfWeek, endOfWeek);
        List<StudySession> studySessions = timeTableDAO.getStudySessionSchedule(startOfWeek, endOfWeek);
        List<Exam> exams = timeTableDAO.getExamSchedule(startOfWeek, endOfWeek);

        // Combine all tasks into a single list
        List<Object> allTasks = new ArrayList<>();
        allTasks.addAll(classSchedules);
        allTasks.addAll(assignments);
        allTasks.addAll(studySessions);
        allTasks.addAll(exams);

        // Add tasks to the correct day's VBox
        addTasksToDay(allTasks);
    }

    private void clearTimetable() {
        mondayColumn.getChildren().clear();
        tuesdayColumn.getChildren().clear();
        wednesdayColumn.getChildren().clear();
        thursdayColumn.getChildren().clear();
        fridayColumn.getChildren().clear();
        saturdayColumn.getChildren().clear();
        sundayColumn.getChildren().clear();
    }

    private <T> void addTasksToDay(List<T> tasks) {
        tasks.sort(Comparator.comparing(task -> {
            return switch (task) {
                case ClassSchedule classSchedule -> classSchedule.getStartTime();
                case StudySession studySession -> studySession.getStartTime();
                case Exam exam -> exam.getExamDate();
                case Assignment assignment -> assignment.getDeadline();
                case null, default -> LocalDateTime.MAX;
            };
        }));

        for (T task : tasks) {
            VBox dayColumn = getDayColumn(getTaskDayOfWeek(task));
            VBox taskBox = createTaskBox(task);
            taskBox.setOnMouseClicked(e -> detailsPopup(task));
            dayColumn.getChildren().add(taskBox);
        }
    }

    private VBox getDayColumn(int dayOfWeek) {
        return switch (dayOfWeek) {
            case 1 -> mondayColumn;
            case 2 -> tuesdayColumn;
            case 3 -> wednesdayColumn;
            case 4 -> thursdayColumn;
            case 5 -> fridayColumn;
            case 6 -> saturdayColumn;
            case 7 -> sundayColumn;
            default -> throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);
        };
    }


    private VBox createTaskBox(Object task) {
        return switch (task) {
            case ClassSchedule classSchedule -> createClassScheduleBox(classSchedule);
            case Assignment assignment -> createAssignmentBox(assignment);
            case StudySession studySession -> createStudySessionBox(studySession);
            case Exam exam -> createExamBox(exam);
            case null, default -> throw new IllegalArgumentException("Unknown task type");
        };
    }

    private VBox createClassScheduleBox(ClassSchedule classSchedule) {
        VBox taskBox = new VBox();
        taskBox.getStyleClass().add("class-schedule-box");

        Label courseLabel = new Label(classSchedule.getCourseName());
        Label taskLabel = new Label("CLASS: ");
        taskLabel.setStyle("-fx-font-weight: bold;");
        Label timeLabel = new Label(classSchedule.getStartTime().toLocalTime() + " - " + classSchedule.getEndTime().toLocalTime());
        Label locationLabel = new Label("Location: " + classSchedule.getLocation());

        taskBox.getChildren().addAll(courseLabel, taskLabel, timeLabel, locationLabel);
        return taskBox;
    }

    private VBox createAssignmentBox(Assignment assignment) {
        VBox taskBox = new VBox();
        taskBox.getStyleClass().add("assignment-box");

        Label courseLabel = new Label(assignment.getCourseName());
        Label taskLabel = new Label("ASSIGNMENT: ");
        taskLabel.setStyle("-fx-font-weight: bold;");
        Label timeLabel = new Label("Deadline: " + assignment.getDeadline().toLocalTime().toString());
        Label statusLabel = new Label("Status: " + (assignment.getStatus()));

        taskBox.getChildren().addAll(courseLabel, taskLabel, timeLabel, statusLabel);
        return taskBox;
    }

    private VBox createStudySessionBox(StudySession studySession) {
        VBox taskBox = new VBox();
        taskBox.getStyleClass().add("study-session-box");

        Label courseLabel = new Label(studySession.getCourseName());
        Label taskLabel = new Label("STUDY SESSION: ");
        taskLabel.setStyle("-fx-font-weight: bold;");
        Label timeLabel = new Label(studySession.getStartTime().toLocalTime() + " - " + studySession.getEndTime().toLocalTime());

        taskBox.getChildren().addAll(courseLabel, taskLabel, timeLabel);
        return taskBox;
    }

    private VBox createExamBox(Exam exam) {
        VBox taskBox = new VBox();
        taskBox.getStyleClass().add("exam-box");

        Label courseLabel = new Label(exam.getCourseName());
        Label taskLabel = new Label("EXAM: ");
        taskLabel.setStyle("-fx-font-weight: bold;");
        Label timeLabel = new Label(exam.getExamDate().toLocalTime().toString());
        Label locationLabel = new Label("Location: " + exam.getLocation());

        taskBox.getChildren().addAll(courseLabel, taskLabel, timeLabel, locationLabel);
        return taskBox;
    }


    //ei tällä hetkellä käytössä
    private double calculateHeight(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        long minutes = duration.toMinutes();
        return Math.max(30, minutes); // Ensure a minimum height of 30
    }

    private int getTaskDayOfWeek(Object task) {
        return switch (task) {
            case ClassSchedule classSchedule -> classSchedule.getStartTime().getDayOfWeek().getValue();
            case Assignment assignment -> assignment.getDeadline().getDayOfWeek().getValue();
            case StudySession studySession -> studySession.getStartTime().getDayOfWeek().getValue();
            case Exam exam -> exam.getExamDate().getDayOfWeek().getValue();
            case null, default -> throw new IllegalArgumentException("Unknown task type");
        };
    }

    @FXML
    private void addButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addTask.fxml"));
            Scene scene = new Scene(loader.load());
            AddTaskController controller = loader.getController();
            controller.setTimetableController(this);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <T> void detailsPopup(T event) {
        try {
            Stage popupStage = new Stage();
            popupStage.setTitle("Event Details");

            VBox popupVBox = new VBox(10);
            popupVBox.setStyle("-fx-padding: 20;");

            Label titleLabel = new Label("Title:");
            TextField titleField = new TextField(getEventTitle(event));

            Label dateLabel = new Label("Date:");
            DatePicker datePicker = new DatePicker(getEventDate(event).toLocalDate());

            Label fromTimeLabel = new Label("From:");
            ChoiceBox<String> fromTimeChoiceBox = new ChoiceBox<>();
            fromTimeChoiceBox.getItems().addAll("06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");
            fromTimeChoiceBox.setValue(getEventStartTime(event));

            Label toTimeLabel = new Label("To:");
            ChoiceBox<String> toTimeChoiceBox = new ChoiceBox<>();
            toTimeChoiceBox.getItems().addAll("07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00");
            toTimeChoiceBox.setValue(getEventEndTime(event));

            Label descriptionLabel = new Label("Description:");
            TextArea descriptionField = new TextArea(getEventDescription(event));

            Button saveButton = new Button("Save");
            Button deleteButton = new Button("Delete");

            HBox buttonHBox = new HBox(30, deleteButton, saveButton);
            buttonHBox.setAlignment(Pos.CENTER);

            saveButton.setOnAction(e -> handleSaveEvent(event, titleField, datePicker, fromTimeChoiceBox, toTimeChoiceBox, descriptionField, popupStage));
            deleteButton.setOnAction(e -> handleDeleteEvent(event, popupStage));

            popupVBox.getChildren().addAll(titleLabel, titleField, dateLabel, datePicker, fromTimeLabel, fromTimeChoiceBox, toTimeLabel, toTimeChoiceBox, descriptionLabel, descriptionField, buttonHBox);

            Scene popupScene = new Scene(popupVBox, 300, 600);
            popupStage.setScene(popupScene);
            popupStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private <T> void handleSaveEvent(T event, TextField titleField, DatePicker datePicker, ChoiceBox<String> fromTimeChoiceBox, ChoiceBox<String> toTimeChoiceBox, TextArea descriptionField, Stage popupStage) {
        String newTitle = titleField.getText();
        LocalDate newDate = datePicker.getValue();
        String newFromTime = fromTimeChoiceBox.getValue();
        String newToTime = toTimeChoiceBox.getValue();
        String newDescription = descriptionField.getText();

        LocalTime startTime = LocalTime.parse(newFromTime, DateTimeFormatter.ofPattern("H:mm"));
        LocalTime endTime = LocalTime.parse(newToTime, DateTimeFormatter.ofPattern("H:mm"));

        LocalDateTime startDateTime = LocalDateTime.of(newDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(newDate, endTime);

        if (event instanceof StudySession studySession) {
            studySession.setDescription(newDescription);
            studySession.setStartTime(startDateTime);
            studySession.setEndTime(endDateTime);
            studySessionDAO.update(studySession);
        } else if (event instanceof Exam exam) {
            exam.setDescription(newDescription);
            LocalDateTime updatedExamDate = LocalDateTime.of(newDate, startTime);
            exam.setExamDate(updatedExamDate);
            examDAO.update(exam);
        } else if (event instanceof Assignment assignment) {
            assignment.setDescription(newDescription);
            assignment.setDeadline(startDateTime);
            assignmentDAO.update(assignment);
        } else if (event instanceof ClassSchedule classSchedule) {
            classSchedule.setCourseName(newTitle);
            classSchedule.setLocation(newDescription);
            classSchedule.setStartTime(startDateTime);
            classSchedule.setEndTime(endDateTime);
            classScheduleDAO.update(classSchedule);
        }

        popupStage.close();
        fetchAndDisplayCurrentWeeksData();
    }



    private void handleDeleteEvent(Object event, Stage popupStage) {
        System.out.println("Delete event: " + getEventTitle(event));

        deleteEvent(event);
        popupStage.close();
    }

    private void deleteEvent(Object event) {
        if (event instanceof ClassSchedule) {
            timeTableDAO.deleteClassSchedule((ClassSchedule) event);
        } else if (event instanceof StudySession) {
            timeTableDAO.deleteStudySession((StudySession) event);
        } else if (event instanceof Exam) {
            timeTableDAO.deleteExam((Exam) event);
        } else if (event instanceof Assignment) {
            timeTableDAO.deleteAssignment((Assignment) event);
        }

        fetchAndDisplayCurrentWeeksData();
    }

    private <T> String getEventTitle(T event) {
        if (event instanceof ClassSchedule classSchedule) {
            return classSchedule.getCourseName();
        } else if (event instanceof Assignment assignment) {
            return assignment.getCourseName();
        } else if (event instanceof StudySession studySession) {
            return studySession.getCourseName();
        } else if (event instanceof Exam exam) {
            return exam.getCourseName();
        }
        return "Unknown Event";
    }

    private <T> LocalDateTime getEventDate(T event) {
        if (event instanceof ClassSchedule classSchedule) {
            return classSchedule.getStartTime();
        } else if (event instanceof Assignment assignment) {
            return assignment.getDeadline();
        } else if (event instanceof StudySession studySession) {
            return studySession.getStartTime();
        } else if (event instanceof Exam exam) {
            return exam.getExamDate();
        }
        return LocalDateTime.now(); // Default to now if unknown event
    }

    private <T> String getEventStartTime(T event) {
        if (event instanceof ClassSchedule classSchedule) {
            return classSchedule.getStartTime().toLocalTime().toString();
        } else if (event instanceof StudySession studySession) {
            return studySession.getStartTime().toLocalTime().toString();
        } else if (event instanceof Exam exam) {
            return exam.getExamDate().toLocalTime().toString();
        } else if (event instanceof Assignment assignment) {
            return assignment.getDeadline().toLocalTime().toString();
        }
        return "00:00"; // Default
    }

    private <T> String getEventEndTime(T event) {
        if (event instanceof ClassSchedule classSchedule) {
            return classSchedule.getEndTime().toLocalTime().toString();
        } else if (event instanceof StudySession studySession) {
            return studySession.getEndTime().toLocalTime().toString();
        } else if (event instanceof Exam exam) {
            return exam.getExamDate().toLocalTime().toString();
        } else if (event instanceof Assignment assignment) {
            return assignment.getDeadline().toLocalTime().toString();
        }
        return "01:00"; // Default
    }

    private <T> String getEventDescription(T event) {
        if (event instanceof ClassSchedule classSchedule) {
            return "Location: " + classSchedule.getLocation();
        } else if (event instanceof Assignment assignment) {
            return "Status: " + assignment.getStatus();
        } else if (event instanceof StudySession studySession) {
            return "Study session for " + studySession.getCourseName();
        } else if (event instanceof Exam exam) {
            return "Location: " + exam.getLocation();
        }
        return "";
    }

}