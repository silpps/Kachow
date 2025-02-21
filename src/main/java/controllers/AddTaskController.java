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

    private String[] tasks = {"Course", "Class Schedule", "Assignment", "Exam", "Study Session"};

    @FXML
    private Button continueButton;

    @FXML
    private Button backButton;

    private TimetableController_v2 timetableController;

    public void setTimetableController(TimetableController_v2 timetableController) {
        this.timetableController = timetableController;
    }


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
        if (taskChoice == null) {
            System.out.println("Please select a task before proceeding.");
            return;
        }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/add" + taskChoice + ".fxml"));
                Scene scene = new Scene(loader.load());
                Object controller = loader.getController();

                if (controller instanceof AddAssignmentController) {
                    ((AddAssignmentController) controller).setTimetableController(timetableController);
                } else if (controller instanceof AddExamController) {
                    ((AddExamController) controller).setTimetableController(timetableController);
                } else if (controller instanceof AddStudySessionController) {
                    ((AddStudySessionController) controller).setTimetableController(timetableController);
                } else if (controller instanceof AddCourseController) {
                    ((AddCourseController) controller).setTimetableController(timetableController);
                }
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }
            catch (Exception e){
                e.printStackTrace();
            }

    }

}
