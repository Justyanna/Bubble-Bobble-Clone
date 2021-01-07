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
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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

        loadLevel("map_01");

        map = new boolean[cols][rows];
        walls = new ArrayList<>();

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                if(i == 0 || i == rows - 1 || j == 0 || j == cols - 1) {
                    if(j == 14 || j == 15 || j == 16 || j == 17) continue;
                    buildWall(j, i);
                }
            }
        }

        for(int i = 5; i < 24; i += 5) {
            for(int j = 0; j < 5; j++) {
                buildWall(3 + j, i);
                buildWall(24 + j, i);
            }
        }

        for(int i = 10; i < 22; i++)
            buildWall(i, 12);

        enemies = new ArrayList<>();

        enemies.add(new Walker(15, 24));

        player = new Player(16, 13, App.getInputs());

    }

    private void buildWall(int x, int y) {

        map[x][y] = true;
        walls.add((Wall) Wall.tiny().spawn(x, y));

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

    private void loadLevel(String name) {

        try {
            URL url = GameSceneController.class.getClassLoader().getResource("maps/" + name + ".xml");
            System.out.println(url);
            File fXmlFile = new File(url.toString());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            System.out.println(doc.getDocumentElement().getNodeName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    Actions

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("menu");
    }

}