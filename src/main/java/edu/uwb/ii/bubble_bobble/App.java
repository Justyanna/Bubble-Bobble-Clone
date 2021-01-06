package edu.uwb.ii.bubble_bobble;

import edu.uwb.ii.bubble_bobble.utils.EncryptionProvider;
import edu.uwb.ii.bubble_bobble.utils.exceptions.EncryptionException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
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

    private final static Logger LOGGER = Logger.getLogger(App.class.getName());
    private static Scene scene;

    public static void main(String[] args) {
        launch();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static void loadFont() {
        try {
            InputStream inputStream = App.class.getClassLoader().getResourceAsStream("fonts/Barcade Brawl.ttf");
            Font.loadFont(inputStream, 20);
        } catch (NullPointerException e) {
            LOGGER.warning("Cannot find font file");
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(edu.uwb.ii.bubble_bobble.App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void start(Stage stage) throws IOException, EncryptionException {
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
    }
}