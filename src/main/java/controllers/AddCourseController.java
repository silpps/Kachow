package controllers;

import dao.CourseDAO;
import dao.IDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import models.Course;

import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller class for the "Add Course" view.
 * This class handles user interactions and logic for adding a new course to the timetable.
 * It validates user input, interacts with the database through DAOs, and updates the timetable view.
 */
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
    private Label courseNameLabel;
    @FXML
    private Label instructorLabel;
    @FXML
    private Label startDateLabel;
    @FXML
    private Label endDateLabel;
    @FXML
    private Label addCourseTitleLabel;
    @FXML
    private Label instructorErrorLabel;
    @FXML
    private Label startDateErrorLabel;
    @FXML
    private Label endDateErrorLabel;
    @FXML
    private Label nameErrorLabel;

    private TimetableController timetableController;

    /**
     * Handles the action when the save button is clicked.
     * Validates the input fields, checks for logical errors (e.g., start date after end date),
     * and adds the course to the database if validation passes.
     */
    @FXML
    private void addCourseSaveButtonClicked() {
        System.out.println("Course save button clicked");
        String courseName = courseNameTextField.getText();
        String instructor = instructorTextField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (courseName.isEmpty() || instructor.isEmpty() || startDate == null || endDate == null) {
            nameErrorLabel.setText(courseName.isEmpty() ? bundle.getString("courseNameErrorLabel") : "");
            instructorErrorLabel.setText(instructor.isEmpty() ? bundle.getString("instructorErrorLabel") : "");
            startDateErrorLabel.setText(startDate == null ? bundle.getString("startDateErrorLabel") : "");
            endDateErrorLabel.setText(endDate == null ? bundle.getString("endDateErrorLabel") : "");
            return;
        }

        if (startDate.isAfter(endDate)) {
            startDateErrorLabel.setText(bundle.getString("startDateAfterEndDateErrorLabel"));
            return;
        }
        Course course = new Course(courseName, instructor, startDate, endDate);
        try {
            courseDAO.add(course);

            //Update the course list in the timetable controller
            timetableController.updateCourseMap();
            // Update the UI after saving the exam
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }

                Platform.runLater(() -> {
                    System.out.println("Updating UI...");
                    timetableController.fetchAndDisplayCurrentWeeksData(bundle);
                    System.out.println("UI updated.");
                });
            }).start();
            showAlert(Alert.AlertType.INFORMATION, bundle.getString("eventSavedTitle"), bundle.getString("eventSavedMessage"));
            addCourseSaveButton.getScene().getWindow().hide();
        } catch (Exception e) {
            System.out.println("Error adding course");
            showAlert(Alert.AlertType.ERROR, bundle.getString("eventSaveErrorTitle"), bundle.getString("eventSaveErrorMessage"));
            e.printStackTrace();
        }
    }

    /**
     * Handles the action when the back button is clicked.
     * Closes the current window.
     */
    @FXML
    private void addCourseBackButtonClicked() {
        addCourseBackButton.getScene().getWindow().hide();
    }


    /**
     * Initializes the controller.
     * Sets up event handlers for the save and back buttons.
     */
     @FXML
     private void initialize() {
         addCourseSaveButton.setOnAction(event -> addCourseSaveButtonClicked());
         addCourseBackButton.setOnAction(event -> addCourseBackButtonClicked());

         courseDAO = new CourseDAO();

     }

    /**
     * Displays an alert dialog with the specified type, title, and message.
     * @param alertType the type of alert (e.g., INFORMATION, ERROR)
     * @param title the title of the alert
     * @param message the message to display in the alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sets the ResourceBundle for localization.
     * @param bundle the ResourceBundle to set for localization
     */
    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        translateUI();
    }

    /**
     * Translates the UI elements based on the provided ResourceBundle.
     * This method updates the text of various UI components to support localization.
     */
    private void translateUI() {
        if (bundle != null) {
            addCourseSaveButton.setText(bundle.getString("saveButton"));
            addCourseBackButton.setText(bundle.getString("backButton"));
            addCourseTitleLabel.setText(bundle.getString("addCourseTitleLabel"));
            courseNameLabel.setText(bundle.getString("courseNameLabel"));
            instructorLabel.setText(bundle.getString("instructorLabel"));
            startDateLabel.setText(bundle.getString("startDateLabel"));
            endDateLabel.setText(bundle.getString("endDateLabel"));

            if ("ar".equals(bundle.getLocale().getLanguage())) {
                rootPane.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
            }
        }
    }

    /**
     * Sets the timetable controller for this view.
     * @param timetableController the timetable controller to set
     */
    public void setTimetableController(TimetableController timetableController) {
        this.timetableController = timetableController;
    }
}
