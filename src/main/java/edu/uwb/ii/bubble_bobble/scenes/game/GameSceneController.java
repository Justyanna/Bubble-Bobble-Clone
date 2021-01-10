package edu.uwb.ii.bubble_bobble.scenes.game;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.scenes.leaderboard.LeaderBoardData;
import edu.uwb.ii.bubble_bobble.utils.CurrentLanguageVersionProvider;
import edu.uwb.ii.bubble_bobble.utils.LeaderboardFileManager;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

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
    private Button goToMenu;
    private Button saveScore;
    private Button menuAfterGame;
    private TextField name;
    private int current_i;
    private LeaderboardFileManager fileManager;
    private Game _game;

    public void initialize() {

        gc = board.getGraphicsContext2D();
        board.widthProperty().bind(gameWindow.widthProperty());
        board.heightProperty().bind(gameWindow.heightProperty());

        saveScore = new Button();
        menuAfterGame = new Button();
        fileManager = new LeaderboardFileManager();
        name = new TextField();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                while (now - last_update > INTERVAL) {
                    gc.clearRect(0, 0, board.getWidth(), board.getHeight());
                    _game.update(gc, cellSize());
                    last_update += INTERVAL;
                    if (_game.quit()) {
                        timer.stop();
                        App.get_inputs().clear();
                        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                        handleQuit();
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

    private void handleQuit() {
        if (!(gameWindow.getChildren().contains(saveScore) || gameWindow.getChildren().contains(menuAfterGame))) {

            int score = _game.get_score();
            addStyleClasses();
            addMenuAfterGameOnAction();
            DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Collection<LeaderBoardData> data = fileManager.getLeaderBoardData();
            saveScore.setOnAction(event -> handleSave(score, data != null ? -data.size() : 0));

            if (fileManager.isSavable(score, data)) {
                displayIfSavable(score, formatter, data);
            } else {

                displayIfNotSavable(score, formatter, data);
            }
        }
    }

    private void handleSave(int score, int offset) {
        name.getStyleClass().add("name-input");
        name.setText("Player 1");
        name.setMaxWidth(200);

        Button confirm = new Button("OK");
        confirm.getStyleClass().add("button-save");
        confirm.setOnAction(actionEvent -> {
            DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            String input = name.getText().replaceAll(" ", "_");

            fileManager.saveScore(input, String.valueOf(score), formatter.format(new Date()));
            try {
                switchToPrimary();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        if (!(gameWindow.getChildren().contains(name) || gameWindow.getChildren().contains(confirm))) {
            gameWindow.getChildren().add(name);
            gameWindow.getChildren().add(confirm);
        }
        gameWindow.setMargin(name, new Insets(offset * 70 + current_i * 100, 0, 0, 0));
        gameWindow.setMargin(confirm, new Insets(offset * 70 + current_i * 100, 0, 0, 260));
    }

    private void displayIfNotSavable(int score, DateFormat formatter, Collection<LeaderBoardData> data) {
        int i = 1;
        i = fillWithScoresGraterThanYour(score, data, i);
        int offset = data != null ? -data.size() : 0;
        displayYourScore(score, formatter, i, offset);
        current_i = i;
        gameWindow.getChildren().add(menuAfterGame);
        gameWindow.setMargin(menuAfterGame, new Insets(offset * 70 + (i + 1) * 100, 0, 0, 0));
    }

    private void displayIfSavable(int score, DateFormat formatter, Collection<LeaderBoardData> data) {
        int i = 1;

        i = fillWithScoresGraterThanYour(score, data, i);
        int offset = data != null ? -data.size() : 0;
        displayYourScore(score, formatter, i, offset);
        i = fillWithScoreLessThanYou(score, data, i);
        current_i = i;
        addButtons(i, offset);
    }

    private void addButtons(int i, int offset) {
        gameWindow.getChildren().add(saveScore);
        gameWindow.getChildren().add(menuAfterGame);
        gameWindow.setMargin(saveScore, new Insets(offset * 70 + (i + 1) * 100, 200, 0, 0));
        gameWindow.setMargin(menuAfterGame, new Insets(offset * 70 + (i + 1) * 100, 0, 0, 200));
    }

    private int fillWithScoreLessThanYou(int score, Collection<LeaderBoardData> data, int i) {
        if (data != null) {
            for (LeaderBoardData row : data) {
                if (row.getScore() <= score) {
                    i++;
                    Label rowLabel = new Label(i + ". " + row.getName() + " " + row.getScore() + " " + row.getDate());
                    gameWindow.getChildren().add(rowLabel);
                    rowLabel.getStyleClass().add("score-row");
                    gameWindow.setMargin(rowLabel, new Insets(-data.size() * 70 + (i - 1) * 100, 0, 0, 0));
                }
            }
        }
        return i;
    }

    private int fillWithScoresGraterThanYour(int score, Collection<LeaderBoardData> data, int i) {
        if (data != null) {
            for (LeaderBoardData row : data) {
                if (row.getScore() > score) {
                    Label rowLabel = new Label(i + ". " + row.getName() + " " + row.getScore() + " " + row.getDate());
                    gameWindow.getChildren().add(rowLabel);
                    rowLabel.getStyleClass().add("score-row");
                    gameWindow.setMargin(rowLabel, new Insets(-data.size() * 70 + (i - 1) * 100, 0, 0, 0));
                    i++;
                }
            }
        }
        return i;
    }

    private void displayYourScore(int score, DateFormat formatter, int i, int offset) {
        Label yourScore = new Label(i + ". " + "You" + " " + score + " " + formatter.format(new Date()));
        yourScore.getStyleClass().add("score-row-you");
        gameWindow.getChildren().add(yourScore);
        gameWindow.setMargin(yourScore, new Insets(offset * 70 + (i - 1) * 100, 0, 0, 0));
    }

    private void addMenuAfterGameOnAction() {
        menuAfterGame.setOnAction(actionEvent -> {
            try {
                switchToPrimary();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void addStyleClasses() {
        menuAfterGame.getStyleClass().add("button-menu");
        saveScore.getStyleClass().add("button-save");
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
                } else if ("menuAfterGame".equals(id)) {
                    menuAfterGame.setText(text);
                } else if ("saveScore".equals(id)) {
                    saveScore.setText(text);
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