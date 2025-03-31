package controllers;

import dao.ExamDAO;
import dao.IDAO;
import dao.TimeTableDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Exam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class AddExamController {

    private IDAO<Exam> examDAO;
    private Map<Integer, String> courses;

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
        if (selectedItem != null) {
            int courseId = Integer.parseInt(selectedItem.replaceAll("[^0-9]", ""));
            String examTitle = examTitleTextField.getText();
            String description = descriptionTextArea.getText();
            String location = locationTextField.getText();
            LocalDate examDate = examDatePicker.getValue();
            String fromTimeString = fromChoiceBox.getValue();

            if (examDate != null && fromTimeString != null) {
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
                LocalDateTime examDateTime = LocalDateTime.of(examDate, LocalTime.parse(fromTimeString, timeFormatter));
                System.out.println(examDateTime);
                System.out.println(examDate);


                Exam exam = new Exam(courseId, examDateTime, examTitle, description, location);
                examDAO.add(exam);
            }

            // Update the UI after saving the exam
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
    }

    public void setTimetableController(TimetableController timetableController) {
        this.timetableController = timetableController;
    }
}