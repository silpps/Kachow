package controllers;

import dao.CourseDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Course;

import java.net.URL;
import java.util.ResourceBundle;

public class AddTaskController implements Initializable {

    @FXML
    private ChoiceBox<String> taskChoiceBox;

    private String[] tasks = {"Assignment", "Course", "Exam", "Study Session"};

    @FXML
    private Button continueButton;

    @FXML
    private Button backButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        taskChoiceBox.getItems().addAll(tasks);
        taskChoiceBox.setOnAction(this::getTask);
        backButton.setOnAction(event -> backButtonClicked());
        continueButton.setOnAction(event -> continueButtonClicked());
    }

    public void getTask(ActionEvent event){
        String task = taskChoiceBox.getValue();
    }


    @FXML
    private void backButtonClicked(){
        backButton.getScene().getWindow().hide();
    }

    @FXML
    private void continueButtonClicked(){
        String taskChoice = taskChoiceBox.getValue();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/add" + taskChoice + ".fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }
            catch (Exception e){
                e.printStackTrace();
            }

    }

}
