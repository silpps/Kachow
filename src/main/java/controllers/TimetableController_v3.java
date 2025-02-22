package controllers;

import dao.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.*;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

//TODO: Järjestele ja siisti koodia
public class TimetableController_v3 implements Initializable {

    @FXML
    private VBox mondayColumn;

    @FXML
    private VBox tuesdayColumn;

    @FXML
    private VBox wednesdayColumn;

    @FXML
    private VBox thursdayColumn;

    @FXML
    private VBox fridayColumn;

    @FXML
    private VBox saturdayColumn;

    @FXML
    private VBox sundayColumn;


    @FXML
    private Label mondayDate;

    @FXML
    private Label tuesdayDate;

    @FXML
    private Label wednesdayDate;

    @FXML
    private Label thursdayDate;

    @FXML
    private Label fridayDate;

    @FXML
    private Label saturdayDate;

    @FXML
    private Label sundayDate;


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

        // Add events to the correct day's VBox
        addEventsToDay(classSchedules);
        addEventsToDay(assignments);
        addEventsToDay(studySessions);
        addEventsToDay(exams);
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

    private <T> void addEventsToDay(List<T> events) {
        events.sort(Comparator.comparing(event -> {
            if (event instanceof ClassSchedule) {
                return ((ClassSchedule) event).getStartTime();
            } else if (event instanceof StudySession) {
                return ((StudySession) event).getStartTime();
            } else if (event instanceof Exam) {
                return ((Exam) event).getExamDate();
            } else {
                return LocalDateTime.MAX;
            }
        }));

        for (T event : events) {
            VBox dayColumn = getDayColumn(getEventDayOfWeek(event));
            VBox eventBox = createEventBox(event);
            eventBox.setOnMouseClicked(e -> detailsPopup(event));
            dayColumn.getChildren().add(eventBox);
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

            HBox buttonHBox = new HBox(10, deleteButton, saveButton);
            buttonHBox.setAlignment(Pos.CENTER_RIGHT);

            saveButton.setOnAction(e -> handleSaveEvent(event, titleField, datePicker, fromTimeChoiceBox, toTimeChoiceBox, descriptionField, popupStage));
            deleteButton.setOnAction(e -> handleDeleteEvent(event, popupStage));

            popupVBox.getChildren().addAll(titleLabel, titleField, dateLabel, datePicker, fromTimeLabel, fromTimeChoiceBox, toTimeLabel, toTimeChoiceBox, descriptionLabel, descriptionField, buttonHBox);

            Scene popupScene = new Scene(popupVBox, 300, 300);
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
            studySession.setTitle(newTitle);
            studySession.setDescription(newDescription);
            studySession.setStartTime(startDateTime);
            studySession.setEndTime(endDateTime);
            studySessionDAO.update(studySession);
        } else if (event instanceof Exam exam) {
            exam.setTitle(newTitle);
            exam.setDescription(newDescription);
            exam.setExamDate(newDate.atStartOfDay());
            examDAO.update(exam);
        } else if (event instanceof Assignment assignment) {
            assignment.setTitle(newTitle);
            assignment.setDescription(newDescription);
            assignment.setDueDate(newDate.atStartOfDay());
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

    private VBox getDayColumn(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return mondayColumn;
            case 2:
                return tuesdayColumn;
            case 3:
                return wednesdayColumn;
            case 4:
                return thursdayColumn;
            case 5:
                return fridayColumn;
            case 6:
                return saturdayColumn;
            case 7:
                return sundayColumn;
            default:
                throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);
        }
    }

    private VBox createEventBox(Object event) {
        VBox eventBox = new VBox();
        eventBox.getStyleClass().add("event-box"); // Add the style class

        Label titleLabel = new Label(getEventTitle(event));
        titleLabel.setStyle("-fx-font-weight: bold;");

        Label timeLabel = new Label(getEventStartTime(event) + " - " + getEventEndTime(event));
        Label descriptionLabel = new Label(getEventDescription(event));

        eventBox.getChildren().addAll(titleLabel, timeLabel, descriptionLabel);
        setEventHeight(eventBox, event);

        return eventBox;
    }

