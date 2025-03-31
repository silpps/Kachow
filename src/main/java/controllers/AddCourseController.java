package controllers;

import dao.CourseDAO;
import dao.IDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Course;

import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddCourseController {

    private IDAO<Course> courseDAO;

    private ResourceBundle bundle;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField courseNameTextField;

    @FXML
    private TextField instructorTextField;

    @FXML
    private Button addCourseSaveButton;

    @FXML
    private Button addCourseBackButton;

    @FXML
    private Label addCourseTitleLabel, courseNameLabel, instructorLabel, locationLabel, startDateLabel, endDateLabel;

    private TimetableController timetableController;

    @FXML
    private void addCourseSaveButtonClicked(){
        System.out.println("Course save button clicked");
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
        courseDAO.add(course);

        //Update the course list in the timetable controller
        timetableController.updateCourseMap();

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

         courseDAO = new CourseDAO();

     }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        translateUI();
    }

    private void translateUI() {
        if (bundle != null) {
            addCourseSaveButton.setText(bundle.getString("saveButton"));
            addCourseBackButton.setText(bundle.getString("backButton"));
            addCourseTitleLabel.setText(bundle.getString("titleLabel"));
            courseNameLabel.setText(bundle.getString("courseNameLabel"));
            instructorLabel.setText(bundle.getString("instructorLabel"));
            locationLabel.setText(bundle.getString("locationBoxLabel"));
            startDateLabel.setText(bundle.getString("startDateLabel"));
            endDateLabel.setText(bundle.getString("endDateLabel"));

        }
    }



    public void setTimetableController(TimetableController timetableController) {
        this.timetableController = timetableController;
    }
}
