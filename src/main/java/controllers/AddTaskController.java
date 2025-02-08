package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class AddTaskController{

    @FXML
    private ChoiceBox<String> taskChoiceBox;

    private String[] tasks = {"Class Schedule", "Assignment", "Course", "Exam", "Study Session"};

    @FXML
    private Button continueButton;

    @FXML
    private Button backButton;



    @FXML
    public void initialize() {
        taskChoiceBox.getItems().addAll(tasks);
        taskChoiceBox.setOnAction(this::getTask);
        backButton.setOnAction(event -> backButtonClicked());
        continueButton.setOnAction(event -> continueButtonClicked());
    }

    public String getTask(ActionEvent event){
        return taskChoiceBox.getValue();
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
