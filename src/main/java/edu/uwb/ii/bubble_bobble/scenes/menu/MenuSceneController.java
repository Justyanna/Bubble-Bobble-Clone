package edu.uwb.ii.bubble_bobble.scenes.menu;

import edu.uwb.ii.bubble_bobble.App;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;



public class MenuSceneController {

    @FXML
    Button exitButton;

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

    @FXML
    void closeApplication(ActionEvent actionEvent) {
        Platform.exit();
    }

}
