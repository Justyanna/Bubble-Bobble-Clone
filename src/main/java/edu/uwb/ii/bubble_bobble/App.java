package edu.uwb.ii.bubble_bobble;

import edu.uwb.ii.bubble_bobble.game.Inputs;
import edu.uwb.ii.bubble_bobble.game.rendering.SpriteSheet;
import edu.uwb.ii.bubble_bobble.utils.CurrentLanguageVersionProvider;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Bubble bobble clone
 */
public class App extends Application {

    public static final String OPTIONS_FILE_PATH =
            System.getProperty("user.home") + "/AppData/Local/Bubble Bobble Clone/config.txt";
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private static final String FONT_PATH = "fonts/Barcade Brawl.ttf";
    public static String customMapName = new String();
    private static Stage _primary_stage;
    private static Scene _active_scene;
    private static boolean _in_game;
    private static Inputs _inputs;

    public static Stage get_primary_stage() {
        return _primary_stage;
    }

    public static Inputs get_inputs() {
        return _inputs;
    }

    public static void setRoot(String fxml) throws IOException {
        _active_scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader =
                new FXMLLoader(edu.uwb.ii.bubble_bobble.App.class.getResource(fxml + "/" + fxml + ".fxml"));
        _in_game = fxml.equals("game");
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

        if (!_in_game) {
            return;
        }

        if (key.getCode() == KeyCode.LEFT) {
            _inputs.left = true;
        } else if (key.getCode() == KeyCode.RIGHT) {
            _inputs.right = true;
        } else if (key.getCode() == KeyCode.UP) {
            _inputs.jump = true;
        } else if (key.getCode() == KeyCode.SPACE) {
            _inputs.action = true;
        }

        key.consume();
    }

    public static void handleKeyUp(KeyEvent key) {

        if (!_in_game) {
            return;
        }

        if (key.getCode() == KeyCode.LEFT) {
            _inputs.left = false;
        } else if (key.getCode() == KeyCode.RIGHT) {
            _inputs.right = false;
        } else if (key.getCode() == KeyCode.UP) {
            _inputs.jump = false;
        } else if (key.getCode() == KeyCode.SPACE) {
            _inputs.action = false;
        } else if (key.getCode() == KeyCode.ESCAPE) {
            _inputs.pause = true;
        }

        key.consume();
    }

    public static void main(String[] args) {
        launch();
    }

    private void loadOptions() {

        try (BufferedReader br = new BufferedReader(new FileReader(OPTIONS_FILE_PATH))) {

            String st;
            while ((st = br.readLine()) != null) {
                String[] temp = st.split(" ");
                if (temp[0].equals("language:")) {
                    String language;
                    if (temp[1].equals("pl") || temp[1].equals("eng") || temp[1].equals("fr")) {
                        language = "language_versions/" + temp[1] + ".xml";
                        CurrentLanguageVersionProvider.currentLanguageVersion = language;
                    } else {
                        throw new UnsupportedOperationException("Bad config file structure");
                    }
                }
                if (temp[0].equals("vfx:")) {
                    String graphicVersion;
                    if (temp[1].equals("beta") || temp[1].equals("legacy")) {
                        graphicVersion = "file:src/main/resources/img/" + temp[1] + "/";
                        SpriteSheet.imgPath = graphicVersion;
                    } else {
                        throw new UnsupportedOperationException("Bad config file structure");
                    }
                }
            }
        } catch (IOException | UnsupportedOperationException e) {
            LOGGER.warning("Cant load options: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) throws IOException {

        loadOptions();
        _inputs = new Inputs();
        _in_game = false;

        _primary_stage = stage;
        _active_scene = new Scene(loadFXML("menu"));
        stage.setScene(_active_scene);

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

        _active_scene.addEventHandler(KeyEvent.KEY_PRESSED, App::handleKeyDown);

        _active_scene.addEventHandler(KeyEvent.KEY_RELEASED, App::handleKeyUp);
    }
}