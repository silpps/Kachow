package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import models.TimetableTime;

public class TimetableController {

    @FXML
    private TableView<TimetableTime> timetable;

    @FXML
    private TableColumn<TimetableTime, String> timeColumn;

    @FXML
    private Button addButton;

    @FXML
    private void initialize(){
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        fillTimeSlots();
    }

    @FXML
    private void addButtonClicked(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addTask.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void fillTimeSlots(){
        for (int i = 6; i < 25; i++){
            String time = i + ":00";
            TimetableTime timetableTime = new TimetableTime(time);
            timetable.getItems().add(timetableTime);
        }
    }



}
