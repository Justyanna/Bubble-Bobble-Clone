package edu.uwb.ii.bubble_bobble.scenes.about_us;

import edu.uwb.ii.bubble_bobble.App;
import javafx.fxml.FXML;

import java.io.IOException;

public class AboutUsSceneContoller {
    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }
}
