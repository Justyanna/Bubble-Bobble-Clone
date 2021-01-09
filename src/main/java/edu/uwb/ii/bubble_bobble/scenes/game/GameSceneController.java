package edu.uwb.ii.bubble_bobble.scenes.game;

import edu.uwb.ii.bubble_bobble.App;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

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
    private int cell_size;
    private Game _game;

    public void initialize() {

        gc = board.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        board.widthProperty().bind(gameWindow.widthProperty());
        board.heightProperty().bind(gameWindow.heightProperty());

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                while (now - last_update > INTERVAL) {
                    gc.clearRect(0, 0, board.getWidth(), board.getHeight());
                    _game.update(gc, cell_size);
                    last_update += INTERVAL;
                }
            }
        };

        //        -- waits for fxml elements to load properly
        Platform.runLater(() -> {

            board.requestFocus();
            cell_size = (int) (board.getHeight() / Map.ROWS);
            _game = new Game();
            gc.clearRect(0, 0, board.getWidth(), board.getHeight());
            _game.update(gc, cell_size);
            timer.start();
        });
    }

    //    Actions

    @FXML
    private void switchToPrimary() throws IOException {
        timer.stop();
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        App.setRoot("menu");
    }
}