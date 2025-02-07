package controllers;

import dao.ExamDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.CourseService;
import models.Exam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AddExamController {

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
    public void initialize(){
        List<String> courseNames = CourseService.getInstance().getCourses();
        courseNameChoiceBox.getItems().addAll(courseNames);

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
        LocalDateTime examDate = examDatePicker.getValue().atStartOfDay();


        Exam exam = new Exam(courseName, examDate, examTitle, description, location);
        ExamDAO examDAO = new ExamDAO();
        examDAO.add(exam);

    }
}
