package edu.uwb.ii.bubble_bobble.scenes.game;

import edu.uwb.ii.bubble_bobble.App;
import javafx.fxml.FXML;

import java.io.IOException;

public class GameSceneController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("menu");
    }
}