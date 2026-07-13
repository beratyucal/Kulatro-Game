package kulatro.alchemy;

import kulatro.Card;
import kulatro.Player;
import kulatro.SpecialCard;
import javax.swing.JOptionPane;
import java.util.List;

public class Transmutation extends SpecialCard {

    // Swaps one card chosen by the player with a random card from the deck.
    // Does not count toward the discard limit.
    // Only marked as used if the player completes the swap.

    // Creates a new Transmutation instance.
    public Transmutation() {
        super("Transmutation",
              "Swap one card in hand with a random card from deck (doesn't count toward discard limit)");
    }

    // Applies the effect.
    @Override
    public void applyEffect(Player player) {
        List<Card> hand = player.getHand();
        if (hand.isEmpty()) return;

        String[] options = hand.stream()
            .map(Card::toString)
            .toArray(String[]::new);

        String chosen = (String) JOptionPane.showInputDialog(
            null, "Select a card to swap:",
            "Transmutation", JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]
        );

        if (chosen == null) return; // cancel - not used

        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).toString().equals(chosen)) {
                hand.remove(i);
                player.drawOneCard();
                markAsUsed();
                break;
            }
        }
    }
}