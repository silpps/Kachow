package controllers;

import dao.AssignmentDAO;
import dao.IDAO;
import dao.TimeTableDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import models.Assignment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controller class for the "Add Assignment" view.
 * This class handles user interactions and logic for adding a new assignment to the timetable.
 * It validates user input, interacts with the database through DAOs, and updates the timetable view.
 */
public class AddAssignmentController {
    private final String[] deadlineTimes = {"6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
    private TimetableController timetableController;
    private ResourceBundle bundle;
    private IDAO<Assignment> assignmentDAO;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ChoiceBox<String> courseNameChoiceBox;
    @FXML
    private TextField assignmentTitleTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private DatePicker assignmentDatePicker;
    @FXML
    private CheckBox notStartedCheckBox;
    @FXML
    private CheckBox ongoingCheckBox;
    @FXML
    private Button backButton;
    @FXML
    private Button assignmentSaveButton;
    @FXML
    private Label addAssignmentTitleLabel;
    @FXML
    private Label assignmentCourseNameLabel;
    @FXML
    private Label assignmentProgressLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label deadlineLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label assignmentTitleLabel;
    @FXML
    private Label assignmentDescriptionLabel;
    @FXML
    private Label titleErrorLabel;
    @FXML
    private Label dateErrorLabel;
    @FXML
    private Label timeErrorLabel;
    @FXML
    private Label progressErrorLabel;
    @FXML
    private Label courseErrorLabel;
    @FXML
    private ChoiceBox<String> deadlineChoiceBox;

    /**
     * Handles the action when the back button is clicked.
     * Closes the current window.
     */
    @FXML
    private void backButtonClicked() {
        backButton.getScene().getWindow().hide();
    }

    /**
     * Initializes the controller.
     * Populates the course choice box with available courses and the deadline choice box with predefined times.
     * Sets up event handlers for the save and back buttons.
     * Retrieves course data from the database using {@link TimeTableDAO}.
     */
    @FXML
    public void initialize() {
        TimeTableDAO timeTableDAO = new TimeTableDAO();
        Map<Integer, String> courses = timeTableDAO.getCourses();
        for (Map.Entry<Integer, String> entry : courses.entrySet()) {
            courseNameChoiceBox.getItems().add(entry.getValue() + " (ID: " + entry.getKey() + ")");
        }

        assignmentDAO = new AssignmentDAO();

        deadlineChoiceBox.getItems().addAll(deadlineTimes);
        backButton.setOnAction(e -> backButtonClicked());
        assignmentSaveButton.setOnAction(e -> assignmentSaveButtonClicked());
    }

    /**
     * Handles the action when the save button is clicked.
     * Validates the input fields, checks for logical errors (e.g., missing or invalid data),
     * and saves the assignment to the database if all validations pass.
     * Updates the timetable view after successfully saving the assignment.
     */
    @FXML
    private void assignmentSaveButtonClicked() {
        // Save the assignment details
        String selectedItem = courseNameChoiceBox.getValue();
        String assignmentTitle = assignmentTitleTextField.getText();
        String description = descriptionTextArea.getText();
        LocalDate date = assignmentDatePicker.getValue();
        String deadlineTimeString = deadlineChoiceBox.getValue();
        if (selectedItem == null || assignmentTitle.isEmpty() || date == null || deadlineTimeString == null || (!notStartedCheckBox.isSelected() && !ongoingCheckBox.isSelected())) {
            courseErrorLabel.setText(selectedItem == null ? bundle.getString("courseErrorLabel") : "");
            titleErrorLabel.setText(assignmentTitle.isEmpty() ? bundle.getString("titleErrorLabel") : "");
            dateErrorLabel.setText(date == null ? bundle.getString("dateErrorLabel") : "");
            timeErrorLabel.setText(deadlineTimeString == null ? bundle.getString("timeErrorLabel") : "");
            progressErrorLabel.setText((!notStartedCheckBox.isSelected() && !ongoingCheckBox.isSelected()) ? bundle.getString("progressErrorLabel") : "");
            return;
        }


        int courseId = Integer.parseInt(selectedItem.replaceFirst(".*\\(ID: (\\d+)\\)$", "$1"));

        String status = "";
        if (notStartedCheckBox.isSelected()) {
            status = "Not started";
        } else if (ongoingCheckBox.isSelected()) {
            status = "Ongoing";
        }


        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        LocalDateTime assignmentDeadline = LocalDateTime.of(date, LocalTime.parse(deadlineTimeString, timeFormatter));
        Assignment assignment = new Assignment(courseId, assignmentTitle, description, assignmentDeadline, status);
        assignmentDAO.add(assignment);
        System.out.println("Assignment added" + assignment.getCourseId() + " " + assignment.getTitle() + " " + assignment.getDescription() + " " + assignment.getDeadline() + " " + assignment.getStatus());

        bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        // Small delay to ensure DB update before fetching data
        // Update the UI after saving the assignment
        new Thread(() -> {
            try {
                Thread.sleep(500); // Ensure database update completes
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

    }

    /**
     * Sets the timetable controller for this view.
     *
     * @param timetableController the timetable controller
     */
    public void setTimetableController(TimetableController timetableController) {
        this.timetableController = timetableController;
    }

    /**
     * Sets the resource bundle for localization and translates the UI.
     *
     * @param bundle the resource bundle
     */
    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        translateUI();
    }

    /**
     * Translates the UI components based on the provided resource bundle.
     * This method updates the text of various UI elements to match the selected language.
     */
    private void translateUI() {
        if (bundle != null) {
            assignmentSaveButton.setText(bundle.getString("saveButton"));
            backButton.setText(bundle.getString("backButton"));
            notStartedCheckBox.setText(bundle.getString("notStartedButton"));
            ongoingCheckBox.setText(bundle.getString("ongoingButton"));
            addAssignmentTitleLabel.setText(bundle.getString("addAssignmentTitleLabel"));
            assignmentCourseNameLabel.setText(bundle.getString("courseNameLabel"));
            assignmentTitleLabel.setText(bundle.getString("titleLabel"));
            assignmentDescriptionLabel.setText(bundle.getString("descriptionLabel"));
            dateLabel.setText(bundle.getString("dateLabel"));
            deadlineLabel.setText(bundle.getString("deadlineLabel"));
            timeLabel.setText(bundle.getString("timeLabel"));
            assignmentProgressLabel.setText(bundle.getString("progressLabel"));

            if ("ar".equals(bundle.getLocale().getLanguage())) {
                rootPane.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
            }

        }
    }
}
