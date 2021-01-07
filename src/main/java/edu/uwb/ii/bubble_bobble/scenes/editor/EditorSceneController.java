package edu.uwb.ii.bubble_bobble.scenes.editor;

import edu.uwb.ii.bubble_bobble.App;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class EditorSceneController {
    private static final int ROWS = 26;
    private static final int COLUMNS = 32;
    public StackPane boardWindow;

    public void initialize() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(1, 1, 1, 1));
        grid.setHgap(1);
        grid.setVgap(2);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                ToggleButton button = new ToggleButton();
                button.setPrefHeight(35);
                button.setPrefWidth(50);
                button.getStyleClass().add("grid-button");
                grid.add(button, c, r);
            }
        }
        boardWindow.getChildren().add(grid);
    }

    public void switchToOptions(ActionEvent actionEvent) throws IOException {
        App.setRoot("options");
    }
}
