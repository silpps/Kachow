package controllers;

import dao.AssignmentDAO;
import dao.TimeTableDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Assignment;
import models.CourseService;

import java.time.LocalDate;
import java.util.List;

public class AddAssignmentController {
    TimeTableDAO timeTableDAO;

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
        AssignmentDAO assignmentDAO = new AssignmentDAO();
        assignmentDAO.add(assignment);

        backButtonClicked();

    }

}
