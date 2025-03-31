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

public class AddTaskController {

    @FXML
    private ChoiceBox<String> taskChoiceBox;

    @FXML
    private Button continueButton, backButton;

    @FXML
    private AnchorPane taskPane;

    @FXML
    private Label addPageTitleLabel, taskLabel;

    private TimetableController timetableController;
    private ResourceBundle bundle; // Store translation bundle

    // Map to store the mapping of localized task names to their English names
    private Map<String, String> localizedToEnglishMap = new HashMap<>();

    public void setTimetableController(TimetableController timetableController) {
        this.timetableController = timetableController;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        translateUI();
        populateLocalizedToEnglishMap();
    }

    // Populate map of localized task names to English task names
    private void populateLocalizedToEnglishMap() {
        localizedToEnglishMap.put(bundle.getString("course"), "Course");
        localizedToEnglishMap.put(bundle.getString("classSchedule"), "Class Schedule");
        localizedToEnglishMap.put(bundle.getString("assignment"), "Assignment");
        localizedToEnglishMap.put(bundle.getString("exam"), "Exam");
        localizedToEnglishMap.put(bundle.getString("studySession"), "Study Session");
    }

    @FXML
    public void initialize() {
        taskChoiceBox.setOnAction(this::getTask);
        backButton.setOnAction(event -> backButtonClicked());
        continueButton.setOnAction(event -> continueButtonClicked());
    }

    // Translate the UI components
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

            if (bundle.getLocale().getLanguage().equals("ar")) {
                taskPane.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
            }
        }
    }

    public void getTask(ActionEvent event) {
        taskChoiceBox.getValue();
    }

    @FXML
    private void backButtonClicked() {
        backButton.getScene().getWindow().hide();
    }

    @FXML
    private void continueButtonClicked() {
        String taskChoice = taskChoiceBox.getValue();
        if (taskChoice == null) {
            System.out.println("Please select a task before proceeding.");
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
