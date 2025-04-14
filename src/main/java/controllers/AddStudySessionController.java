package controllers;

import dao.IDAO;
import dao.StudySessionDAO;
import dao.TimeTableDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import models.StudySession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Locale;

/**
 * Controller class for the "Add Study Session" view.
 * This class handles the user interactions and logic for adding a new study session to the timetable.
 * It validates user input, interacts with the database through DAOs, and updates the timetable view.
 */
public class AddStudySessionController {

    private IDAO<StudySession> studySessionDAO;

    private ResourceBundle bundle;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ChoiceBox<String> courseNameChoiceBox;

    @FXML
    private TextField sessionTitleTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private DatePicker sessionDatePicker;

    @FXML
    private ChoiceBox<String> fromChoiceBox;

    @FXML
    private ChoiceBox<String> toChoiceBox;

    @FXML
    private Button sessionSaveButton;

    @FXML
    private Button sessionBackButton;

    @FXML
    private Label addStudySessionTitleLabel, addCourseNameLabel, addSessionTitleLabel, addSessionDescriptionLabel, sessionDateLabel, fromLabel, toLabel;

    @FXML
    private Label courseErrorLabel, titleErrorLabel, dateErrorLabel, fromTimeErrorLabel, toTimeErrorLabel;

    private final String[] startTimes = {"6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};

    private final String[] endTimes = {"7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"};

    private TimetableController timetableController;

    /**
     * Initializes the controller.
     * Populates the course choice box and time choice boxes with data.
     * Sets up event handlers for the save and back buttons.
     * Retrieves course data from the database using {@link TimeTableDAO}.
     */
    @FXML
    private void initialize() {
        TimeTableDAO timeTableDAO = new TimeTableDAO();
        Map<Integer, String> courses = timeTableDAO.getCourses();
        for (Map.Entry<Integer, String> entry : courses.entrySet()) {
            courseNameChoiceBox.getItems().add(entry.getValue() + " (ID: " + entry.getKey() + ")");
        }

        studySessionDAO = new StudySessionDAO();

        fromChoiceBox.getItems().addAll(startTimes);
        toChoiceBox.getItems().addAll(endTimes);
        sessionSaveButton.setOnAction(e -> sessionSaveButtonClicked());
        sessionBackButton.setOnAction(e -> sessionBackButtonClicked());
    }

    /**
     * Handles the action when the back button is clicked.
     * Closes the current window.
     */
    public void sessionBackButtonClicked() {
        sessionBackButton.getScene().getWindow().hide();
    }

    /**
     * Handles the action when the save button is clicked.
     * Validates the input fields, checks for logical errors (e.g., from time after to time),
     * and adds the study session to the database if validation passes.
     * Updates the timetable view after successfully adding the session.
     */
    public void sessionSaveButtonClicked() {
        String selectedItem = courseNameChoiceBox.getValue();
        String sessionTitle = sessionTitleTextField.getText();
        String description = descriptionTextArea.getText();
        LocalDate date = sessionDatePicker.getValue();
        String fromTimeString = fromChoiceBox.getValue();
        String toTimeString = toChoiceBox.getValue();

        if (selectedItem == null || sessionTitle.isEmpty() || date == null || fromTimeString == null || toTimeString == null) {
            courseErrorLabel.setText(selectedItem == null ? bundle.getString("courseErrorLabel") : "");
            titleErrorLabel.setText(sessionTitle.isEmpty() ? bundle.getString("titleErrorLabel") : "");
            dateErrorLabel.setText(date == null ? bundle.getString("dateErrorLabel") : "");
            fromTimeErrorLabel.setText(fromTimeString == null ? bundle.getString("timeErrorLabel") : "");
            toTimeErrorLabel.setText(toTimeString == null ? bundle.getString("timeErrorLabel") : "");
            return;
        }


        int courseId = Integer.parseInt(selectedItem.replaceFirst(".*\\(ID: (\\d+)\\)$", "$1"));


        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        LocalDateTime fromTime = LocalDateTime.of(date, LocalTime.parse(fromTimeString, timeFormatter));
        LocalDateTime toTime = LocalDateTime.of(date, LocalTime.parse(toTimeString, timeFormatter));
        System.out.println(fromTime);
        System.out.println(toTime);
        System.out.println(date);
        if (fromTime.isAfter(toTime)) {
            fromTimeErrorLabel.setText(bundle.getString("toTimeBeforeFromTimeError"));
            return;
        }

        StudySession studySession = new StudySession(courseId, sessionTitle, description, fromTime, toTime);
        studySessionDAO.add(studySession);
        System.out.println("Study session added, CourseId:" + studySession.getCourseId() + " " + studySession.getTitle() + " " + studySession.getDescription());

        bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

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


        sessionBackButtonClicked();

    }

    /**
     * Sets the ResourceBundle for localization and translates the UI.
     *
     * @param bundle the ResourceBundle to set
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
            sessionSaveButton.setText(bundle.getString("saveButton"));
            sessionBackButton.setText(bundle.getString("backButton"));
            addStudySessionTitleLabel.setText(bundle.getString("addStudySessionTitleLabel"));
            addCourseNameLabel.setText(bundle.getString("courseNameLabel"));
            addSessionTitleLabel.setText(bundle.getString("titleLabel"));
            addSessionDescriptionLabel.setText(bundle.getString("descriptionLabel"));
            sessionDateLabel.setText(bundle.getString("dateLabel"));
            fromLabel.setText(bundle.getString("fromTimeLabel"));
            toLabel.setText(bundle.getString("toTimeLabel"));

            if (bundle.getLocale().getLanguage().equals("ar")) {
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


