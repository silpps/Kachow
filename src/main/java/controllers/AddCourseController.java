package controllers;

import dao.CourseDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Course;
import models.CourseService;

import java.time.LocalDate;
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
    private Button addCourseSaveButton;

    @FXML
    private Button addCourseBackButton;

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

         courseDAO = new CourseDAO();

     }


    public void setTimetableController(TimetableController timetableController) {
        this.timetableController = timetableController;
    }
}
