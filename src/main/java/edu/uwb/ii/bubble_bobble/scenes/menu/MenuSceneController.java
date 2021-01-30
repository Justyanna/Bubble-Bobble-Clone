package edu.uwb.ii.bubble_bobble.scenes.menu;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.utils.CurrentLanguageVersionProvider;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;

public class MenuSceneController {

    public Button playGame;
    public Button leaderboard;
    public Button options;
    public Button aboutUsButton;
    public Button exitButton;
    @FXML
    Button userLevels;

    public void initialize() {
        loadLanguageVersion();
    }

    private void loadLanguageVersion() {
        Document doc = CurrentLanguageVersionProvider.loadXml();
        processXml(doc);
    }

    private void processXml(Document doc) {
        if (doc != null) {
            doc.getDocumentElement().normalize();
            Node menu = doc.getElementsByTagName("Menu").item(0);

            for (int i = 0; i < menu.getChildNodes().getLength(); i++) {

                Node node = menu.getChildNodes().item(i);
                String id = node.getNodeName();
                var text = node.getTextContent();

                if (id == null || text == null) {
                    continue;
                }

                if ("playGame".equals(id)) {
                    playGame.setText(text);
                } else if ("leaderboard".equals(id)) {
                    leaderboard.setText(text);
                } else if ("options".equals(id)) {
                    options.setText(text);
                } else if ("aboutUsButton".equals(id)) {
                    aboutUsButton.setText(text);
                } else if ("exitButton".equals(id)) {
                    exitButton.setText(text);
                } else if ("userLevels".equals(id)) {
                    userLevels.setText(text);
                }
            }
        }
    }

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
        App.setRoot("leaderboard");
    }

    @FXML
    void closeApplication(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void switchToAboutUs(ActionEvent actionEvent) throws IOException {
        App.setRoot("about_us");
    }

    @FXML
    void switchToLevelSelect(ActionEvent actionEvent) throws IOException {
        App.setRoot("user_levels");
    }
}
