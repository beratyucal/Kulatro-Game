package kulatro.quantum;

import kulatro.Card;
import kulatro.NumericCard;
import kulatro.Player;
import kulatro.SpecialCard;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.stream.Collectors;

public class GluonBind extends SpecialCard {

    // Merges two selected cards into one card with their combined value (max 9).
    // The new card takes the type of the first selected card.
    // A new card is drawn from the deck to replace the removed card.
    // Only marked as used if the player completes both selections.

    // Creates a new GluonBind instance.
    public GluonBind() {
        super("Gluon Bind",
              "Merge two cards into one with combined numeric value (max 9), draw one card in its place");
    }

    // Applies the effect.
    @Override
    public void applyEffect(Player player) {
        List<Card> hand = player.getHand();
        List<NumericCard> numericCards = hand.stream()
            .filter(c -> c instanceof NumericCard)
            .map(c -> (NumericCard) c)
            .collect(Collectors.toList());

        if (numericCards.size() < 2) {
            JOptionPane.showMessageDialog(null, "Not enough cards to merge!");
            return;
        }

        String[] options = numericCards.stream()
            .map(Card::toString)
            .toArray(String[]::new);

        String card1Str = (String) JOptionPane.showInputDialog(
            null, "Select first card to merge:",
            "Gluon Bind", JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]
        );
        if (card1Str == null) return; // cancel - not used

        String[] options2 = numericCards.stream()
            .map(Card::toString)
            .filter(s -> !s.equals(card1Str))
            .toArray(String[]::new);

        if (options2.length == 0) return;

        String card2Str = (String) JOptionPane.showInputDialog(
            null, "Select second card to merge:",
            "Gluon Bind", JOptionPane.QUESTION_MESSAGE,
            null, options2, options2[0]
        );
        if (card2Str == null) return; // cancel - not used

        NumericCard card1 = null, card2 = null;
        for (NumericCard nc : numericCards) {
            if (nc.toString().equals(card1Str) && card1 == null) card1 = nc;
            else if (nc.toString().equals(card2Str) && card2 == null) card2 = nc;
        }

        if (card1 == null || card2 == null) return;

        int mergedValue = Math.min(9, card1.getValue() + card2.getValue());
        String newType = card1.getType();

        hand.remove(card1);
        hand.remove(card2);
        hand.add(new NumericCard(newType, mergedValue));
        player.drawOneCard();

        markAsUsed();
    }
}