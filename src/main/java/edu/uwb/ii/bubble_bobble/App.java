package edu.uwb.ii.bubble_bobble;

import edu.uwb.ii.bubble_bobble.game.Inputs;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Bubble bobble clone
 */
public class App extends Application {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private static final String FONT_PATH = "fonts/Barcade Brawl.ttf";
    private static Scene scene;
    private static boolean in_game;
    private static Inputs inputs;

    public static Inputs getInputs() {
        return inputs;
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader =
                new FXMLLoader(edu.uwb.ii.bubble_bobble.App.class.getResource(fxml + "/" + fxml + ".fxml"));
        in_game = fxml.equals("game");
        return fxmlLoader.load();
    }

    private static void loadFont() {
        try {
            InputStream inputStream = App.class.getClassLoader().getResourceAsStream(FONT_PATH);
            Font.loadFont(inputStream, 20);
        } catch (NullPointerException e) {
            LOGGER.warning("Cannot find font file");
        }
    }

    public static void handleKeyDown(KeyEvent key) {

        if (!in_game) {
            return;
        }

        if (key.getCode() == KeyCode.LEFT) {
            inputs.left = true;
        } else if (key.getCode() == KeyCode.RIGHT) {
            inputs.right = true;
        } else if (key.getCode() == KeyCode.UP) {
            inputs.jump = true;
        } else if (key.getCode() == KeyCode.SPACE) {
            inputs.action = true;
        }

        key.consume();
    }

    public static void handleKeyUp(KeyEvent key) {

        if (!in_game) {
            return;
        }

        if (key.getCode() == KeyCode.LEFT) {
            inputs.left = false;
        } else if (key.getCode() == KeyCode.RIGHT) {
            inputs.right = false;
        } else if (key.getCode() == KeyCode.UP) {
            inputs.jump = false;
        } else if (key.getCode() == KeyCode.SPACE) {
            inputs.action = false;
        } else if (key.getCode() == KeyCode.ESCAPE) {
            inputs.pause = true;
        }

        key.consume();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {

        inputs = new Inputs();
        in_game = false;

        scene = new Scene(loadFXML("menu"));
        stage.setScene(scene);

        loadFont();

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        stage.show();

        scene.addEventHandler(KeyEvent.KEY_PRESSED, App::handleKeyDown);

        scene.addEventHandler(KeyEvent.KEY_RELEASED, App::handleKeyUp);
    }
}