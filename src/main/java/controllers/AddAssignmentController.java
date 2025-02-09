package controllers;

import dao.AssignmentDAO;
import dao.TimeTableDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Assignment;
import models.CourseService;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public class AddAssignmentController {
    TimeTableDAO timeTableDAO;
    TimetableController_v2 timetableController;

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
    private void backButtonClicked() {
        backButton.getScene().getWindow().hide();
    }

    @FXML
    public void initialize(){
        timeTableDAO = new TimeTableDAO();
        List<String> courseNames = timeTableDAO.getCourseNames();
        courseNameChoiceBox.getItems().addAll(courseNames);

        assignmentDAO = new AssignmentDAO();

        backButton.setOnAction(e -> backButtonClicked());
        assignmentSaveButton.setOnAction(e -> assignmentSaveButtonClicked());
    }

    @FXML
    private void assignmentSaveButtonClicked() {
        String courseName = courseNameChoiceBox.getValue();
        String assignmentTitle = assignmentTitleTextField.getText();
        String description = descriptionTextArea.getText();
        LocalDate date = assignmentDatePicker.getValue();
        String status = "";
        if (notStartedCheckBox.isSelected()) {
            status = "Not started";
        } else if (ongoingCheckBox.isSelected()) {
            status = "Ongoing";
        }

        Assignment assignment = new Assignment(courseName, assignmentTitle, description, date.atStartOfDay(), status);
        assignmentDAO.add(assignment);
        System.out.println("Assignment added" + assignment.getCourseName() + " " + assignment.getTitle() + " " + assignment.getDescription() + " " + assignment.getDueDate() + " " + assignment.getStatus());
        assignmentDAO.add(assignment); // Insert assignment into database

// Small delay to ensure DB update before fetching data
        new Thread(() -> {
            try {
                Thread.sleep(500); // Ensure database update completes
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                System.out.println("Updating UI...");
                timetableController.fetchAndDisplayCurrentWeekData(timetableController.timetable.getItems());
                timetableController.timetable.refresh();
                System.out.println("UI updated.");
            });
        }).start();

        backButtonClicked();
    }

    public void setTimetableController(TimetableController_v2 timetableController) {
        this.timetableController = timetableController;
    }
}
