package edu.uwb.ii.bubble_bobble.scenes.game;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.entity.Player;
import edu.uwb.ii.bubble_bobble.game.entity.Wall;
import edu.uwb.ii.bubble_bobble.game.entity.enemy.Walker;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;

public class GameSceneController {

    //    -- utility
    private final static long INTERVAL = 1000000000L / 60;
    private static long last_update = System.nanoTime();

    @FXML
    BorderPane root;
    @FXML
    public StackPane gameWindow;
    @FXML
    public Canvas board;
    public GraphicsContext gc;
    public AnimationTimer timer;

    private int rows = 26;
    private int cols = 32;
    private int cell_size;

    boolean [][] map;

    private Player player;
    private ArrayList<Wall> walls;
    private ArrayList<Enemy> enemies;

    public void initialize() {

        gc = board.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        board.widthProperty().bind(gameWindow.widthProperty());
        board.heightProperty().bind(gameWindow.heightProperty());

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                while (now - last_update > INTERVAL) {
                    update();
                    last_update += INTERVAL;
                }
            }
        };

//        waits for fxml elements to load properly
        Platform.runLater(()-> {

            board.requestFocus();
            cell_size = (int) (board.getHeight() / rows);
            setupGame();
            update();
            timer.start();

        });

    }

    private void setupGame() {

        map = new boolean[cols][rows];
        walls = new ArrayList<>();

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                if(i == 0 || i == rows - 1 || j == 0 || j == cols - 1) {
                    if(j == 9 || j == 10 || j == 11) continue;
                    map[j][i] = true;
                    walls.add((Wall) Wall.tiny().spawn(j, i));
                }
            }
        }

        enemies = new ArrayList<>();

        enemies.add(new Walker(15, 24));

        player = new Player(16, 13, App.getInputs());

    }

    private void update() {

        gc.clearRect(0, 0, board.getWidth(), board.getHeight());

        for(Wall w : walls)
            w.draw(gc, cell_size);

        for(Enemy e : enemies)
            e.move(map);

        player.move(map);

        for(Enemy e : enemies)
            e.draw(gc, cell_size);

        player.draw(gc, cell_size);

    }

//    Actions

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("menu");
    }

}