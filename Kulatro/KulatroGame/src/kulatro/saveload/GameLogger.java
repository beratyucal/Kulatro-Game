package kulatro.saveload;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameLogger {

    private static final String LOG_FILE = "log.txt";
    private static GameLogger instance;
    private BufferedWriter writer;

    // Creates a new GameLogger instance.
    private GameLogger() {
        try {
            writer = new BufferedWriter(new FileWriter(LOG_FILE, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Returns the instance.
    public static GameLogger getInstance() {
        if (instance == null) {
            instance = new GameLogger();
        }
        return instance;
    }

    // Writes a message to the game log.
    public void log(String message) {
        try {
            String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Logs the game start.
    public void logGameStart(String username, String deck, String difficulty) {
        log("GAME START - Player: " + username + ", Deck: " + deck + ", Difficulty: " + difficulty);
    }

    // Logs the round start.
    public void logRoundStart(int roundNumber, int targetScore) {
        log("ROUND " + roundNumber + " START - Target Score: " + targetScore);
    }

    // Logs the initial hand.
    public void logInitialHand(String hand) {
        log("INITIAL HAND: " + hand);
    }

    // Logs the discard.
    public void logDiscard(String cardDiscarded, int remainingDiscards) {
        log("DISCARD - Card: " + cardDiscarded + ", Remaining Discards: " + remainingDiscards);
    }

    // Logs the special card usage.
    public void logSpecialCardUsage(String cardName) {
        log("SPECIAL CARD USED: " + cardName);
    }

    // Logs the hand submission.
    public void appendHandToLogSubmission(String hand, int score) {
        log("HAND SUBMITTED: " + hand + " -> Score: " + score);
    }

    // Logs the round result.
    public void logRoundResult(int roundNumber, int score, int targetScore, boolean won) {
        log("ROUND " + roundNumber + " RESULT - Score: " + score + 
            ", Target: " + targetScore + ", " + (won ? "WON" : "LOST"));
    }

    // Logs the score change.
    public void logScoreChange(int oldScore, int newScore) {
        log("SCORE CHANGE: " + oldScore + " -> " + newScore);
    }

    // Logs the game completion.
    public void logGameCompletion(String username, int totalScore, boolean won) {
        log("GAME COMPLETE - Player: " + username + 
            ", Total Score: " + totalScore + ", " + (won ? "WIN" : "LOSS"));
    }

    // Closes logger resources.
    public void close() {
        try {
            if (writer != null) writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}