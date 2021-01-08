package edu.uwb.ii.bubble_bobble.scenes.editor;

import edu.uwb.ii.bubble_bobble.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
import java.util.logging.Logger;

public class EditorSceneController {

    static final String[] ids = {"Empty", "Wall", "Player", "Knight", "Ninja"};
    private static final Logger LOGGER = Logger.getLogger(EditorSceneController.class.getName());
    private static final int ROWS = 26;
    private static final int ROW_CORNER = ROWS - 1;
    private static final int COLUMNS = 32;
    private static final int COLUMNS_CORNER = COLUMNS - 1;
    private static final String MAPS_PATH = System.getProperty("user.home") + "/AppData/Local/Bubble Bobble Clone/maps";
    private static final int PLAYER_LIMIT = 1;
    private String currentSelected;
    @FXML
    VBox rightPanel;
    private Map map;
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
            toggleButton.setOnAction((event) -> handleIdToggleButtonClick(toggleButton, ids[finalI]));
            if (ids[i] == "Wall") {
                toggleButton.setSelected(true);
                currentSelected = ids[1];
            }
            rightPanel.getChildren().add(toggleButton);
            rightPanel.setSpacing(10);
        }
    }

    private void fillGridWithCells(Rectangle2D bounds) {
        grid = new GridPane();
        grid.setMaxWidth(0.8 * bounds.getWidth());
        grid.setMaxHeight(0.65 * bounds.getWidth());
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.setHgap(1);
        grid.setVgap(2);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                ToggleButton button = new ToggleButton();
                button.setPrefHeight(bounds.getHeight() / ROWS);
                button.setPrefWidth(bounds.getWidth() / COLUMNS);

                button.setOnMouseClicked(event -> {
                    handleToggleButtonClick(button);
                });

                button.getStyleClass().add("grid-button-wall");
                grid.add(button, c, r);
            }
        }

        boardWindow.getChildren().add(grid);
    }

    private void handleToggleButtonClick(ToggleButton button) {
        Integer colIndex = GridPane.getColumnIndex(button);
        Integer rowIndex = GridPane.getRowIndex(button);
        int rowSym;
        int colSym;

        ToggleButton symmetricButton;
        if ((rowIndex == 0 || rowIndex == ROW_CORNER) && colIndex != 0 && colIndex != COLUMNS_CORNER) {
            fillRowBorder(colIndex, rowIndex);
        }

        if ((colIndex == 0 || colIndex == COLUMNS_CORNER) && rowIndex != 0 && rowIndex != ROW_CORNER) {
            fillColumnBorder(colIndex, rowIndex);
        }

        if ((rowIndex == 0 || rowIndex == ROW_CORNER) && (colIndex == 0 || colIndex == COLUMNS_CORNER)) {

            fillBoardCorners(colIndex, rowIndex);
        }

        map.toggle(colIndex, rowIndex, currentSelected);
    }

    private void fillRowBorder(Integer colIndex, Integer rowIndex) {
        ToggleButton symmetricButton;
        int rowSym = Math.abs(rowIndex - ROWS) - 1;
        symmetricButton = (ToggleButton) grid.getChildren().get(rowSym * COLUMNS + colIndex);
        symmetricButton.setSelected(!symmetricButton.isSelected());
    }

    private void fillColumnBorder(Integer colIndex, Integer rowIndex) {
        int colSym;
        ToggleButton symmetricButton;
        colSym = Math.abs(colIndex - COLUMNS) - 1;
        symmetricButton = (ToggleButton) grid.getChildren().get(rowIndex * COLUMNS + colSym);
        symmetricButton.setSelected(!symmetricButton.isSelected());
    }

    private void fillBoardCorners(Integer colIndex, Integer rowIndex) {
        ToggleButton symmetricButton;
        symmetricButton = (ToggleButton) grid.getChildren().get(0);
        symmetricButton.setSelected(!symmetricButton.isSelected());
        symmetricButton = (ToggleButton) grid.getChildren().get(COLUMNS_CORNER);
        symmetricButton.setSelected(!symmetricButton.isSelected());
        symmetricButton = (ToggleButton) grid.getChildren().get(ROW_CORNER * COLUMNS);
        symmetricButton.setSelected(!symmetricButton.isSelected());
        symmetricButton = (ToggleButton) grid.getChildren().get(ROW_CORNER * COLUMNS + COLUMNS_CORNER);
        symmetricButton.setSelected(!symmetricButton.isSelected());
        symmetricButton = (ToggleButton) grid.getChildren().get(rowIndex * COLUMNS + colIndex);
        symmetricButton.setSelected(!symmetricButton.isSelected());
    }

    public void switchToOptions(ActionEvent actionEvent) throws IOException {
        App.setRoot("options");
    }

    @FXML
    void saveMap(ActionEvent actionEvent) {
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
    void importMap(ActionEvent actionEvent) {

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
            for (Cell[] cellRow : map.getBody()) {
                for (Cell cell : cellRow) {
                    if (cell.getId() != "Empty") {
                        ToggleButton button =
                                (ToggleButton) grid.getChildren().get(cell.getY() * COLUMNS + cell.getX());
                        button.setSelected(true);
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException | IllegalArgumentException e) {
            e.printStackTrace();
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
    void resetBoard(ActionEvent actionEvent) {
        grid.getChildren().forEach(cell -> ((ToggleButton) cell).setSelected(false));
    }

    private void handleIdToggleButtonClick(ToggleButton button, String id) {
        if (button.isSelected()) {
            currentSelected = id;
        }
    }
}
