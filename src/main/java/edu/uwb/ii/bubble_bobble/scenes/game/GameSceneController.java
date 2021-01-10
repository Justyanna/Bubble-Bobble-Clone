package edu.uwb.ii.bubble_bobble.scenes.game;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.utils.CurrentLanguageVersionProvider;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;

public class GameSceneController {

    //    -- utility
    private final static long INTERVAL = 1000000000L / 60;
    private static long last_update = System.nanoTime();
    @FXML
    public StackPane gameWindow;
    @FXML
    public Canvas board;
    public GraphicsContext gc;
    public AnimationTimer timer;
    @FXML
    BorderPane root;
    @FXML
    Button goToMenu;
    private Game _game;

    public void initialize() {

        gc = board.getGraphicsContext2D();
        board.widthProperty().bind(gameWindow.widthProperty());
        board.heightProperty().bind(gameWindow.heightProperty());

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                while (now - last_update > INTERVAL) {
                    gc.clearRect(0, 0, board.getWidth(), board.getHeight());
                    _game.update(gc, cellSize());
                    last_update += INTERVAL;
                    if (_game.quit()) {
                        try {
                            switchToPrimary();
                        } catch (IOException e) {
                        }
                    }
                }

            }
        };

        //        -- waits for fxml elements to load properly
        Platform.runLater(() -> {

            board.requestFocus();

            _game = Game.getInstance();
            _game.start();
            _game.update(gc, cellSize());

            timer.start();

        });
        loadLanguageVersion();
    }

    public int cellSize() {
        return (int) board.getHeight() / Map.ROWS;
    }

    private void loadLanguageVersion() {
        Document doc = CurrentLanguageVersionProvider.loadXml();
        processXml(doc);
    }

    private void processXml(Document doc) {
        if (doc != null) {
            doc.getDocumentElement().normalize();
            Node menu = doc.getElementsByTagName("Game").item(0);

            for (int i = 0; i < menu.getChildNodes().getLength(); i++) {

                Node node = menu.getChildNodes().item(i);
                String id = node.getNodeName();
                var text = node.getTextContent();

                if (id == null || text == null) {
                    continue;
                }

                if ("goToMenu".equals(id)) {
                    goToMenu.setText(text);
                }
            }
        }
    }

    //    Actions

    @FXML
    private void switchToPrimary() throws IOException {

        timer.stop();
        App.get_inputs().clear();
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        App.setRoot("menu");

    }
}