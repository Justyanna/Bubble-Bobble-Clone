package edu.uwb.ii.bubble_bobble;

import edu.uwb.ii.bubble_bobble.game.Inputs;
import edu.uwb.ii.bubble_bobble.scenes.game.GameSceneController;
import edu.uwb.ii.bubble_bobble.utils.EncryptionProvider;
import edu.uwb.ii.bubble_bobble.utils.exceptions.EncryptionException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Bubble bobble clone
 */
public class App extends Application {

    private static Scene scene;
    private static boolean in_game;
    private static Inputs inputs;

    public static Inputs getInputs() {
        return inputs;
    }

    @Override
    public void start(Stage stage) throws IOException, EncryptionException {

        inputs = new Inputs();
        in_game = false;

        scene = new Scene(loadFXML("menu"));
        stage.setScene(scene);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        stage.show();

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            handleKeyDown(key);
        });

        scene.addEventHandler(KeyEvent.KEY_RELEASED, (key) -> {
            handleKeyUp(key);
        });

    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(edu.uwb.ii.bubble_bobble.App.class.getResource(fxml + ".fxml"));
        in_game = fxml.equals("game");
        return fxmlLoader.load();
    }

    public static void handleKeyDown(KeyEvent key) {

        if (!in_game) return;

        if(key.getCode() == KeyCode.LEFT) inputs.left = true;
        else if(key.getCode() == KeyCode.RIGHT) inputs.right = true;
        else if(key.getCode() == KeyCode.UP) inputs.jump = true;
        else if(key.getCode() == KeyCode.SPACE) inputs.action = true;

        key.consume();

    }

    public static void handleKeyUp(KeyEvent key) {

        if (!in_game) return;

        if(key.getCode() == KeyCode.LEFT) inputs.left = false;
        else if(key.getCode() == KeyCode.RIGHT) inputs.right = false;
        else if(key.getCode() == KeyCode.UP) inputs.jump = false;
        else if(key.getCode() == KeyCode.SPACE) inputs.action = false;
        else if(key.getCode() == KeyCode.ESCAPE) inputs.pause = true;

        key.consume();

    }

    public static void main(String[] args) {
        launch();
    }

}