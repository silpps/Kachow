package controllers;

import dao.ExamDAO;
import dao.TimeTableDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Exam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddExamController {
    private TimeTableDAO timeTableDAO;

    private ExamDAO examDAO;

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
    private ChoiceBox<String> fromChoiceBox;

    private final String[] startTimes = {"6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};

    private TimetableController timetableController;

    @FXML
    public void initialize(){
        timeTableDAO = new TimeTableDAO();
        List<String> courseNames = timeTableDAO.getCourseNames();
        courseNameChoiceBox.getItems().addAll(courseNames);

        examDAO = new ExamDAO();

        fromChoiceBox.getItems().addAll(startTimes);
        backButton.setOnAction(e -> backButtonClicked());
        examSaveButton.setOnAction(e -> examSaveButtonClicked());
    }

    @FXML
    private void backButtonClicked() {
        backButton.getScene().getWindow().hide();
    }

    @FXML
    private void examSaveButtonClicked() {
        String courseName = courseNameChoiceBox.getValue();
        String examTitle = examTitleTextField.getText();
        String description = descriptionTextArea.getText();
        String location = locationTextField.getText();
        LocalDate examDate = examDatePicker.getValue();
        String fromTimeString = fromChoiceBox.getValue();

        if (examDate != null && fromTimeString != null) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime examDateTime = LocalDateTime.of(examDate, LocalTime.parse(fromTimeString, timeFormatter));
            System.out.println(examDateTime);
            System.out.println(examDate);


            Exam exam = new Exam(courseName, examDateTime, examTitle, description, location);
            examDAO.add(exam);
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

        backButtonClicked();
    }

    public void setTimetableController(TimetableController timetableController) {
        this.timetableController = timetableController;
    }
}