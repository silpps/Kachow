package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller class for the "Add Task" view.
 * This class handles the user interactions and logic for adding a new task to the timetable.
 */
public class AddTaskController {

    @FXML
    private ChoiceBox<String> taskChoiceBox;
    @FXML
    private Button continueButton;
    @FXML
    private Button backButton;
    @FXML
    private AnchorPane taskPane;
    @FXML
    private Label taskLabel;
    @FXML
    private Label taskErrorLabel;
    @FXML
    private Label addPageTitleLabel;

    private TimetableController timetableController;
    private ResourceBundle bundle; // Store translation bundle

    // Map to store the mapping of localized task names to their English names
    private final Map<String, String> localizedToEnglishMap = new HashMap<>();

    /**
     * Sets the TimetableController instance.
     *
     * @param timetableController The TimetableController instance to set.
     */
    public void setTimetableController(TimetableController timetableController) {
        this.timetableController = timetableController;
    }

    /**
     * Sets the ResourceBundle for localization.
     *
     * @param bundle The ResourceBundle instance to set.
     */
    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        translateUI();
        populateLocalizedToEnglishMap();
    }

    /**
     * Sets the localized task names to their English equivalents.
     * This method is called to populate the map of localized task names to English task names.
     */
    private void populateLocalizedToEnglishMap() {
        localizedToEnglishMap.put(bundle.getString("course"), "Course");
        localizedToEnglishMap.put(bundle.getString("classSchedule"), "Class Schedule");
        localizedToEnglishMap.put(bundle.getString("assignment"), "Assignment");
        localizedToEnglishMap.put(bundle.getString("exam"), "Exam");
        localizedToEnglishMap.put(bundle.getString("studySession"), "Study Session");
    }

    /**
     * Initializes the controller.
     * Sets up the action handlers for the buttons and choice box.
     */
    @FXML
    public void initialize() {
        taskChoiceBox.setOnAction(this::getTask);
        backButton.setOnAction(event -> backButtonClicked());
        continueButton.setOnAction(event -> continueButtonClicked());
    }

    /**
     * Translates the UI elements based on the provided ResourceBundle.
     * This method updates the text of various UI components to support localization.
     */
    private void translateUI() {
        if (bundle != null) {
            backButton.setText(bundle.getString("backButton"));
            continueButton.setText(bundle.getString("continueButton"));
            addPageTitleLabel.setText(bundle.getString("addPageTitleLabel"));
            taskLabel.setText(bundle.getString("taskLabel"));

            // Translating ChoiceBox items
            taskChoiceBox.getItems().clear();
            taskChoiceBox.getItems().addAll(
                    bundle.getString("course"),
                    bundle.getString("classSchedule"),
                    bundle.getString("assignment"),
                    bundle.getString("exam"),
                    bundle.getString("studySession")
            );

            if ("ar".equals(bundle.getLocale().getLanguage())) {
                taskPane.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
            }
        }
    }

    /**
     * Handles the action when a task is selected from the choice box.
     * This method retrieves the selected task and can be used for further processing.
     *
     * @param event The ActionEvent triggered by the choice box selection.
     */
    public void getTask(ActionEvent event) {
        taskChoiceBox.getValue();
    }

    /**
     * Handles the action when the back button is clicked.
     * Closes the current window.
     */
    @FXML
    private void backButtonClicked() {
        backButton.getScene().getWindow().hide();
    }

    /**
     * Handles the action when the continue button is clicked.
     * This method checks if a task is selected and loads the corresponding FXML file.
     */
    @FXML
    private void continueButtonClicked() {
        String taskChoice = taskChoiceBox.getValue();
        if (taskChoice == null) {
            System.out.println("Please select a task before proceeding.");
            taskErrorLabel.setText(bundle.getString("taskErrorLabel"));
            return;
        }

        // Translate localized task choice to English
        String englishTaskChoice = localizedToEnglishMap.getOrDefault(taskChoice, taskChoice);

        try {
            // Load the corresponding FXML based on the task choice
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add" + englishTaskChoice + ".fxml"), bundle);
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/timetable.css");

            // Set up the controller for the new window based on task choice
            Object controller = loader.getController();
            if (controller instanceof AddAssignmentController) {
                ((AddAssignmentController) controller).setTimetableController(timetableController);
                ((AddAssignmentController) controller).setBundle(bundle);
            } else if (controller instanceof AddExamController) {
                ((AddExamController) controller).setTimetableController(timetableController);
                ((AddExamController) controller).setBundle(bundle);
            } else if (controller instanceof AddStudySessionController) {
                ((AddStudySessionController) controller).setTimetableController(timetableController);
                ((AddStudySessionController) controller).setBundle(bundle);
            } else if (controller instanceof AddClassScheduleController) {
                ((AddClassScheduleController) controller).setTimetableController(timetableController);
                ((AddClassScheduleController) controller).setBundle(bundle);
            } else if (controller instanceof AddCourseController) {
                ((AddCourseController) controller).setTimetableController(timetableController);
                ((AddCourseController) controller).setBundle(bundle);
            }

            // Show the new window
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            continueButton.getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
