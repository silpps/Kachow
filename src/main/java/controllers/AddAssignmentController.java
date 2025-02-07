package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class AddAssignmentController {

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
        backButton.setOnAction(e -> backButtonClicked());
        assignmentSaveButton.setOnAction(e -> assignmentSaveButtonClicked());
    }

    @FXML
    private void assignmentSaveButtonClicked() {
        String courseName = courseNameChoiceBox.getValue();
        String assignmentTitle = assignmentTitleTextField.getText();
        String description = descriptionTextArea.getText();
        LocalDate date = assignmentDatePicker.getValue();
        boolean notStarted = notStartedCheckBox.isSelected();
        boolean ongoing = ongoingCheckBox.isSelected();

        /*
        Assignment assignment = new Assignment(courseName, assignmentTitle, description, date, notStarted, ongoing);
        AssignmentDAO assignmentDAO = new AssignmentDAO();
        assignmentDAO.addAssignment(assignment);

         */
    }
}
