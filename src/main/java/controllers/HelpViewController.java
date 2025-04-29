package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelpViewController {
    @FXML
    private Label mainDialog;
    @FXML
    private Label mainGuide;

    public HelpViewController() {}

    public void setMainDialog(String dialog) {
        this.mainDialog.setText(dialog);
    }

    public void setMainGuide(String mainGuide) {
        this.mainGuide.setText(mainGuide);
    }
}
