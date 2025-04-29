package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller class for the Help window
 */
public class HelpViewController {
    @FXML
    private Label mainDialog;
    @FXML
    private Label mainGuide;

    public HelpViewController() {}

    /**
     * sets the text for the first dialog
     * @param dialog the dialog text
     */
    public void setMainDialog(String dialog) {
        this.mainDialog.setText(dialog);
    }

    /**
     * sets the text for the first guide
     * @param mainGuide text for the first guide
     */
    public void setMainGuide(String mainGuide) {
        this.mainGuide.setText(mainGuide);
    }
}
