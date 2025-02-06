package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import java.net.URL;
import java.util.ResourceBundle;

public class AddTaskController implements Initializable {

    @FXML
    private ChoiceBox<String> taskChoiceBox;

    private String[] tasks = {"Assignment", "Course", "Exam", "Study Session"};

    @FXML
    private ChoiceBox<String> startTimeChoiceBox;

    @FXML
    private ChoiceBox<String> endTimeChoiceBox;

    private String[] startTimes = {"6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};

    private String[] endTimes = {"7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"};

    @FXML
    private Button saveButton;

    @FXML
    private Button backButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        taskChoiceBox.getItems().addAll(tasks);
        taskChoiceBox.setOnAction(this::getTask);
        startTimeChoiceBox.getItems().addAll(startTimes);
        endTimeChoiceBox.getItems().addAll(endTimes);
    }

    public void getTask(ActionEvent event){
        String task = taskChoiceBox.getValue();
    }

    public void getStartTime(ActionEvent event){
        String startTime = startTimeChoiceBox.getValue();
    }

    public void getEndTime(ActionEvent event){
        String endTime = endTimeChoiceBox.getValue();
    }

}