    private String getEventTitle(Object event) {
        if (event instanceof ClassSchedule) {
            return ((ClassSchedule) event).getCourseName();
        } else if (event instanceof Assignment) {
            return "ASSIGNMENT: " + ((Assignment) event).getTitle();
        } else if (event instanceof StudySession) {
            return "STUDY SESSION: " + ((StudySession) event).getTitle();
        } else if (event instanceof Exam) {
            return "EXAM: " + ((Exam) event).getTitle();
        } else {
            return "Unknown Event";
        }
    }

    private String getEventStartTime(Object event) {
        if (event instanceof ClassSchedule) {
            ClassSchedule classSchedule = (ClassSchedule) event;
            return classSchedule.getStartTime().toLocalTime().toString();
        } else if (event instanceof StudySession) {
            StudySession studySession = (StudySession) event;
            return studySession.getStartTime().toLocalTime().toString();
        } else if (event instanceof Exam) {
            return ((Exam) event).getExamDate().toString();
        } else if (event instanceof Assignment) {
            String dueDateStr = ((Assignment) event).getDueDate().toString();
            return dueDateStr.replace("Due" , "");
        } else {
            return "";
        }
    }

    public String getEventEndTime(Object event) {
        if (event instanceof ClassSchedule) {
            ClassSchedule classSchedule = (ClassSchedule) event;
            System.out.println("End time: " + classSchedule.getEndTime().toLocalTime().toString());
            return classSchedule.getEndTime().toLocalTime().toString();
        } else if (event instanceof StudySession) {
            StudySession studySession = (StudySession) event;
            System.out.println("End time: " + studySession.getEndTime().toLocalTime().toString());
            return studySession.getEndTime().toLocalTime().toString();
        } else {
            return "";
        }
    }

    private String getEventDescription(Object event) {
        if (event instanceof ClassSchedule) {
            return ((ClassSchedule) event).getLocation();
        } else if (event instanceof Assignment) {
            return ((Assignment) event).getDescription();
        } else if (event instanceof StudySession) {
            return ((StudySession) event).getDescription();
        } else if (event instanceof Exam) {
            return ((Exam) event).getDescription();
        } else {
            return "";
        }
    }

    private LocalDateTime getEventDate(Object event) {
        if (event instanceof ClassSchedule) {
            return ((ClassSchedule) event).getStartTime();
        } else if (event instanceof StudySession) {
            return ((StudySession) event).getStartTime();
        } else if (event instanceof Exam) {
            return ((Exam) event).getExamDate();
        } else if (event instanceof Assignment) {
            return ((Assignment) event).getDueDate();
        } else {
            return LocalDateTime.now();
        }
    }

    private void setEventHeight(VBox eventBox, Object event) {
        double height;
        if (event instanceof ClassSchedule) {
            ClassSchedule classSchedule = (ClassSchedule) event;
            height = calculateHeight(classSchedule.getStartTime(), classSchedule.getEndTime());
        } else if (event instanceof StudySession) {
            StudySession studySession = (StudySession) event;
            height = calculateHeight(studySession.getStartTime(), studySession.getEndTime());
        } else {
            height = 30; // Minimum height for events without a specific time
        }
        eventBox.setPrefHeight(height);
    }

    private double calculateHeight(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        long minutes = duration.toMinutes();
        return Math.max(30, minutes); // Ensure a minimum height of 30
    }

    private int getEventDayOfWeek(Object event) {
        if (event instanceof ClassSchedule) {
            return ((ClassSchedule) event).getStartTime().getDayOfWeek().getValue();
        } else if (event instanceof Assignment) {
            return ((Assignment) event).getDueDate().getDayOfWeek().getValue();
        } else if (event instanceof StudySession) {
            return ((StudySession) event).getStartTime().getDayOfWeek().getValue();
        } else if (event instanceof Exam) {
            return ((Exam) event).getExamDate().getDayOfWeek().getValue();
        } else {
            throw new IllegalArgumentException("Unknown event type");
        }
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
}