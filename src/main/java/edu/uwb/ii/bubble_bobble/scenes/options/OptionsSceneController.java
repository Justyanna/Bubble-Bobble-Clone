package edu.uwb.ii.bubble_bobble.scenes.options;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.utils.CurrentLanguageVersionProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class OptionsSceneController {
    @FXML
    private VBox spinnerBox;
    private Spinner languageSpinner;
    private static final ObservableList<String> languages =
            FXCollections.observableArrayList(
                    "ENG", "PL", "FR"
            );
    private static final List<String> languagesArr = Arrays.asList("ENG", "PL", "FR");


    public void initialize() {
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
        });


        spinnerBox.getChildren().add(languageSpinner);
    }

    private String getCurrentSpinnerValue() {
        int slash = CurrentLanguageVersionProvider.currentLanguageVersion.indexOf("/") + 1;
        int dot = CurrentLanguageVersionProvider.currentLanguageVersion.indexOf(".");
        return CurrentLanguageVersionProvider.currentLanguageVersion.substring(slash, dot);
    }


    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }

    public void switchToBoardEditor(ActionEvent actionEvent) throws IOException {
        App.setRoot("editor");
    }
}
