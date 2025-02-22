package controllers;

import dao.ClassScheduleDAO;
import dao.IDAO;
import dao.TimeTableDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.ClassSchedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddClassScheduleController {
    private IDAO<ClassSchedule> classScheduleDAO;
    private TimeTableDAO timeTableDAO;

    private TimetableController timetableController;

    @FXML
    private DatePicker sessionDatePicker;

    @FXML
    private ChoiceBox<String> courseNameChoiceBox;

    @FXML
    private TextField locationTextField;

    @FXML
    private ChoiceBox<String> fromChoiceBox;

    @FXML
    private ChoiceBox<String> toChoiceBox;

    @FXML
    private ChoiceBox<String> dayOfWeekChoiceBox;

    @FXML
    private Button sessionBackButton;

    @FXML
    private Button sessionSaveButton;

    private String[] startTimes = {"6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};

    private String[] endTimes = {"7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"};


    @FXML
    private void initialize() {
        classScheduleDAO = new ClassScheduleDAO();
        timeTableDAO = new TimeTableDAO();
        List<String> courseNames = timeTableDAO.getCourseNames();
        courseNameChoiceBox.getItems().addAll(courseNames);

        dayOfWeekChoiceBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");


        fromChoiceBox.getItems().addAll(startTimes);
        toChoiceBox.getItems().addAll(endTimes);
        sessionSaveButton.setOnAction(e -> sessionSaveButtonClicked());
        sessionBackButton.setOnAction(e -> sessionBackButtonClicked());
    }

    @FXML
    private void sessionBackButtonClicked() {
        // Close the window
        sessionBackButton.getScene().getWindow().hide();
    }

    @FXML
    private void sessionSaveButtonClicked() {
        // Get the selected course name
        String courseName = courseNameChoiceBox.getValue();

        // Get location
        String location = locationTextField.getText();
        LocalDate date = sessionDatePicker.getValue();
        String fromTimeString = fromChoiceBox.getValue();
        String toTimeString = toChoiceBox.getValue();

        // Get the selected day of the week
        //TODO: Tarkista onko tarpeellinen
        String dayOfWeek = dayOfWeekChoiceBox.getValue();
        System.out.println("Days of the week: " + dayOfWeek);

        // Parse the start and end times
        if (date != null && fromTimeString != null && toTimeString != null) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime fromTime = LocalDateTime.of(date, LocalTime.parse(fromTimeString, timeFormatter));
            LocalDateTime toTime = LocalDateTime.of(date, LocalTime.parse(toTimeString, timeFormatter));
            System.out.println(fromTime);
            System.out.println(toTime);
            System.out.println(date);
            if (fromTime.isAfter(toTime)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid time");
                alert.setContentText("From time must be before to time");
                alert.showAndWait();
                return;
            }
            ClassSchedule classSchedule = new ClassSchedule(courseName, dayOfWeek, location, fromTime, toTime);
            classScheduleDAO.add(classSchedule);
            System.out.println("Class Schedule added" + classSchedule.getCourseName() + " " + classSchedule.getLocation());


        }

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                System.out.println("Updating UI...");
                timetableController.fetchAndDisplayCurrentWeeksData();
                System.out.println("UI updated.");
            });
        }).start();

        // Close the window
        sessionBackButtonClicked();
    }

    public void setTimetableController(TimetableController timetableController) {
        this.timetableController = timetableController;
    }
}