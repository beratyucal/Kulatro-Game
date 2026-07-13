package kulatro.element;

import kulatro.Card;
import kulatro.NumericCard;
import kulatro.Player;
import kulatro.SpecialCard;
import javax.swing.JOptionPane;
import java.util.List;

public class NobleGas extends SpecialCard {

    // Locks one chosen card in place. The locked card cannot be discarded
    // but its value counts twice during scoring.
    // Only marked as used if the player selects a card to lock.

    private NumericCard lockedCard = null;

    // Creates a new NobleGas instance.
    public NobleGas() {
        super("Noble Gas",
              "Lock one card in place - it cannot be discarded but its value counts twice for scoring");
    }

    // Applies the effect.
    @Override
    public void applyEffect(Player player) {
        List<Card> hand = player.getHand();

        String[] options = hand.stream()
            .filter(c -> c instanceof NumericCard)
            .map(Card::toString)
            .toArray(String[]::new);

        if (options.length == 0) return;

        String chosen = (String) JOptionPane.showInputDialog(
            null, "Select a card to lock:",
            "Noble Gas", JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]
        );

        if (chosen == null) return; // cancel - not used

        for (Card card : hand) {
            if (card instanceof NumericCard && card.toString().equals(chosen)) {
                lockedCard = (NumericCard) card;
                lockedCard.setLocked(true);
                markAsUsed();
                break;
            }
        }
    }

    // Returns the locked card.
    public NumericCard getLockedCard() {
        return lockedCard;
    }

    // Resets the current state.
    @Override
    public void reset() {
        super.reset();
        // Locked card stays locked across rounds
    }
}