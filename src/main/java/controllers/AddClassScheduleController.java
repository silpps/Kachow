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
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Locale;

public class AddClassScheduleController {
    private IDAO<ClassSchedule> classScheduleDAO;
    private ResourceBundle bundle;

    private TimetableController timetableController;

    private Map<Integer, String> courses;

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

    private final String[] startTimes = {"6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};

    private final String[] endTimes = {"7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"};


    @FXML
    private void initialize() {
        classScheduleDAO = new ClassScheduleDAO();
        TimeTableDAO timeTableDAO = new TimeTableDAO();
        courses = timeTableDAO.getCourses();
        for (Map.Entry<Integer, String> entry : courses.entrySet()) {
            courseNameChoiceBox.getItems().add(entry.getValue() + " (ID: " + entry.getKey() + ")");
        }

        fromChoiceBox.getItems().addAll(startTimes);
        toChoiceBox.getItems().addAll(endTimes);
        scheduleSaveButton.setOnAction(e -> scheduleSaveButtonClicked());
        scheduleBackButton.setOnAction(e -> scheduleBackButtonClicked());
    }

    @FXML
    private void scheduleBackButtonClicked() {
        // Close the window
        scheduleBackButton.getScene().getWindow().hide();
    }

    @FXML
    private void scheduleSaveButtonClicked() {
        // Get the selected course name
        String selectedItem = courseNameChoiceBox.getValue();
        String location = locationTextField.getText();
        System.out.println("Location: " + location);
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

        if (selectedItem != null) {
            // Get the course ID
            int courseId = Integer.parseInt(selectedItem.replaceAll("[^0-9]", ""));



            // Parse the start and end times
            if (date != null && fromTimeString != null && toTimeString != null) {
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
                ClassSchedule classSchedule = new ClassSchedule(courseId, location, description, fromTime, toTime);
                classScheduleDAO.add(classSchedule);
                System.out.println("Class Schedule added" + classSchedule.getCourseId() + " " + classSchedule.getLocation());


            }
            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

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
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        translateUI();
    }

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

    public void setTimetableController(TimetableController timetableController) {
        this.timetableController = timetableController;
    }
}
