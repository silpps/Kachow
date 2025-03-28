package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

    public class View extends Application {

        @Override
        public void start(Stage stage) throws Exception {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/timetable.fxml"));
            Parent root = fxmlLoader.load();
            stage.getIcons().add(new Image("/calendar.png"));
            stage.setTitle("StudyFlow");
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/timetable.css");
            stage.setScene(scene);
            stage.show();
        }
}
