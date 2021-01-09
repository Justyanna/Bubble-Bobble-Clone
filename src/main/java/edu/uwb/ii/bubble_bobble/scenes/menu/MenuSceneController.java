package edu.uwb.ii.bubble_bobble.scenes.menu;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.utils.CurrentLanguageVersionProvider;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;

public class MenuSceneController {

    public Button playGame;
    public Button leaderboard;
    public Button options;
    public Button aboutUsButton;
    public Button exitButton;

    public void initialize() {
        Document doc = loadXml();
        processXml(doc);
    }

    private Document loadXml() {
        Document doc = null;
        try {
            InputStream in = MenuSceneController.class.getClassLoader().getResourceAsStream(CurrentLanguageVersionProvider.currentLanguageVersion);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return doc;
    }

    private void processXml(Document doc) {
        if (doc != null) {
            doc.getDocumentElement().normalize();
            NodeList buttons = doc.getElementsByTagName("Button");

            for (int i = 0; i < buttons.getLength(); i++) {

                var attr = buttons.item(i).getAttributes();
                var id = attr.getNamedItem("id");
                var text = buttons.item(i).getTextContent();

                if (id == null || text == null) continue;

                if ("playGame".equals(id.getNodeValue())) {
                    playGame.setText(text);
                } else if ("leaderboard".equals(id.getNodeValue())) {
                    leaderboard.setText(text);
                } else if ("options".equals(id.getNodeValue())) {
                    options.setText(text);
                } else if ("aboutUsButton".equals(id.getNodeValue())) {
                    aboutUsButton.setText(text);
                } else if ("exitButton".equals(id.getNodeValue())) {
                    exitButton.setText(text);
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
}
