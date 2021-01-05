package edu.uwb.ii.bubble_bobble.scenes.game;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.rendering.Animation;
import edu.uwb.ii.bubble_bobble.rendering.SpriteSheet;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;

public class GameSceneController {

    @FXML public StackPane gameWindow;
    @FXML public Canvas board;
    public GraphicsContext gc;
    public AnimationTimer timer;

    private SpriteSheet placeholder;
    private Animation testAnimation;

    public void initialize() {

        gc = board.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        board.widthProperty().bind(gameWindow.widthProperty());
        board.heightProperty().bind(gameWindow.heightProperty());

        loadResources();

        timer = new AnimationTimer() {
            @Override public void handle(long now) {
                update();
            }
        };

        timer.start();

    }

    private void loadResources() {

        placeholder = new SpriteSheet("placeholder", 12, 12);
        testAnimation = new Animation(0, 8, 8);

    }

    private void update() {

        placeholder.draw(gc, testAnimation.next(), 0, 0, 80, 80);

    }

//    Actions

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("menu");
    }

}