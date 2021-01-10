package edu.uwb.ii.bubble_bobble.scenes.options;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.utils.CurrentLanguageVersionProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class OptionsSceneController {

    private static final ObservableList<String> languages = FXCollections.observableArrayList("ENG", "PL", "FR");
    private static final List<String> languagesArr = Arrays.asList("ENG", "PL", "FR");
    @FXML
    Button goToMenu;
    @FXML
    Button boardEditor;
    @FXML
    Label languageVersion;
    @FXML
    private VBox spinnerBox;
    private Spinner languageSpinner;

    public void initialize() {
        loadLanguageVersion();
        languageSpinner = new Spinner(languages);
        languageSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        languageSpinner.setMinHeight(30);
        languageSpinner.setEditable(false);
        languageSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            String file_path = "language_versions/";
            switch (newValue) {
                case "PL":
                    file_path += "pl";
                    break;
                case "ENG":
                    file_path += "eng";
                    break;
                case "FR":
                    file_path += "fr";
                    break;
            }
            file_path += ".xml";
            CurrentLanguageVersionProvider.currentLanguageVersion = file_path;
            loadLanguageVersion();
        });

        switch (getCurrentSpinnerValue()) {
            case "PL":
                languageSpinner.increment();
                break;
            case "ENG":
                break;
            case "FR":
                languageSpinner.increment(2);
                break;
        }
        spinnerBox.getChildren().add(languageSpinner);
    }

    private void loadLanguageVersion() {
        Document doc = CurrentLanguageVersionProvider.loadXml();
        processXml(doc);
    }

    private void processXml(Document doc) {
        if (doc != null) {
            doc.getDocumentElement().normalize();
            Node menu = doc.getElementsByTagName("Options").item(0);

            for (int i = 0; i < menu.getChildNodes().getLength(); i++) {

                Node node = menu.getChildNodes().item(i);
                String id = node.getNodeName();
                var text = node.getTextContent();

                if (id == null || text == null) {
                    continue;
                }

                if ("boardEditor".equals(id)) {
                    boardEditor.setText(text);
                } else if ("goToMenu".equals(id)) {
                    goToMenu.setText(text);
                } else if ("languageVersion".equals(id)) {
                    languageVersion.setText(text);
                }
            }
        }
    }

    private String getCurrentSpinnerValue() {
        int slash = CurrentLanguageVersionProvider.currentLanguageVersion.indexOf("/") + 1;
        int dot = CurrentLanguageVersionProvider.currentLanguageVersion.indexOf(".");
        return CurrentLanguageVersionProvider.currentLanguageVersion.substring(slash, dot).toUpperCase();
    }

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }

    public void switchToBoardEditor(ActionEvent actionEvent) throws IOException {
        App.setRoot("editor");
    }
}
