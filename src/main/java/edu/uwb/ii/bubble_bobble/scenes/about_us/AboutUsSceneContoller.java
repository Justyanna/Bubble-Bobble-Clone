package edu.uwb.ii.bubble_bobble.scenes.about_us;

import edu.uwb.ii.bubble_bobble.App;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AboutUsSceneContoller {
    public BorderPane root;
    @FXML
    private Pagination pagination;
    String texts[] = {"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been " +
            "the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and " +
            "scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into " +
            "electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release " +
            "of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like " +
            "Aldus PageMaker including versions of Lorem Ipsum.",
            "t is a long established fact that a reader will be distracted by the readable content of a page when " +
                    "looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal " +
                    "distribution of letters, as opposed to using 'Content here, content here', making it look like " +
                    "readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their " +
                    "default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy." +
                    " Various versions have evolved over the years, sometimes by accident, sometimes on purpose " +
                    "(injected humour and the like)."};
    int pageAmount = texts.length;
    int itemsPerPage = 1;

    public VBox createPage(int pageIndex) {
        VBox box = new VBox(5);
        int page = pageIndex * itemsPerPage;
        for (int i = page; i < page + itemsPerPage; i++) {
            TextArea textArea = new TextArea(texts[i]);
            box.getChildren().add(textArea);
            textArea.setWrapText(true);
            textArea.setDisable(true);
            textArea.setEditable(false);
            textArea.setPrefWidth(1000);
            textArea.setPrefHeight(800);
        }
        box.setAlignment(Pos.CENTER);
        return box;
    }


    public void initialize() {
        pagination.setPageCount(pageAmount / itemsPerPage);
        pagination.setPageFactory(pageIndex -> createPage(pageIndex));

    }

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }
}
