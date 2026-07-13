package kulatro;

public class Round {
    private int roundNumber;
    private int targetScore;
    private int roundScore;
    private boolean hasRoundEnded;

    // Creates a new Round instance.
    public Round(int roundNumber, int targetScore) {
        this.roundNumber = roundNumber;
        this.targetScore = targetScore;
        this.roundScore = 0;
        this.hasRoundEnded = false;
    }

    // Submits the hand.
    public void scoreSubmittedHand(Hand hand, SpecialCard selectedSpecialCard) {
        roundScore = hand.scoreCards(selectedSpecialCard);
        hasRoundEnded = true;
    }

    // Checks whether the round was won.
    public boolean didReachTarget() {
        return roundScore >= targetScore;
    }

    // Returns the round number.
    public int getRoundNumber() {
        return roundNumber;
    }

    // Returns the target score.
    public int getTargetScore() {
        return targetScore;
    }

    // Returns the round score.
    public int getRoundScore() {
        return roundScore;
    }

    // Checks whether the round is completed.
    public boolean hasRoundEnded() {
        return hasRoundEnded;
    }
    // Updates the round score.
    public void setRoundScore(int score) {
        this.roundScore = score;
        if (score > 0) this.hasRoundEnded = true;
    }
    private int submittedCount = 4;

    // Updates the submitted count.
    public void rememberSubmittedCardCount(int count) {
        this.submittedCount = count;
    }

    // Returns the submitted count.
    public int getSubmittedCardCount() {
        return submittedCount;
    }
}
