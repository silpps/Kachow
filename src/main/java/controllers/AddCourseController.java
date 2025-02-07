package controllers;

import dao.CourseDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Course;
import models.CourseService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddCourseController {

    private CourseDAO courseDAO;

    @FXML
    private TextField courseNameTextField;

    @FXML
    private TextField instructorTextField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private CheckBox mondayCheckBox, tuesdayCheckBox, wednesdayCheckBox, thursdayCheckBox, fridayCheckBox;

    @FXML
    private Button addCourseSaveButton;

    @FXML
    private Button addCourseBackButton;

    private List<String> courses = new ArrayList<>();

    @FXML
    private void addCourseSaveButtonClicked(){
        String courseName = courseNameTextField.getText();
        String instructor = instructorTextField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
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
    }

    @FXML
    private void addCourseBackButtonClicked() {
        addCourseBackButton.getScene().getWindow().hide();
    }

     @FXML
     private void initialize() {
         addCourseSaveButton.setOnAction(event -> addCourseSaveButtonClicked());
         addCourseBackButton.setOnAction(event -> addCourseBackButtonClicked());
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

}
