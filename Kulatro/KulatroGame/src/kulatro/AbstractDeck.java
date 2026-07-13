package kulatro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractDeck {
    protected List<Card> cards;

    // Creates a new AbstractDeck instance.
    public AbstractDeck() {
        cards = new ArrayList<>();
        buildNumberCards();
        buildSpecialCards();
        Collections.shuffle(cards);
    }

    protected abstract void buildNumberCards();
    protected abstract void buildSpecialCards();

    // Draws the next card from the deck.
    public Card draw() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }

    // Returns the cards.
    public List<Card> getCards() {
        return cards;
    }

    // Returns the number of remaining cards.
    public int countCardsLeft() {
        return cards.size();
    }

    // Shuffles the deck.
    public void shuffle() {
        Collections.shuffle(cards);
    }
}