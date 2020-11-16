package edu.uwb.ii.bubble_bobble.scenes.leaderboard_about;

import edu.uwb.ii.bubble_bobble.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LeaderboardAboutSceneController implements Initializable {
    String sceneName;

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(resourceBundle);
    }
}
