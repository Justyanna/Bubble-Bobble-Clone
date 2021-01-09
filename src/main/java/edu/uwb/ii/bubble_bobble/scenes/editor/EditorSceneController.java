package edu.uwb.ii.bubble_bobble.scenes.editor;

import edu.uwb.ii.bubble_bobble.App;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.logging.Logger;

public class EditorSceneController {

    static final String[] ids = {"empty", "Wall", "Player", "Knight", "Ninja"};
    private static final Logger LOGGER = Logger.getLogger(EditorSceneController.class.getName());
    private static final int ROWS = 26;
    private static final int ROW_CORNER = ROWS - 1;
    private static final int COLUMNS = 32;
    private static final int COLUMNS_CORNER = COLUMNS - 1;
    private static final String MAPS_PATH = System.getProperty("user.home") + "/AppData/Local/Bubble Bobble Clone/maps";
    @FXML
    ToggleButton threeGapFrame;
    private String currentSelected;
    private Map map;
    @FXML
    private VBox rightPanel;
    @FXML
    private ToggleButton fullFrame;
    @FXML
    private ToggleButton oneGapFrame;
    @FXML
    private ToggleButton twoGapFrame;
    @FXML
    private StackPane boardWindow;
    @FXML
    private GridPane grid;
    @FXML
    private ToggleGroup modes;

