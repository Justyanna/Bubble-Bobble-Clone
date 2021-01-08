package edu.uwb.ii.bubble_bobble.scenes.leaderboard;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.utils.LeaderboardFileManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

public class LeaderboardSceneController {

    private static final Logger LOGGER = Logger.getLogger(LeaderboardSceneController.class.getName());

    @FXML
    TableView<LeaderBoardData> tableView;
    @FXML
    TableColumn<LeaderBoardData, String> colName;
    @FXML
    TableColumn<LeaderBoardData, Number> colScore;
    @FXML
    TableColumn<LeaderBoardData, String> colDate;
    @FXML
    VBox vbox;
    LeaderboardFileManager leaderboardFileManager;

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }

    public void initialize() {
        leaderboardFileManager = new LeaderboardFileManager();
        prepareTableView();
        leaderboardFileManager.saveScore("Ania","5000", "10.10.2020");
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
