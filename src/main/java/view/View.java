package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * main.Main class for the JavaFX application.
 * This class initializes the application, sets the default locale,
 * loads the FXML file, and sets up the primary stage.
 * It also sets the application icon and title.
 */
public class View extends Application {

        @Override
        public void start(Stage stage) throws Exception {
            Locale.setDefault(new Locale("en", "UK"));
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/timetable.fxml"), ResourceBundle.getBundle("messages"));
            Parent root = fxmlLoader.load();
            stage.getIcons().add(new Image("/calendar.png"));
            stage.setTitle("StudyFlow");
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/timetable.css");
            stage.setScene(scene);
            stage.show();
        }
}
