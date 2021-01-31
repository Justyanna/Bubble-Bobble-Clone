package edu.uwb.ii.bubble_bobble.utils;

import edu.uwb.ii.bubble_bobble.scenes.leaderboard.LeaderBoardData;
import edu.uwb.ii.bubble_bobble.utils.exceptions.EncryptionException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LeaderboardFileManager {

    private static final Logger LOGGER = Logger.getLogger(LeaderboardFileManager.class.getName());
    private static final String ENCRYPTION_KEY = "Okon1234Okon1234";
    private final String appPath;
    private final String leaderboardPath;

    public LeaderboardFileManager() {
        appPath = System.getProperty("user.home") + "/AppData/Local/Bubble Bobble Clone";
        leaderboardPath = appPath + "/leaderboard.txt";
    }

    public boolean isSavable(int score, Collection<LeaderBoardData> data) {
        if (score > 0) {
            if (data != null) {
                if (data.size() < 20) {
                    return true;
                } else {

                    int min = Integer.MAX_VALUE;

                    for (LeaderBoardData row : data) {
                        if (row.getScore() < min) {
                            min = row.getScore();
                        }
                    }
                    return score > min;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public void saveScore(String name, String score, String date) {
        try {
            Files.createDirectories(Path.of(appPath));
            Files.createFile(Path.of(leaderboardPath));
        } catch (IOException e) {
           // LOGGER.info("Leaderboard directory already exist");
        }
        try {
            File file = new File(leaderboardPath);
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

    public Collection<LeaderBoardData> getLeaderBoardData() {
        Collection<LeaderBoardData> list = null;

        try {
            String[] lines = readFile();
            if (lines.length > 0) {
                list = createLeaderBoardDataCollection(lines);
            }
        } catch (IOException | NullPointerException | EncryptionException e) {
            LOGGER.warning("Cannot load leaderboard");
            return null;
        }

        list = takeSublist(list);
        return list;
    }

    private String[] readFile() throws IOException, EncryptionException {
        String contents;
        try {
            Files.createDirectories(Path.of(appPath));
            Files.createFile(Path.of(leaderboardPath));
        } catch (IOException e) {
//            LOGGER.info("Leaderboard directory already exist");
        }

        File file = new File(leaderboardPath);
        tryDecryptLeaderboardFile(file);

        try (InputStream inputStream = new FileInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            contents = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            throw e;
        }

        tryEncryptLeaderboardFile(file);
        return contents.split(System.lineSeparator());
    }

    private Collection<LeaderBoardData> createLeaderBoardDataCollection(String[] lines) {
        Collection<LeaderBoardData> list;
        list = Arrays.stream(lines).sequential().map(line -> {
            if (!line.isEmpty()) {
                String[] details = line.split(" ");
                return new LeaderBoardData(details[0].replaceAll("_", " "), details[1], details[2]);
            }

            throw new NullPointerException();
        }).collect(Collectors.toList());
        return list;
    }

    private Collection<LeaderBoardData> takeSublist(Collection<LeaderBoardData> list) {
        if (list != null) {
            if (list.size() < 10) {
                list = list.stream()
                        .sorted(Comparator.comparingInt(LeaderBoardData::getScore).reversed())
                        .collect(Collectors.toList())
                        .subList(0, list.size());
            } else {
                list = list.stream()
                        .sorted(Comparator.comparingInt(LeaderBoardData::getScore).reversed())
                        .collect(Collectors.toList())
                        .subList(0, 10);
            }
        }
        return list;
    }

    private void tryEncryptLeaderboardFile(File file) throws EncryptionException {
        try {
            EncryptionProvider.encrypt(ENCRYPTION_KEY, file, file);
        } catch (EncryptionException e) {
            LOGGER.info("Cannot decrypt file");
            throw e;
        }
    }

    private void tryDecryptLeaderboardFile(File file) throws EncryptionException {
        try {
            EncryptionProvider.decrypt(ENCRYPTION_KEY, file, file);
        } catch (EncryptionException e) {
            LOGGER.info("Cannot decrypt file");
            throw e;
        }
    }
}
