package edu.uwb.ii.bubble_bobble.scenes.menu;

import edu.uwb.ii.bubble_bobble.App;
import javafx.fxml.FXML;

import java.io.IOException;

public class MenuSceneController {

    @FXML
    private void switchToGame() throws IOException {
        App.setRoot("game");
    }

    @FXML
    private void switchToOptions() throws IOException {
        App.setRoot("options");
    }

    @FXML
    private void switchToLeaderboardAbout() throws IOException {
        App.setRoot("leaderboard_about");
    }
}
