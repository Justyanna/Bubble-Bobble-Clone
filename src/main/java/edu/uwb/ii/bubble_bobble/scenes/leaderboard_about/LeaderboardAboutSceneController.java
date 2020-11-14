package edu.uwb.ii.bubble_bobble.scenes.leaderboard_about;

import edu.uwb.ii.bubble_bobble.App;
import javafx.fxml.FXML;

import java.io.IOException;

public class LeaderboardAboutSceneController {
    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }
}
