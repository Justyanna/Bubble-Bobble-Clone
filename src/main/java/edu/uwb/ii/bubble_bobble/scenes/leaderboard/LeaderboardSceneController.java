package edu.uwb.ii.bubble_bobble.scenes.leaderboard;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.utils.CurrentLanguageVersionProvider;
import edu.uwb.ii.bubble_bobble.utils.LeaderboardFileManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

public class LeaderboardSceneController {

    private static final Logger LOGGER = Logger.getLogger(LeaderboardSceneController.class.getName());
    private LeaderboardFileManager leaderboardFileManager;
    @FXML
    private TableView<LeaderBoardData> tableView;
    @FXML
    private TableColumn<LeaderBoardData, String> colName;
    @FXML
    private TableColumn<LeaderBoardData, Number> colScore;
    @FXML
    private TableColumn<LeaderBoardData, String> colDate;
    @FXML
    private VBox vbox;
    @FXML
    private Button goToMenu;

    public void initialize() {
        leaderboardFileManager = new LeaderboardFileManager();
        prepareTableView();
        loadLanguageVersion();
    }

    private void loadLanguageVersion() {
        Document doc = CurrentLanguageVersionProvider.loadXml();
        processXml(doc);
    }

    private void processXml(Document doc) {
        if (doc != null) {
            doc.getDocumentElement().normalize();
            Node menu = doc.getElementsByTagName("Leaderboard").item(0);

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

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }

    private TableView<LeaderBoardData> prepareTableView() {
        Collection<LeaderBoardData> list = leaderboardFileManager.getLeaderBoardData();

        if (list != null && (!list.isEmpty())) {
            ObservableList<LeaderBoardData> details = FXCollections.observableArrayList(list);

            colName.setCellValueFactory(data -> data.getValue().nameProperty());
            colScore.setCellValueFactory(data -> data.getValue().scoreProperty());
            colDate.setCellValueFactory(data -> data.getValue().dateProperty());

            tableView.setEditable(false);
            tableView.setItems(details);
        } else {
            vbox.getChildren().remove(tableView);
            Label label = new Label("Leaderboard is empty :( Come back here soon...");
            vbox.getChildren().add(0, label);
        }

        return tableView;
    }
}
