package kulatro.engine;

import kulatro.Hand;
import kulatro.Player;
import kulatro.Round;
import kulatro.SpecialCard;
import kulatro.Card;
import java.util.List;
import java.util.List;
import kulatro.Card;
import kulatro.alchemy.PhilosophersStone;
import kulatro.element.IsotopeDecay;

public class Game {
    private Player player;
    private Round[] rounds;
    private int currentRoundIndex;
    private int[] targetScores;
    private boolean isGameFinished;
    private String sessionName;
    private String difficulty;
    private String deckType;

    // Creates a new Game instance.
    public Game(Player player, String sessionName, String difficulty, String deckType) {
        this.player = player;
        this.sessionName = sessionName;
        this.difficulty = difficulty;
        this.deckType = deckType;
        this.rounds = new Round[4];
        this.currentRoundIndex = 0;
        this.isGameFinished = false;

        pickTargetsForDifficulty(difficulty);

        for (int i = 0; i < 4; i++) {
            rounds[i] = new Round(i + 1, targetScores[i]);
        }
    }

    // Updates the target scores.
    private void pickTargetsForDifficulty(String difficulty) {
        switch (difficulty) {
            case "Easy":
                targetScores = new int[]{40, 55, 70, 85};
                break;
            case "Hard":
                targetScores = new int[]{60, 75, 90, 105};
                break;
            default:
                targetScores = new int[]{50, 65, 80, 95};
                break;
        }
    }

    // Starts the round.
    public void beginRound() {
        player.emptyHand();
        player.resetRoundDiscardCount();
        player.drawSomeCards(4); // her zaman 4 kart

        // Kalici special cardlari her round resetle
        SpecialCard sc = player.getChosenSpecialCard();
        if (sc != null && !isSingleUseSpecial(sc)) {
            sc.reset();
        }
    }

    // Checks whether the special card is one-time use.
    private boolean isSingleUseSpecial(SpecialCard sc) {
        return sc instanceof PhilosophersStone ||
               sc instanceof IsotopeDecay;
    }

    // Secili kartlarla submit
    public int scoreSubmittedHand(List<Card> selectedCards) {
        if (selectedCards == null || selectedCards.isEmpty()) return 0;
        if (selectedCards.size() > 4) return 0;

        int submittedCount = selectedCards.size();

        Hand hand = new Hand(selectedCards);
        Round current = getCurrentRound();
        current.scoreSubmittedHand(hand, player.getChosenSpecialCard());
        player.addRoundScore(current.getRoundScore());

        // Submit edilen kart sayisini kaydet
        current.rememberSubmittedCardCount(submittedCount);

        currentRoundIndex++;
        if (currentRoundIndex >= 4) {
            isGameFinished = true;
        }

        return current.getRoundScore();
    }

   

    // Checks whether the player has won.
    public boolean hasPlayerWonGame() {
        if (!isGameFinished) return false;

        double thresholdAverage = 0;
        double playerAverage = 0;

        for (int i = 0; i < 4; i++) {
            thresholdAverage += targetScores[i];
            playerAverage += rounds[i].getRoundScore();
        }

        thresholdAverage /= 4;
        playerAverage /= 4;

        return playerAverage >= thresholdAverage;
    }

    // Returns the player average.
    public double calculatePlayerAverage() {
        double total = 0;
        for (Round r : rounds) {
            total += r.getRoundScore();
        }
        return total / 4;
    }

    // Returns the threshold average.
    public double calculateTargetAverage() {
        double total = 0;
        for (int t : targetScores) {
            total += t;
        }
        return total / 4;
    }

    // Returns the rounds won.
    public int countWonRounds() {
        int won = 0;
        for (Round r : rounds) {
            if (r.hasRoundEnded() && r.didReachTarget()) {
                won++;
            }
        }
        return won;
    }

    // Returns the current round.
    public Round getCurrentRound() {
        if (currentRoundIndex < 4) {
            return rounds[currentRoundIndex];
        }
        return null;
    }

    // Returns the current round index.
    public int getCurrentRoundIndex() {
        return currentRoundIndex;
    }

    // Updates the current round index.
    public void setCurrentRoundIndex(int index) {
        this.currentRoundIndex = index;
    }

    // Returns the player.
    public Player getPlayer() {
        return player;
    }

    // Returns the rounds.
    public Round[] getRounds() {
        return rounds;
    }

    // Checks whether the game has finished.
    public boolean isGameFinished() {
        return isGameFinished;
    }

    // Returns the session name.
    public String getSessionName() {
        return sessionName;
    }

    // Returns the difficulty.
    public String getDifficulty() {
        return difficulty;
    }

    // Returns the deck type.
    public String getDeckType() {
        return deckType;
    }

    // Returns the target scores.
    public int[] getTargetScores() {
        return targetScores;
    }
    
}