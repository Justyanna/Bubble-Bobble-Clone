package edu.uwb.ii.bubble_bobble.scenes.about_us;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.utils.CurrentLanguageVersionProvider;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;

public class AboutUsSceneContoller {

    public BorderPane root;
    public static final String PATH = "img/about/slajd";
    public static final int PAGES_NUMBER = 6;
    int pageAmount = PAGES_NUMBER;
    int itemsPerPage = 1;
    @FXML
    private Button goToMenu;
    @FXML
    private Pagination pagination;

    public VBox createPage(int pageIndex) {
        VBox box = new VBox(5);
        int page = pageIndex * itemsPerPage;
        for (int i = page; i < page + itemsPerPage; i++) {
            Image image = new Image(PATH + page + ".png");

            ImageView imageView = new ImageView(image);
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            imageView.setFitHeight(bounds.getHeight()*0.8);
            imageView.setFitWidth(bounds.getWidth()*0.8);
            box.getChildren().add(imageView);
        }
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private void loadLanguageVersion() {
        Document doc = CurrentLanguageVersionProvider.loadXml();
        processXml(doc);
    }

    private void processXml(Document doc) {
        if (doc != null) {
            doc.getDocumentElement().normalize();
            Node menu = doc.getElementsByTagName("AboutUs").item(0);

            for (int i = 0; i < menu.getChildNodes().getLength(); i++) {

                Node node = menu.getChildNodes().item(i);
                String id = node.getNodeName();
                var text = node.getTextContent();

                if (id == null || text == null) {
                    continue;
                }

                if ("goToMenu".equals(id)) {
                    goToMenu.setText(text);
                }
            }
        }
    }

    public void initialize() {
        pagination.setPageCount(pageAmount / itemsPerPage);
        pagination.setPageFactory(pageIndex -> createPage(pageIndex));
        loadLanguageVersion();
    }

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }
}
