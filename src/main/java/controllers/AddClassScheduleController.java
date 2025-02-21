package controllers;

import dao.ClassScheduleDAO;
import dao.IDAO;
import dao.TimeTableDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.ClassSchedule;
import models.CourseService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddClassScheduleController {
    private IDAO<ClassSchedule> classScheduleDAO;
    private TimeTableDAO timeTableDAO;

    private DatePicker datePicker;
    @FXML
    private ChoiceBox<String> courseNameChoiceBox;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextField locationTextField;

    @FXML
    private TextField startTimeTextField;

    @FXML
    private TextField endTimeTextField;

    @FXML
    private ChoiceBox<String> dayOfWeekChoiceBox;

    @FXML
    private Button backButton;

    @FXML
    private Button saveButton;

    @FXML
    private void initialize() {
        classScheduleDAO = new ClassScheduleDAO();
        timeTableDAO = new TimeTableDAO();
        List<String> courseNames = timeTableDAO.getCourseNames();
        courseNameChoiceBox.getItems().addAll(courseNames);

        dayOfWeekChoiceBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");


        // Set up button actions
        backButton.setOnAction(e -> backButtonClicked());
        saveButton.setOnAction(e -> saveButtonClicked());
    }

    @FXML
    private void backButtonClicked() {
        // Close the window
        backButton.getScene().getWindow().hide();
    }

    @FXML
    private void saveButtonClicked() {
        // Get the selected course name
        String courseName = courseNameChoiceBox.getValue();

        // Get the title and location
        String location = locationTextField.getText();

        LocalDate date = datePicker.getValue();

        // Parse the start and end times
        LocalTime startTime = LocalTime.parse(startTimeTextField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(endTimeTextField.getText(), DateTimeFormatter.ofPattern("HH:mm"));

        // Combine the date with the start and end times to create LocalDateTime objects
        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

        // Get the selected day of the week
        String dayOfWeek = dayOfWeekChoiceBox.getValue();


        System.out.println("Days of the week: " + dayOfWeek);
        // Create a new ClassSchedule object
        ClassSchedule classSchedule = new ClassSchedule(courseName, dayOfWeek, location, startDateTime, endDateTime);

        // Save the class schedule to the database
        classScheduleDAO.add(classSchedule);

        // Close the window
        backButtonClicked();
    }
}