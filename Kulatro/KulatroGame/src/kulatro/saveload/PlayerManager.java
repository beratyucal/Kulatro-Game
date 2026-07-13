package kulatro.saveload;

import kulatro.Player;
import kulatro.engine.Game;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlayerManager {

    private static final String PLAYERS_FILE = "players.txt";
    private static final String HISTORY_FILE = "game_history.txt";

    // Creates a new PlayerManager instance.
    public PlayerManager() {
        makeFileIfMissing(PLAYERS_FILE);
        makeFileIfMissing(HISTORY_FILE);
    }

    // Creates the file if it is missing.
    private void makeFileIfMissing(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Registers a new player.
    public boolean registerNewPlayer(String username, String password) throws IOException {
        if (usernameAlreadyExists(username)) return false;
        BufferedWriter writer = new BufferedWriter(new FileWriter(PLAYERS_FILE, true));
        writer.write(username + "," + password);
        writer.newLine();
        writer.close();
        return true;
    }

    // Logs in a player.
    public Player login(String username, String password) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(PLAYERS_FILE));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                reader.close();
                return new Player(username, password);
            }
        }
        reader.close();
        return null;
    }

    // Checks whether the username exists.
    public boolean usernameAlreadyExists(String username) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(PLAYERS_FILE));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 1 && parts[0].equals(username)) {
                reader.close();
                return true;
            }
        }
        reader.close();
        return false;
    }

    // Returns all usernames.
    public List<String> listAllUsernames() throws IOException {
        List<String> usernames = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(PLAYERS_FILE));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 1) {
                usernames.add(parts[0]);
            }
        }
        reader.close();
        return usernames;
    }

    // Saves the game result.
    public void writeGameSaveResult(Game game) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE, true));
        String status = game.isGameFinished() ? (game.hasPlayerWonGame() ? "WIN" : "LOST") : "ONGOING";
        writer.write(game.getPlayer().getUsername() + "," +
                game.getSessionName() + "," +
                game.getPlayer().currentTotalScore() + "," +
                status + "," +
                game.getDeckType() + "," +
                game.getDifficulty());
        writer.newLine();
        writer.close();
    }

    // Returns the leaderboard data.
    public List<String[]> readLeaderboardData() throws IOException {
        List<String[]> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                data.add(parts);
            }
        }
        reader.close();

        // Skora gore sirala
        Collections.sort(data, new Comparator<String[]>() {
            @Override
            public int compare(String[] first, String[] second) {
                return parseScore(second[2]) - parseScore(first[2]);
            }
        });
        return data;
    }

    // Converts score text to a number for leaderboard sorting.
    private int parseScore(String scoreText) {
        try {
            return Integer.parseInt(scoreText);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}