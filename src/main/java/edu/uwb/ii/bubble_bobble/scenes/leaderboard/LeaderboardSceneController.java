package edu.uwb.ii.bubble_bobble.scenes.leaderboard;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.utils.EncryptionProvider;
import edu.uwb.ii.bubble_bobble.utils.exceptions.EncryptionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LeaderboardSceneController {

    private static final Logger LOGGER = Logger.getLogger(LeaderboardSceneController.class.getName());
    private static final String LEADERBOARD_PATH = "leaderboard/leaderboard.txt";
    private static final String ENCRYPTION_KEY = "Okon1234Okon1234";

    @FXML
    TableView<LeaderBoardData> tableView;
    @FXML
    TableColumn<LeaderBoardData, String> colName;
    @FXML
    TableColumn<LeaderBoardData, Number> colScore;
    @FXML
    TableColumn<LeaderBoardData, String> colDate;

    public static void saveScore(String name, String score, String date) {
        try {
            File file = new File(Objects.requireNonNull(
                    LeaderboardSceneController.class.getClassLoader().getResource(LEADERBOARD_PATH))
                                         .getFile());
            EncryptionProvider.decrypt(ENCRYPTION_KEY, file, file);

            try (FileWriter fw = new FileWriter(file, true); BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter pw = new PrintWriter(bw)) {

                pw.println(name + " " + score + " " + date);
                pw.flush();
            }

            EncryptionProvider.encrypt(ENCRYPTION_KEY, file, file);
        } catch (EncryptionException | IOException e) {
            LOGGER.warning("Cannot save score on leaderboard");
        }
    }

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

            File file = new File(getClass().getClassLoader().getResource(LEADERBOARD_PATH).getFile());
            EncryptionProvider.decrypt(ENCRYPTION_KEY, file, file);
            URL path = getClass().getClassLoader().getResource(LEADERBOARD_PATH);

            list = Files.readAllLines(Paths.get(path.toURI())).stream().map(line -> {
                String[] details = line.split(" ");
                return new LeaderBoardData(details[0], details[1], details[2]);
            }).collect(Collectors.toList());

            EncryptionProvider.encrypt(ENCRYPTION_KEY, file, file);
        } catch (IOException | URISyntaxException | EncryptionException e) {
            LOGGER.warning("Cannot load leaderboard");
        }

        assert list != null;
        if (list.size() < 31) {
            list = list.stream()
                    .sorted(Comparator.comparingInt(LeaderBoardData::getScore).reversed())
                    .collect(Collectors.toList())
                    .subList(0, list.size());
        } else {
            list = list.stream()
                    .sorted(Comparator.comparingInt(LeaderBoardData::getScore).reversed())
                    .collect(Collectors.toList())
                    .subList(0, 31);
        }
        return list;
    }
}
