package controllers;

import dao.CourseDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Course;
import models.CourseService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddCourseController {

    private CourseDAO courseDAO;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField courseNameTextField;

    @FXML
    private TextField instructorTextField;

    @FXML
    private ComboBox<String> startComboBox;

    @FXML
    private ComboBox<String> endComboBox;

    @FXML
    private CheckBox mondayCheckBox, tuesdayCheckBox, wednesdayCheckBox, thursdayCheckBox, fridayCheckBox;

    @FXML
    private Button addCourseSaveButton;

    @FXML
    private Button addCourseBackButton;

    private List<String> courses = new ArrayList<>();
    private TimetableController_v3 timetableController;

    @FXML
    private void addCourseSaveButtonClicked(){
        System.out.println("Course save button clicked");
        String courseName = courseNameTextField.getText();
        String instructor = instructorTextField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String startTimeString = startComboBox.getValue();
        String endTimeString = endComboBox.getValue();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // kurssissa ei aikaa, ainoastaan päivämäärät
        // LocalDateTime startTime = LocalDateTime.of(startDate, LocalTime.parse(startTimeString, formatter));
        // LocalDateTime endTime = LocalDateTime.of(endDate, LocalTime.parse(endTimeString, formatter));

        if (startDate.isAfter(endDate)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid date");
            alert.setContentText("Start date must be before end date");
            alert.showAndWait();
            return;
        }
        Course course = new Course(courseName, instructor, startDate, endDate);
        CourseService.getInstance().addCourse(courseName);
        courseDAO.add(course);

        addCourseSaveButton.getScene().getWindow().hide();
    }

    @FXML
    private void addCourseBackButtonClicked() {
        addCourseBackButton.getScene().getWindow().hide();
    }

     @FXML
     private void initialize() {
         addCourseSaveButton.setOnAction(event -> addCourseSaveButtonClicked());
         addCourseBackButton.setOnAction(event -> addCourseBackButtonClicked());
         startComboBox.getItems().addAll(
                 "6:00",
                    "7:00",
                    "8:00",
                    "9:00",
                    "10:00",
                    "11:00",
                    "12:00",
                    "13:00",
                    "14:00",
                    "15:00",
                    "16:00",
                    "17:00",
                    "18:00",
                    "19:00",
                    "20:00",
                    "21:00",
                    "22:00",
                    "23:00"
         );
            endComboBox.getItems().addAll(

                    "7:00",
                    "8:00",
                    "9:00",
                    "10:00",
                    "11:00",
                    "12:00",
                    "13:00",
                    "14:00",
                    "15:00",
                    "16:00",
                    "17:00",
                    "18:00",
                    "19:00",
                    "20:00",
                    "21:00",
                    "22:00",
                    "23:00",
                    "24:00"
            );
         courseDAO = new CourseDAO();

     }

     //toimiiko tää booleanina? ehkä tähän joku muu
    private void chosenDaysOfTheWeek(){
        if (mondayCheckBox.isSelected()){
            //maanantai
        }
        if (tuesdayCheckBox.isSelected()){
            //tiistai
        }
        if (wednesdayCheckBox.isSelected()){
            //keskiviikko
        }
        if (thursdayCheckBox.isSelected()){
            //torstai
        }
        if (fridayCheckBox.isSelected()){
            //perjantai
        }
    }

    public void setTimetableController(TimetableController_v3 timetableController) {
        this.timetableController = timetableController;
    }
}
