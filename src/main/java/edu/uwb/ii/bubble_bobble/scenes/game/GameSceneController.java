package edu.uwb.ii.bubble_bobble.scenes.game;

import edu.uwb.ii.bubble_bobble.App;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;

public class GameSceneController {

    @FXML public StackPane gameWindow;
    @FXML public Canvas board;
    public GraphicsContext gc;
    public AnimationTimer timer;

    @FXML public Label test;

    public void initialize() {

        gc = board.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        board.widthProperty().bind(gameWindow.widthProperty());
        board.heightProperty().bind(gameWindow.heightProperty());

        timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                gc.fillRect(0, 0, 100, 100);
            }

        };

        timer.start();

    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("menu");
    }

}