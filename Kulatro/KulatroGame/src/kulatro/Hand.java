package kulatro;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<NumericCard> numericCards;
    private ScoreManager scoreManager;

    // Creates a new Hand instance.
    public Hand(List<Card> cards) {
        numericCards = new ArrayList<>();
        scoreManager = new ScoreManager();
        for (Card card : cards) {
            if (card instanceof NumericCard) {
                numericCards.add((NumericCard) card);
            }
        }
    }

    // Calculates the score.
    public int scoreCards(SpecialCard specialCard) {
        List<Card> cards = new ArrayList<>();
        cards.addAll(numericCards);
        return scoreManager.scoreCards(cards, specialCard);
    }

    // Returns the numeric cards.
    public List<NumericCard> numericCardsOnly() {
        return numericCards;
    }
}