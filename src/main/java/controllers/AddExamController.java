package controllers;

import dao.ExamDAO;
import dao.IDAO;
import dao.TimeTableDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import models.Exam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller class for the "Add Exam" view.
 * This class handles the user interactions and logic for adding a new exam to the timetable.
 * It validates user input, interacts with the database through DAOs, and updates the timetable view.
 */
public class AddExamController {

    private IDAO<Exam> examDAO;
    private ResourceBundle bundle;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ChoiceBox<String> courseNameChoiceBox;
    @FXML
    private TextField locationTextField;
    @FXML
    private TextField examTitleTextField;
    @FXML
    private DatePicker examDatePicker;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Button backButton;
    @FXML
    private Button examSaveButton;
    @FXML
    private Label courseNameLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label examDateLabel;
    @FXML
    private Label fromLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label addExamTitleLabel;
    @FXML
    private Label titleErrorLabel;
    @FXML
    private Label dateErrorLabel;
    @FXML
    private Label timeErrorLabel;
    @FXML
    private Label courseErrorLabel;
    @FXML
    private ChoiceBox<String> fromChoiceBox;

    private final String[] startTimes = {"6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};

    private TimetableController timetableController;


    /**
     * Initializes the controller.
     * Populates the course choice box with available courses and the time choice box with predefined start times.
     * Sets up event handlers for the save and back buttons.
     * Retrieves course data from the database using {@link TimeTableDAO}.
     */
    @FXML
    public void initialize(){
        TimeTableDAO timeTableDAO = new TimeTableDAO();
        Map<Integer, String> courses = timeTableDAO.getCourses();
        for (Map.Entry<Integer, String> entry : courses.entrySet()) {
            courseNameChoiceBox.getItems().add(entry.getValue() + " (ID: " + entry.getKey() + ")");
        }

        examDAO = new ExamDAO();

        fromChoiceBox.getItems().addAll(startTimes);
        backButton.setOnAction(e -> backButtonClicked());
        examSaveButton.setOnAction(e -> examSaveButtonClicked());
    }

    /**
     * Handles the action when the back button is clicked.
     * Closes the current window.
     */
    @FXML
    private void backButtonClicked() {
        // Close the current window
        backButton.getScene().getWindow().hide();
    }

    /**
     * Handles the action when the save button is clicked.
     * Validates the input fields, checks for logical errors (e.g., missing fields),
     * and adds the exam to the database if validation passes.
     * Updates the timetable view after successfully adding the exam.
     */
    @FXML
    private void examSaveButtonClicked() {
        // Save the exam details
        String selectedItem = courseNameChoiceBox.getValue();
        String examTitle = examTitleTextField.getText();
        String description = descriptionTextArea.getText();
        String location = locationTextField.getText();
        LocalDate examDate = examDatePicker.getValue();
        String fromTimeString = fromChoiceBox.getValue();

        if (selectedItem == null || examTitle.isEmpty() || description.isEmpty() || location.isEmpty() || examDate == null || fromTimeString == null) {
            courseErrorLabel.setText(selectedItem == null ? bundle.getString("courseErrorLabel") : "");
            titleErrorLabel.setText(examTitle.isEmpty() ? bundle.getString("titleErrorLabel") : "");
            dateErrorLabel.setText(examDate == null ? bundle.getString("dateErrorLabel") : "");
            timeErrorLabel.setText(fromTimeString == null ? bundle.getString("timeErrorLabel") : "");
            return;
        }

        int courseId = Integer.parseInt(selectedItem.replaceFirst(".*\\(ID: (\\d+)\\)$", "$1"));


        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        LocalDateTime examDateTime = LocalDateTime.of(examDate, LocalTime.parse(fromTimeString, timeFormatter));
        System.out.println(examDateTime);
        System.out.println(examDate);


        Exam exam = new Exam(courseId, examDateTime, examTitle, description, location);
        try {
            examDAO.add(exam);
            showAlert(Alert.AlertType.INFORMATION, bundle.getString("eventSavedTitle"), bundle.getString("eventSavedMessage"));
            bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
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

            backButtonClicked();
        } catch (Exception e) {
            System.out.println("Error adding exam");
            showAlert(Alert.AlertType.ERROR, bundle.getString("eventSaveErrorTitle"), bundle.getString("assignmentSaveErrorMessage"));
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sets the ResourceBundle for localization.
     *
     * @param bundle the ResourceBundle to set for localization.
     */
    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        translateUI();
    }

    /**
     * Translates the UI elements based on the provided ResourceBundle.
     */
    private void translateUI() {
        if (bundle != null) {
            examSaveButton.setText(bundle.getString("saveButton"));
            backButton.setText(bundle.getString("backButton"));
            addExamTitleLabel.setText(bundle.getString("addExamTitleLabel"));
            titleLabel.setText(bundle.getString("titleLabel"));
            courseNameLabel.setText(bundle.getString("courseNameLabel"));
            examDateLabel.setText(bundle.getString("dateLabel"));
            fromLabel.setText(bundle.getString("fromTimeLabel"));
            descriptionLabel.setText(bundle.getString("descriptionLabel"));
            locationLabel.setText(bundle.getString("locationBoxLabel"));

            if ("ar".equals(bundle.getLocale().getLanguage())) {
                rootPane.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
            }
        }
    }

    /**
     * Sets the timetable controller for this view.
     *
     * @param timetableController the timetable controller
     */
    public void setTimetableController(TimetableController timetableController) {
        this.timetableController = timetableController;
    }
}