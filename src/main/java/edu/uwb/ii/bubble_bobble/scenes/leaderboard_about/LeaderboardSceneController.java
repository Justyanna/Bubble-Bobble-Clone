package edu.uwb.ii.bubble_bobble.scenes.leaderboard_about;

import edu.uwb.ii.bubble_bobble.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }

    public void initialize() {
        prepareTableView();
    }

    private TableView<LeaderBoardData> prepareTableView() {
        Collection<LeaderBoardData> list = getLeaderBoardData();
        ObservableList<LeaderBoardData> details = FXCollections.observableArrayList(list);

        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colScore.setCellValueFactory(data -> data.getValue().scoreProperty());
        colDate.setCellValueFactory(data -> data.getValue().dateProperty());

        tableView.setEditable(false);
        tableView.setItems(details);

        return tableView;
    }

    private Collection<LeaderBoardData> getLeaderBoardData() {
        Collection<LeaderBoardData> list = null;

        try {
            URL path = getClass().getClassLoader().getResource("leaderboard/leaderboard.txt");

            list = Files.readAllLines(Paths.get(path.toURI())).stream().map(line -> {
                String[] details = line.split(" ");
                return new LeaderBoardData(details[0], details[1], details[2]);
            }).collect(Collectors.toList());
        } catch (IOException | URISyntaxException e) {
            LOGGER.warning("Cannot load leaderboard");
        }

        assert list != null;
        return list.stream()
                .sorted(Comparator.comparingInt(LeaderBoardData::getScore).reversed())
                .collect(Collectors.toList())
                .subList(0, 31);
    }
}
