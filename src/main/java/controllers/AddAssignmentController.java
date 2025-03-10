package controllers;

import dao.AssignmentDAO;
import dao.TimeTableDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Assignment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddAssignmentController {
    TimeTableDAO timeTableDAO;
    TimetableController timetableController;

    private AssignmentDAO assignmentDAO;

    @FXML
    private ChoiceBox<String> courseNameChoiceBox;

    @FXML
    private TextField assignmentTitleTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private DatePicker assignmentDatePicker;

    @FXML
    private CheckBox notStartedCheckBox, ongoingCheckBox;

    @FXML
    private Button backButton, assignmentSaveButton;

    @FXML
    private ChoiceBox<String> deadlineChoiceBox;

    private final String[] deadlineTimes = {"6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00" };

    @FXML
    private void backButtonClicked() {
        backButton.getScene().getWindow().hide();
    }

    @FXML
    public void initialize(){
        timeTableDAO = new TimeTableDAO();
        List<String> courseNames = timeTableDAO.getCourseNames();
        courseNameChoiceBox.getItems().addAll(courseNames);

        assignmentDAO = new AssignmentDAO();

        deadlineChoiceBox.getItems().addAll(deadlineTimes);
        backButton.setOnAction(e -> backButtonClicked());
        assignmentSaveButton.setOnAction(e -> assignmentSaveButtonClicked());
    }

    @FXML
    private void assignmentSaveButtonClicked() {
        // Save the assignment details
        String courseName = courseNameChoiceBox.getValue();
        String assignmentTitle = assignmentTitleTextField.getText();
        String description = descriptionTextArea.getText();
        LocalDate date = assignmentDatePicker.getValue();
        String deadlineTimeString = deadlineChoiceBox.getValue();
        String status = "";
        if (notStartedCheckBox.isSelected()) {
            status = "Not started";
        } else if (ongoingCheckBox.isSelected()) {
            status = "Ongoing";
        }

        if (date != null && deadlineTimeString != null) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
            LocalDateTime assignmentDeadline = LocalDateTime.of(date, LocalTime.parse(deadlineTimeString, timeFormatter));
            System.out.println(assignmentDeadline);
            System.out.println(date);

            Assignment assignment = new Assignment(courseName, assignmentTitle, description, assignmentDeadline, status);
            assignmentDAO.add(assignment);
            System.out.println("Assignment added" + assignment.getCourseName() + " " + assignment.getTitle() + " " + assignment.getDescription() + " " + assignment.getDeadline() + " " + assignment.getStatus());
        }

        // Small delay to ensure DB update before fetching data
        // Update the UI after saving the assignment
        new Thread(() -> {
            try {
                Thread.sleep(500); // Ensure database update completes
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
