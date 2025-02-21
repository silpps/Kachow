package controllers;

import dao.TimeTableDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private Button addButton;

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
            dayColumn.getChildren().add(eventBox);
        }
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

        Label timeLabel = new Label(getEventTime(event));
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

    private String getEventTime(Object event) {
        if (event instanceof ClassSchedule) {
            ClassSchedule classSchedule = (ClassSchedule) event;
            return classSchedule.getStartTime().toLocalTime() + " - " + classSchedule.getEndTime().toLocalTime();
        } else if (event instanceof StudySession) {
            StudySession studySession = (StudySession) event;
            return studySession.getStartTime().toLocalTime() + " - " + studySession.getEndTime().toLocalTime();
        } else if (event instanceof Exam) {
            return ((Exam) event).getExamDate().toString();
        } else if (event instanceof Assignment) {
            return "Due: " + ((Assignment) event).getDueDate().toString();
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