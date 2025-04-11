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

public class AddExamController {

    private IDAO<Exam> examDAO;
    private Map<Integer, String> courses;
    private ResourceBundle bundle;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ChoiceBox<String> courseNameChoiceBox;

    @FXML
    private TextField examTitleTextField, locationTextField;

    @FXML
    private DatePicker examDatePicker;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button backButton, examSaveButton;

    @FXML
    private Label addExamTitleLabel, courseNameLabel,  titleLabel ,examDateLabel, fromLabel, locationLabel, descriptionLabel;

    @FXML
    private Label courseErrorLabel, titleErrorLabel, dateErrorLabel, timeErrorLabel;

    @FXML
    private ChoiceBox<String> fromChoiceBox;

    private final String[] startTimes = {"6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};

    private TimetableController timetableController;

    @FXML
    public void initialize(){
        TimeTableDAO timeTableDAO = new TimeTableDAO();
        courses = timeTableDAO.getCourses();
        for (Map.Entry<Integer, String> entry : courses.entrySet()) {
            courseNameChoiceBox.getItems().add(entry.getValue() + " (ID: " + entry.getKey() + ")");
        }

        examDAO = new ExamDAO();

        fromChoiceBox.getItems().addAll(startTimes);
        backButton.setOnAction(e -> backButtonClicked());
        examSaveButton.setOnAction(e -> examSaveButtonClicked());
    }

    @FXML
    private void backButtonClicked() {
        // Close the current window
        backButton.getScene().getWindow().hide();
    }

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

        if (selectedItem != null) {
            int courseId = Integer.parseInt(selectedItem.replaceAll("[^0-9]", ""));


            if (examDate != null && fromTimeString != null) {
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
                LocalDateTime examDateTime = LocalDateTime.of(examDate, LocalTime.parse(fromTimeString, timeFormatter));
                System.out.println(examDateTime);
                System.out.println(examDate);


                Exam exam = new Exam(courseId, examDateTime, examTitle, description, location);
                examDAO.add(exam);
            }

            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
            // Update the UI after saving the exam
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Platform.runLater(() -> {
                    System.out.println("Updating UI...");
                    timetableController.fetchAndDisplayCurrentWeeksData(bundle);
                    System.out.println("UI updated.");
                });
            }).start();

            backButtonClicked();
        }
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        translateUI();
    }

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

            if (bundle.getLocale().getLanguage().equals("ar")) {
                rootPane.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
            }
        }
    }

    public void setTimetableController(TimetableController timetableController) {
        this.timetableController = timetableController;
    }
}