package kulatro;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String username;
    private String password;
    private AbstractDeck deck;
    private SpecialCard selectedSpecialCard;
    private List<Card> hand;
    private List<Card> discardPile;
    private int remainingDiscards;
    private int roundDiscards;
    private static final int MAX_ROUND_DISCARDS = 4;
    private int totalScore;

    // Creates a new Player instance.
    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.hand = new ArrayList<>();
        this.discardPile = new ArrayList<>();
        this.remainingDiscards = 6;
        this.roundDiscards = 0;
        this.totalScore = 0;
    }

    // Updates the deck.
    public void setDeck(AbstractDeck deck) {
        this.deck = deck;
    }

    // Returns the deck.
    public AbstractDeck getDeck() {
        return deck;
    }

    // Updates the selected special card.
    public void chooseSpecialCard(SpecialCard card) {
        this.selectedSpecialCard = card;
    }

    // Returns the selected special card.
    public SpecialCard getChosenSpecialCard() {
        return selectedSpecialCard;
    }

    // Draws one card into the hand.
    public void drawOneCard() {
        Card drawn = deck.draw();
        if (drawn != null) {
            hand.add(drawn);
        }
    }

    // Draws multiple cards into the hand.
    public void drawSomeCards(int count) {
        int drawn = 0;
        while (drawn < count) {
            Card card = deck.draw();
            if (card == null) break;
            hand.add(card);
            drawn++;
        }
    }

    // Draws the starting hand.
    public void dealOpeningHand() {
        int numericCount = 0;
        while (numericCount < 4) {
            Card drawn = deck.draw();
            if (drawn == null) break;
            if (drawn instanceof NumericCard) {
                hand.add(drawn);
                numericCount++;
            }
        }
    }

    // Returns the hand.
    public List<Card> getHand() {
        return hand;
    }

    // Returns the discard pile.
    public List<Card> getDiscardPile() {
        return discardPile;
    }

    // Clears the hand.
    public void emptyHand() {
        hand.clear();
    }

    // Resets the round discards.
    public void resetRoundDiscardCount() {
        roundDiscards = 0;
    }

    // Checks whether the player can discard.
    public boolean hasDiscardAvailable() {
        return remainingDiscards > 0 && roundDiscards < MAX_ROUND_DISCARDS;
    }

    // Discards the selected card.
    public boolean discardFromHand(Card card) {
        if (card instanceof SpecialCard) return false;
        if (!hasDiscardAvailable()) return false;
        if (!hand.contains(card)) return false;

        hand.remove(card);
        discardPile.add(card);
        remainingDiscards--;
        roundDiscards++;
        drawOneCard();
        return true;
    }

    // Returns the remaining discards.
    public int getRemainingDiscards() {
        return remainingDiscards;
    }

    // Returns the round discards.
    public int getRoundDiscards() {
        return roundDiscards;
    }

    // Returns the max round discards.
    public int getMaxRoundDiscards() {
        return MAX_ROUND_DISCARDS;
    }

    // Adds the score.
    public void addRoundScore(int score) {
        totalScore += score;
    }

    // Returns the total score.
    public int currentTotalScore() {
        return totalScore;
    }

    // Updates the total score.
    public void restoreTotalScore(int score) {
        this.totalScore = score;
    }

    // Updates the remaining discards.
    public void restoreRemainingDiscards(int discards) {
        this.remainingDiscards = discards;
    }

    // Updates the round discards.
    public void restoreRoundDiscards(int discards) {
        this.roundDiscards = discards;
    }

    // Returns the username.
    public String getUsername() {
        return username;
    }

    // Checks whether the password matches.
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}