    public void initialize() {
        map = new Map();
        currentSelected = ids[0];
        createRightPanel();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        fillGridWithCells(bounds);

        modes.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null) {
                oldVal.setSelected(true);
            }
        });
    }

    private void createRightPanel() {
        for (int i = 1; i < ids.length; i++) {

            ToggleButton toggleButton = new ToggleButton(ids[i]);
            toggleButton.setToggleGroup(modes);
            toggleButton.setPrefHeight(30);
            toggleButton.setPrefWidth(100);
            int finalI = i;
            toggleButton.setOnAction(event -> handleIdToggleButtonClick(toggleButton, ids[finalI]));
            if (i == 1) {
                toggleButton.setSelected(true);
                currentSelected = ids[1];
            }

            rightPanel.getChildren().add(toggleButton);
            rightPanel.setSpacing(10);
        }
    }

    private void fillGridWithCells(Rectangle2D bounds) {
        grid = new GridPane();
        grid.prefWidthProperty().bind(boardWindow.widthProperty());
        grid.prefHeightProperty().bind(boardWindow.heightProperty());


        Platform.runLater(() -> {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLUMNS; c++) {
                    ToggleButton button = new ToggleButton();
                    button.setPrefHeight((grid.prefHeightProperty().get()) / ROWS);
                    button.setPrefWidth((grid.prefWidthProperty().get() ) / COLUMNS);
                    button.setOnMouseClicked(event -> handleToggleButtonClick(button));
                    button.getStyleClass().add("grid-button-empty");

                    grid.add(button, c, r);
                }
            }
        });

        boardWindow.getChildren().add(grid);
    }

    private void handleToggleButtonClick(ToggleButton button) {
        Integer colIndex = GridPane.getColumnIndex(button);
        Integer rowIndex = GridPane.getRowIndex(button);
        if (((rowIndex == 0 || rowIndex == ROW_CORNER) || (colIndex == 0 || colIndex == COLUMNS_CORNER))) {
            handleWallCell(button, colIndex, rowIndex);
        } else {
            if (Arrays.stream(map.getBody())
                    .allMatch(row -> Arrays.stream(row).noneMatch(x -> x.getId().equalsIgnoreCase(ids[2])))) {
                toggleAndSetClass(button, colIndex, rowIndex);
            } else {
                if (currentSelected == ids[2] &&
                        button.getStyleClass().stream().anyMatch(x -> x.equals("grid-button-player-B")) ||
                        button.getStyleClass().stream().anyMatch(x -> x.equals("grid-button-player-A"))) {
                    toggleAndSetClass(button, colIndex, rowIndex);
                } else {
                    toggleAndSetClass(button, colIndex, rowIndex);
                }
            }
        }
    }

    private void handleWallCell(ToggleButton button, Integer colIndex, Integer rowIndex) {
        String cell;
        if (currentSelected.equals(ids[1])) {
            cell = toggleAndSetClass(button, colIndex, rowIndex);
            if (ids[1].equalsIgnoreCase(cell) || ids[0].equalsIgnoreCase(cell)) {

                if ((rowIndex == 0 || rowIndex == ROW_CORNER) && colIndex != 0 && colIndex != COLUMNS_CORNER) {
                    fillRowBorder(colIndex, rowIndex);
                }

                if ((colIndex == 0 || colIndex == COLUMNS_CORNER) && rowIndex != 0 && rowIndex != ROW_CORNER) {
                    fillColumnBorder(colIndex, rowIndex);
                }

                if ((rowIndex == 0 || rowIndex == ROW_CORNER) && (colIndex == 0 || colIndex == COLUMNS_CORNER)) {

                    fillBoardCorners(colIndex, rowIndex);
                }
            }
        }
    }

    private String toggleAndSetClass(ToggleButton button, Integer colIndex, Integer rowIndex) {
        return toggleAndSetClass(button, colIndex, rowIndex, currentSelected);
    }

    private String toggleAndSetClass(ToggleButton button, Integer colIndex, Integer rowIndex, String cellClass) {
        String cell = map.toggle(colIndex, rowIndex, cellClass);
        button.getStyleClass().clear();
        button.getStyleClass().add("grid-button-" + cell);
        return cell;
    }

    private void fillRowBorder(Integer colIndex, Integer rowIndex) {
        int rowSym = Math.abs(rowIndex - ROWS) - 1;
        ToggleButton symmetricButton = (ToggleButton) grid.getChildren().get(rowSym * COLUMNS + colIndex);
        toggleAndSetClass(symmetricButton, colIndex, rowSym);
    }

    private void fillColumnBorder(Integer colIndex, Integer rowIndex) {
        int colSym = Math.abs(colIndex - COLUMNS) - 1;
        ToggleButton symmetricButton = (ToggleButton) grid.getChildren().get(rowIndex * COLUMNS + colSym);
        toggleAndSetClass(symmetricButton, colSym, rowIndex);
    }

    private void fillBoardCorners(Integer colIndex, Integer rowIndex) {
        ToggleButton symmetricButton;
        symmetricButton = (ToggleButton) grid.getChildren().get(0);
        toggleAndSetClass(symmetricButton, 0, 0);
        symmetricButton = (ToggleButton) grid.getChildren().get(COLUMNS_CORNER);
        toggleAndSetClass(symmetricButton, COLUMNS_CORNER, 0);
        symmetricButton = (ToggleButton) grid.getChildren().get(ROW_CORNER * COLUMNS);
        toggleAndSetClass(symmetricButton, 0, ROW_CORNER);
        symmetricButton = (ToggleButton) grid.getChildren().get(ROW_CORNER * COLUMNS + COLUMNS_CORNER);
        toggleAndSetClass(symmetricButton, COLUMNS_CORNER, ROW_CORNER);
        symmetricButton = (ToggleButton) grid.getChildren().get(rowIndex * COLUMNS + colIndex);
        toggleAndSetClass(symmetricButton, colIndex, rowIndex);
    }

    public void switchToOptions() throws IOException {
        App.setRoot("options");
    }

    @FXML
    void saveMap() {
        tryCreateMapsDirectory();

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("xml files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(MAPS_PATH));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            File file = fileChooser.showSaveDialog(boardWindow.getScene().getWindow());
            transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(map.generateFxml());
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(domSource, streamResult);
        } catch (ParserConfigurationException | TransformerException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void importMap() {
        tryCreateMapsDirectory();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("xml files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(MAPS_PATH));

        try {
            File file = fileChooser.showOpenDialog(boardWindow.getScene().getWindow());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            map.importFormDocument(doc);
            updateBoard();
        } catch (ParserConfigurationException | IOException | SAXException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void updateBoard() {
        for (Cell[] cellRow : map.getBody()) {
            for (Cell cell : cellRow) {
                if (!cell.getId().equalsIgnoreCase("empty")) {
                    ToggleButton button = (ToggleButton) grid.getChildren().get(cell.getY() * COLUMNS + cell.getX());
                    button.getStyleClass().clear();
                    button.getStyleClass()
                            .add("grid-button-" + (cell.getId().equals("Wall") || cell.getId().equals("empty") ?
                                    cell.getId().toLowerCase() :
                                    cell.getId().toLowerCase() + "-" + (cell.getFacing() > 0 ? "A" : "B")));
                }
            }
        }
    }

    private void tryCreateMapsDirectory() {
        try {
            Files.createDirectories(Path.of(MAPS_PATH));
        } catch (IOException e) {
            LOGGER.info("Maps directory already exist");
        }
    }

    @FXML
    void resetBoard() {
        map.resetMap();
        grid.getChildren().forEach(cell -> {
            cell.getStyleClass().clear();
            cell.getStyleClass().add("grid-button-empty");
        });
    }

    private void handleIdToggleButtonClick(ToggleButton button, String id) {
        if (button.isSelected()) {
            currentSelected = id;
        }
    }

    @FXML
    void addFullFrame() {

        grid.getChildren().forEach(cell -> {
            Integer colIndex = GridPane.getColumnIndex(cell);
            Integer rowIndex = GridPane.getRowIndex(cell);
            if (fullFrame.isSelected()) {
                if ((rowIndex == 0 || rowIndex == ROW_CORNER) || (colIndex == 0 || colIndex == COLUMNS_CORNER)) {
                    map.getBody()[colIndex][rowIndex].setId("Wall");
                    cell.getStyleClass().clear();
                    cell.getStyleClass().add("grid-button-wall");
                }
            } else {
                if ((rowIndex == 0 || rowIndex == ROW_CORNER) || (colIndex == 0 || colIndex == COLUMNS_CORNER)) {
                    map.getBody()[colIndex][rowIndex].setId("empty");
                    cell.getStyleClass().clear();
                    cell.getStyleClass().add("grid-button-empty");
                }
            }
        });
    }

    @FXML
    void addOneGapFrame() {

        grid.getChildren().forEach(cell -> {
            Integer colIndex = GridPane.getColumnIndex(cell);
            Integer rowIndex = GridPane.getRowIndex(cell);
            boolean b = (((rowIndex == 0 || rowIndex == ROW_CORNER) && colIndex != 15 && colIndex != 16 &&
                    colIndex != 17)) || (colIndex == 0 || colIndex == COLUMNS_CORNER);
            if (oneGapFrame.isSelected()) {
                if (b) {
                    map.getBody()[colIndex][rowIndex].setId("Wall");
                    cell.getStyleClass().clear();
                    cell.getStyleClass().add("grid-button-wall");
                } else if (((rowIndex == 0 || rowIndex == ROW_CORNER) && colIndex == 15) || colIndex == 16 ||
                        colIndex == 17) {
                    map.getBody()[colIndex][rowIndex].setId("empty");
                    cell.getStyleClass().clear();
                    cell.getStyleClass().add("grid-button-empty");
                }
            } else {
                if (b) {
                    map.getBody()[colIndex][rowIndex].setId("empty");
                    cell.getStyleClass().clear();
                    cell.getStyleClass().add("grid-button-empty");
                }
            }
        });
    }

    @FXML
    void addTwoGapFrame() {
        grid.getChildren().forEach(cell -> {
            Integer colIndex = GridPane.getColumnIndex(cell);
            Integer rowIndex = GridPane.getRowIndex(cell);
            boolean b = ((rowIndex == 0 || rowIndex == ROW_CORNER) && colIndex != 7 && colIndex != 8 && colIndex != 9 &&
                    colIndex != 23 && colIndex != 24 && colIndex != 25) ||
                    (colIndex == 0 || colIndex == COLUMNS_CORNER);
            if (twoGapFrame.isSelected()) {
                if (b) {
                    map.getBody()[colIndex][rowIndex].setId("Wall");
                    cell.getStyleClass().clear();
                    cell.getStyleClass().add("grid-button-wall");
                } else if (((rowIndex == 0 || rowIndex == ROW_CORNER) && colIndex == 7 || colIndex == 8 ||
                        colIndex == 9 || colIndex == 23 || colIndex == 24 || colIndex == 25)) {
                    map.getBody()[colIndex][rowIndex].setId("empty");
                    cell.getStyleClass().clear();
                    cell.getStyleClass().add("grid-button-empty");
                }
            } else {
                if (b) {
                    map.getBody()[colIndex][rowIndex].setId("empty");
                    cell.getStyleClass().clear();
                    cell.getStyleClass().add("grid-button-empty");
                }
            }
        });
    }

    @FXML
    void addThreeGapFrame(ActionEvent actionEvent) {
        grid.getChildren().forEach(cell -> {
            Integer colIndex = GridPane.getColumnIndex(cell);
            Integer rowIndex = GridPane.getRowIndex(cell);
            boolean b = ((rowIndex == 0 || rowIndex == ROW_CORNER) && colIndex != 1 && colIndex != 2 && colIndex != 3 &&
                    colIndex != 4 && colIndex != 27 && colIndex != 28 && colIndex != 29 && colIndex != 30) ||
                    (colIndex == 0 || colIndex == COLUMNS_CORNER);
            if (threeGapFrame.isSelected()) {
                if (b) {
                    map.getBody()[colIndex][rowIndex].setId("Wall");
                    cell.getStyleClass().clear();
                    cell.getStyleClass().add("grid-button-wall");
                } else if (((rowIndex == 0 || rowIndex == ROW_CORNER) && colIndex == 1 || colIndex == 2 ||
                        colIndex == 3 || colIndex == 4 || colIndex == 27 || colIndex == 28 || colIndex == 29 ||
                        colIndex == 30)) {
                    map.getBody()[colIndex][rowIndex].setId("empty");
                    cell.getStyleClass().clear();
                    cell.getStyleClass().add("grid-button-empty");
                }
            } else {
                if (b) {
                    map.getBody()[colIndex][rowIndex].setId("empty");
                    cell.getStyleClass().clear();
                    cell.getStyleClass().add("grid-button-empty");
                }
            }
        });
    }
}
