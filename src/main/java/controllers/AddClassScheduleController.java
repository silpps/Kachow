package controllers;

import dao.ClassScheduleDAO;
import dao.IDAO;
import dao.TimeTableDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import models.ClassSchedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Locale;

/**
 * Controller class for the "Add Class Schedule" view.
 * This class handles user interactions and logic for adding a new class schedule to the timetable.
 * It validates user input, interacts with the database through DAOs, and updates the timetable view.
 */
public class AddClassScheduleController {
    private final String[] startTimes = {"6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
    private final String[] endTimes = {"7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"};
    private IDAO<ClassSchedule> classScheduleDAO;
    private ResourceBundle bundle;
    private TimetableController timetableController;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private DatePicker sessionDatePicker;
    @FXML
    private ChoiceBox<String> courseNameChoiceBox;
    @FXML
    private TextField locationTextField;
    @FXML
    private TextArea scheduleDescriptionArea;
    @FXML
    private ChoiceBox<String> fromChoiceBox;
    @FXML
    private ChoiceBox<String> toChoiceBox;
    @FXML
    private Button scheduleBackButton;
    @FXML
    private Button scheduleSaveButton;
    @FXML
    private Label addClassScheduleTitleLabel, courseNameLabel, locationLabel, sessionDateLabel, fromLabel, toLabel, descriptionLabel;
    @FXML
    private Label courseErrorLabel, dateErrorLabel, fromTimeErrorLabel, toTimeErrorLabel;

    /**
     * Initializes the controller.
     * Populates the course choice box with available courses and the time choice boxes with predefined start and end times.
     * Sets up event handlers for the save and back buttons.
     * Retrieves course data from the database using {@link TimeTableDAO}.
     */
    @FXML
    private void initialize() {
        classScheduleDAO = new ClassScheduleDAO();
        TimeTableDAO timeTableDAO = new TimeTableDAO();
        Map<Integer, String> courses = timeTableDAO.getCourses();
        for (Map.Entry<Integer, String> entry : courses.entrySet()) {
            courseNameChoiceBox.getItems().add(entry.getValue() + " (ID: " + entry.getKey() + ")");
        }

        fromChoiceBox.getItems().addAll(startTimes);
        toChoiceBox.getItems().addAll(endTimes);
        scheduleSaveButton.setOnAction(e -> scheduleSaveButtonClicked());
        scheduleBackButton.setOnAction(e -> scheduleBackButtonClicked());
    }

    /**
     * Handles the action when the back button is clicked.
     * Closes the current window.
     */
    @FXML
    private void scheduleBackButtonClicked() {
        // Close the window
        scheduleBackButton.getScene().getWindow().hide();
    }

    /**
     * Handles the action when the save button is clicked.
     * Validates the input fields, checks for logical errors (e.g., start time is after end time),
     * and saves the class schedule to the database if all validations pass.
     * Updates the timetable view after successfully saving the class schedule.
     */
    @FXML
    private void scheduleSaveButtonClicked() {
        // Get the selected course name
        String selectedItem = courseNameChoiceBox.getValue();
        String location = locationTextField.getText();
        String description = scheduleDescriptionArea.getText();
        LocalDate date = sessionDatePicker.getValue();
        String fromTimeString = fromChoiceBox.getValue();
        String toTimeString = toChoiceBox.getValue();

        if (selectedItem == null || date == null || fromTimeString == null || toTimeString == null) {
            courseErrorLabel.setText(selectedItem == null ? bundle.getString("courseErrorLabel") : "");
            dateErrorLabel.setText(date == null ? bundle.getString("dateErrorLabel") : "");
            fromTimeErrorLabel.setText(fromTimeString == null ? bundle.getString("timeErrorLabel") : "");
            toTimeErrorLabel.setText(toTimeString == null ? bundle.getString("timeErrorLabel") : "");
            return;
        }

        // Get the course ID
        int courseId = Integer.parseInt(selectedItem.replaceFirst(".*\\(ID: (\\d+)\\)$", "$1"));


        // Parse the start and end times
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        LocalDateTime fromTime = LocalDateTime.of(date, LocalTime.parse(fromTimeString, timeFormatter));
        LocalDateTime toTime = LocalDateTime.of(date, LocalTime.parse(toTimeString, timeFormatter));
        if (fromTime.isAfter(toTime)) {
            fromTimeErrorLabel.setText(bundle.getString("toTimeBeforeFromTimeError"));
            return;
        }
        ClassSchedule classSchedule = new ClassSchedule(courseId, location, description, fromTime, toTime);
        classScheduleDAO.add(classSchedule);
        System.out.println("Class Schedule added" + classSchedule.getCourseId() + " " + classSchedule.getLocation());


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

        // Close the window
        scheduleBackButtonClicked();

    }

    /**
     * Sets the resource bundle for localization and translates the UI components.
     *
     * @param bundle the resource bundle containing localized strings.
     */
    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        translateUI();
    }

    /**
     * Translates the UI components using the provided resource bundle.
     * Sets the text of various labels and buttons based on the current locale.
     */
    private void translateUI() {
        if (bundle != null) {
            scheduleSaveButton.setText(bundle.getString("saveButton"));
            scheduleBackButton.setText(bundle.getString("backButton"));
            addClassScheduleTitleLabel.setText(bundle.getString("addClassScheduleTitleLabel"));
            courseNameLabel.setText(bundle.getString("courseNameLabel"));
            locationLabel.setText(bundle.getString("locationBoxLabel"));
            sessionDateLabel.setText(bundle.getString("dateLabel"));
            fromLabel.setText(bundle.getString("fromTimeLabel"));
            toLabel.setText(bundle.getString("toTimeLabel"));
            descriptionLabel.setText(bundle.getString("descriptionLabel"));

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
