package controllers;

import dao.CourseDAO;
import dao.IDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import models.Course;

import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddCourseController {

    private IDAO<Course> courseDAO;

    private ResourceBundle bundle;

    @FXML
    private AnchorPane rootPane;

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
    private Label addCourseTitleLabel, courseNameLabel, instructorLabel, startDateLabel, endDateLabel;

    @FXML
    private Label nameErrorLabel, instructorErrorLabel, startDateErrorLabel, endDateErrorLabel;

    private TimetableController timetableController;

    @FXML
    private void addCourseSaveButtonClicked(){
        System.out.println("Course save button clicked");
        String courseName = courseNameTextField.getText();
        String instructor = instructorTextField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (courseName.isEmpty() || instructor.isEmpty() || startDate == null || endDate == null) {
            nameErrorLabel.setText(courseName.isEmpty() ? bundle.getString("courseNameErrorLabel") : "");
            instructorErrorLabel.setText(instructor.isEmpty() ? bundle.getString("instructionErrorLabel") : "");
            startDateErrorLabel.setText(startDate == null ? bundle.getString("startDateErrorLabel") : "");
            endDateErrorLabel.setText(endDate == null ? bundle.getString("endDateErrorLabel") : "");
            return;
        }

        if (startDate.isAfter(endDate)) {
            startDateErrorLabel.setText(bundle.getString("startDateAfterEndDateErrorLabel"));
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
            startDateLabel.setText(bundle.getString("startDateLabel"));
            endDateLabel.setText(bundle.getString("endDateLabel"));

            if (bundle.getLocale().getLanguage().equals("ar")) {
                rootPane.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
            }
        }
    }



    public void setTimetableController(TimetableController timetableController) {
        this.timetableController = timetableController;
    }
}
