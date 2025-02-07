package controllers;

import dao.CourseDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.Course;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddCourseController {

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
        Course course = new Course(courseName, instructor, startDate, endDate);
        courses.add(courseName);
        CourseDAO courseDAO = new CourseDAO();
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
     }

     //toimiiko tää booleanina?
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
