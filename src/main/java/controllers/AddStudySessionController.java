package controllers;

import dao.StudySessionDAO;
import dao.TimeTableDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.CourseService;
import models.StudySession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddStudySessionController {
    private TimeTableDAO timeTableDAO;

    private StudySessionDAO studySessionDAO;

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

    private String[] startTimes = {"6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};

    private String[] endTimes = {"7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"};

    private TimetableController_v3 timetableController;

    @FXML
    private void initialize() {
        timeTableDAO = new TimeTableDAO();
        List<String> courseNames = timeTableDAO.getCourseNames();
        courseNameChoiceBox.getItems().addAll(courseNames);

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
        String courseName = courseNameChoiceBox.getValue();
        String sessionTitle = sessionTitleTextField.getText();
        String description = descriptionTextArea.getText();
        LocalDate date = sessionDatePicker.getValue();
        String fromTimeString = fromChoiceBox.getValue();
        String toTimeString = toChoiceBox.getValue();


        if (date != null && fromTimeString != null && toTimeString != null) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime fromTime = LocalDateTime.of(date, LocalTime.parse(fromTimeString, timeFormatter));
            LocalDateTime toTime = LocalDateTime.of(date, LocalTime.parse(toTimeString, timeFormatter));
            System.out.println(fromTime);
            System.out.println(toTime);
            System.out.println(date);
            if (fromTime.isAfter(toTime)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid time");
                alert.setContentText("From time must be before to time");
                alert.showAndWait();
                return;
            }

                StudySession studySession = new StudySession(courseName, sessionTitle, description, fromTime, toTime);
                studySessionDAO.add(studySession);
            System.out.println("Study session added" + studySession.getCourseName() + " " + studySession.getTitle() + " " + studySession.getDescription());
            }
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                System.out.println("Updating UI...");
                timetableController.fetchAndDisplayCurrentWeeksData();
                System.out.println("UI updated.");
            });
        }).start();



        sessionBackButtonClicked();
        }

    public void setTimetableController(TimetableController_v3 timetableController) {
        this.timetableController = timetableController;
    }
}


