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

    public void sessionBackButtonClicked() {
        sessionBackButton.getScene().getWindow().hide();
    }

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

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        translateUI();
    }

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


    public void setTimetableController(TimetableController timetableController) {
        this.timetableController = timetableController;
    }
}


