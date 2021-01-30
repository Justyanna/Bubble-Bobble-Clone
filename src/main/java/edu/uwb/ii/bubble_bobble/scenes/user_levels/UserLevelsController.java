package edu.uwb.ii.bubble_bobble.scenes.user_levels;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.utils.CurrentLanguageVersionProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class UserLevelsController {

    private static final String MAPS_PATH = System.getProperty("user.home") + "/AppData/Local/Bubble Bobble Clone/maps";
    @FXML
    Button goToMenu;
    @FXML
    VBox centerBox;
    Label empty;
    File[] listOfFiles;
    File[] filesSlice;
    int maxPerPage = 12;
    int currentPage = 1;
    int indexOfLast = currentPage * maxPerPage;
    int indexOfFirst = indexOfLast - maxPerPage;
    @FXML
    Button next;
    @FXML
    Button previous;

    void getSlice() {
        filesSlice = Arrays.copyOfRange(listOfFiles, indexOfFirst, indexOfLast);
    }

    public void initialize() {
        empty = new Label();
        loadLanguageVersion();
        File mapDir = new File(MAPS_PATH);
        listOfFiles = mapDir.listFiles();
        previous.setVisible(false);

        if (listOfFiles.length > 0) {
            if (listOfFiles.length > maxPerPage) {
                getSlice();
                reload();
            } else {
                next.setVisible(false);
                filesSlice = listOfFiles;
                reload();
            }
        } else {
            next.setVisible(false);
            previous.setVisible(false);
            empty.getStyleClass().add("label");
            centerBox.getChildren().add(empty);
        }
    }

    void reload() {
        centerBox.getChildren().clear();

        for (File file : filesSlice) {
            String mapName = String.format("%12s", file.getName().replace(".xml", ""));
            Label mapname = new Label(mapName);
            mapname.getStyleClass().add("label");
            mapname.minWidth(150);
            Button playButton = getButton("Play", "button-play");
            playButton.setOnMouseClicked(event -> handlePlayMap(event));
            Button deleteButton = getButton("Delete", "button-delete");
            deleteButton.setOnMouseClicked(event -> handleDeleteMap(event));

            HBox rowBox = new HBox();
            rowBox.getChildren().addAll(mapname, playButton, deleteButton);
            rowBox.setAlignment(Pos.CENTER);
            rowBox.setSpacing(40);

            centerBox.getChildren().add(rowBox);
        }
    }

    private void handlePlayMap(MouseEvent event) {
        Button self = (Button) event.getSource();
        Optional<javafx.scene.Node> mapname = self.getParent()
                .getChildrenUnmodifiable()
                .stream()
                .filter(x -> x.getClass().equals(Label.class))
                .findFirst();
        Label label = (Label) mapname.get();
        String filename = label.getText().trim() + ".xml";
        App.customMapName = filename;
        try {
            switchToGame();
        } catch (IOException e) {

        }
    }

    private Button getButton(String play, String s) {
        Button playButton = new Button(play);
        playButton.setPrefHeight(50);
        playButton.setPrefWidth(200);
        playButton.getStyleClass().add(s);
        return playButton;
    }

    private void handleDeleteMap(MouseEvent event) {
        Button self = (Button) event.getSource();
        Optional<javafx.scene.Node> mapname = self.getParent()
                .getChildrenUnmodifiable()
                .stream()
                .filter(x -> x.getClass().equals(Label.class))
                .findFirst();
        Label label = (Label) mapname.get();
        String filename = label.getText().trim() + ".xml";
        File file = new File(MAPS_PATH + "/" + filename);
        file.delete();
        initialize();
    }

    @FXML
    void switchToPrimary(ActionEvent actionEvent) throws IOException {
        App.setRoot("menu");
    }

    @FXML
    void switchToGame() throws IOException {
        App.setRoot("game");
    }

    private void loadLanguageVersion() {
        Document doc = CurrentLanguageVersionProvider.loadXml();
        processXml(doc);
    }

    private void processXml(Document doc) {
        if (doc != null) {
            doc.getDocumentElement().normalize();
            Node menu = doc.getElementsByTagName("UserLevels").item(0);

            for (int i = 0; i < menu.getChildNodes().getLength(); i++) {

                Node node = menu.getChildNodes().item(i);
                String id = node.getNodeName();
                var text = node.getTextContent();

                if (id == null || text == null) {
                    continue;
                }

                if ("goToMenu".equals(id)) {
                    goToMenu.setText(text);
                } else if ("next".equals(id)) {
                    next.setText(text);
                } else if ("previous".equals(id)) {
                    previous.setText(text);
                } else if ("empty".equals(id)) {
                    empty.setText(text);
                }
            }
        }
    }

    @FXML
    void turnPageBack(ActionEvent actionEvent) {
        if (currentPage > 1) {
            currentPage--;
            indexOfLast = currentPage * maxPerPage;
            indexOfFirst = indexOfLast - maxPerPage;
            getSlice();
            reload();
        }

        if (!(listOfFiles.length <= indexOfLast)) {
            next.setVisible(true);
        }

        if (currentPage == 1) {
            previous.setVisible(false);
            next.setVisible(true);
        }
    }

    @FXML
    void turnPageNext(ActionEvent actionEvent) {
        currentPage++;
        indexOfFirst = indexOfLast;
        indexOfLast = Math.min(currentPage * maxPerPage, listOfFiles.length);
        getSlice();
        reload();
        if (indexOfLast >= listOfFiles.length) {
            next.setVisible(false);
        }
        previous.setVisible(true);
    }
}
