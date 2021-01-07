package edu.uwb.ii.bubble_bobble.scenes.options;

import edu.uwb.ii.bubble_bobble.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class OptionsSceneController {

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }

    public void switchToBoardEditor(ActionEvent actionEvent) throws IOException {
        App.setRoot("editor");
    }
}
