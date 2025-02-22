package controllers;

import dao.TimeTableDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.*;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeTableDAO = new TimeTableDAO();
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
            return switch (event) {
                case ClassSchedule classSchedule -> classSchedule.getStartTime();
                case StudySession studySession -> studySession.getStartTime();
                case Exam exam -> exam.getExamDate();
                case null, default -> LocalDateTime.MAX;
            };
        }));

        for (T event : events) {
            VBox dayColumn = getDayColumn(getTaskDayOfWeek(event));
            VBox eventBox = createEventBox(event);
            dayColumn.getChildren().add(eventBox);
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

    private VBox createEventBox(Object task) {
        VBox taskBox = new VBox();
        taskBox.getStyleClass().add("task-box"); // Add the style class

        Label courseLabel = new Label(getCourseName(task));

        Label taskLabel = new Label(getTaskLabel(task));
        taskLabel.setStyle("-fx-font-weight: bold;");

        Label timeLabel = new Label(getTaskTime(task));
       // Label descriptionLabel = new Label(getEventDescription(task));

        taskBox.getChildren().addAll(courseLabel, taskLabel, timeLabel);
        //setTaskHeight(taskBox, task);

        return taskBox;
    }

    private String getTaskLabel(Object event) {
        return switch (event) {
            case ClassSchedule classSchedule -> "CLASS: ";
            case Assignment assignment -> "ASSIGNMENT: ";
            case StudySession studySession -> "STUDY SESSION: ";
            case Exam exam -> "EXAM: ";
            case null, default -> "Unknown Task";
        };
    }

    private String getCourseName(Object event) {
        return switch (event) {
            case ClassSchedule classSchedule -> classSchedule.getCourseName();
            case Assignment assignment -> assignment.getCourseName();
            case StudySession studySession -> studySession.getCourseName();
            case Exam exam -> exam.getCourseName();
            case null, default -> "Unknown Course";
        };
    }

    private String getTaskTime(Object task) {
        return switch (task) {
            case ClassSchedule classSchedule ->
                    classSchedule.getStartTime().toLocalTime() + " - " + classSchedule.getEndTime().toLocalTime();
            case StudySession studySession ->
                    studySession.getStartTime().toLocalTime() + " - " + studySession.getEndTime().toLocalTime();
            case Exam exam -> exam.getExamDate().toLocalTime().toString();
            case Assignment assignment -> "Deadline: " + assignment.getDeadline().toLocalTime().toString();
            case null, default -> "";
        };
    }

    /*
    private String getEventDescription(Object task) {
        if (task instanceof ClassSchedule) {
            return ((ClassSchedule) task).getLocation();
        } else if (task instanceof Assignment) {
            return ((Assignment) task).getDescription();
        } else if (task instanceof StudySession) {
            return ((StudySession) task).getDescription();
        } else if (task instanceof Exam) {
            return ((Exam) task).getDescription();
        } else {
            return "";
        }
    }*/
    //TODO: Mieti onko tarpeellinen
    /*private void setTaskHeight(VBox eventBox, Object task) {
        double height;
        if (task instanceof ClassSchedule classSchedule) {
            height = calculateHeight(classSchedule.getStartTime(), classSchedule.getEndTime());
        } else if (task instanceof StudySession studySession) {
            height = calculateHeight(studySession.getStartTime(), studySession.getEndTime());
        } else {
            height = 30; // Minimum height for events without a specific time
        }
        eventBox.setPrefHeight(height);
    }*/

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
}