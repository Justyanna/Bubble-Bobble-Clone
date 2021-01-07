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
    private static final int ROW_CORNER = ROWS - 1;
    private static final int COLUMNS = 32;
    private static final int COLUMNS_CORNER = COLUMNS - 1;
    public StackPane boardWindow;
    private GridPane grid;

    public void initialize() {
        grid = new GridPane();
        grid.setPadding(new Insets(1, 1, 1, 1));
        grid.setHgap(1);
        grid.setVgap(2);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                ToggleButton button = new ToggleButton();
                button.setPrefHeight(35);
                button.setPrefWidth(50);

                button.setOnMouseClicked(event -> {
                    Integer colIndex = GridPane.getColumnIndex(button);
                    Integer rowIndex = GridPane.getRowIndex(button);
                    int rowSym;
                    int colSym;

                    ToggleButton symmetricButton;
                    if ((rowIndex == 0 || rowIndex == ROW_CORNER) && colIndex != 0 && colIndex != COLUMNS_CORNER) {
                        rowSym = Math.abs(rowIndex - ROWS) - 1;
                        symmetricButton = (ToggleButton) grid.getChildren().get(rowSym * COLUMNS + colIndex);
                        symmetricButton.setSelected(!symmetricButton.isSelected());
                    }

                    if ((colIndex == 0 || colIndex == COLUMNS_CORNER) && rowIndex != 0 && rowIndex != ROW_CORNER) {
                        colSym = Math.abs(colIndex - COLUMNS) - 1;
                        symmetricButton = (ToggleButton) grid.getChildren().get(rowIndex * COLUMNS + colSym);
                        symmetricButton.setSelected(!symmetricButton.isSelected());
                    }

                    if ((rowIndex == 0 || rowIndex == ROW_CORNER) && (colIndex == 0 || colIndex == COLUMNS_CORNER)) {

                        symmetricButton = (ToggleButton) grid.getChildren().get(0);
                        symmetricButton.setSelected(!symmetricButton.isSelected());
                        symmetricButton = (ToggleButton) grid.getChildren().get(COLUMNS_CORNER);
                        symmetricButton.setSelected(!symmetricButton.isSelected());
                        symmetricButton = (ToggleButton) grid.getChildren().get(ROW_CORNER * COLUMNS);
                        symmetricButton.setSelected(!symmetricButton.isSelected());
                        symmetricButton = (ToggleButton) grid.getChildren().get(ROW_CORNER * COLUMNS + COLUMNS_CORNER);
                        symmetricButton.setSelected(!symmetricButton.isSelected());
                        symmetricButton = (ToggleButton) grid.getChildren().get(rowIndex*COLUMNS+colIndex);
                        symmetricButton.setSelected(!symmetricButton.isSelected());

                    }
                });

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
