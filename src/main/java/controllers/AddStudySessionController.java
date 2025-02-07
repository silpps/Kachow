package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.CourseService;
import models.StudySession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddStudySessionController {

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

    private String[] startTimes = {"06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};

    private String[] endTimes = {"07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"};

    @FXML
    private void initialize() {
        List<String> courseNames = CourseService.getInstance().getCourses();
        courseNameChoiceBox.getItems().addAll(courseNames);

        fromChoiceBox.getItems().addAll(startTimes);
        toChoiceBox.getItems().addAll(endTimes);
        sessionSaveButton.setOnAction(e -> sessionSaveButtonClicked());
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
            LocalTime fromTime = LocalTime.parse(fromTimeString, timeFormatter);
            LocalTime toTime = LocalTime.parse(toTimeString, timeFormatter);
            if (fromTime.isAfter(toTime)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid time");
                alert.setContentText("From time must be before to time");
                alert.showAndWait();
                return;
            }

                StudySession studySession = new StudySession(courseName, sessionTitle, description, date, fromTime, toTime);

                /*StudySessionDao studySessionDao = new StudySessionDao();
                studySessionDao.addStudySession(studySession);*/
            }


        }
    }


