package kulatro.element;

import kulatro.Card;
import kulatro.NumericCard;
import kulatro.Player;
import kulatro.SpecialCard;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.stream.Collectors;

public class ElectronBond extends SpecialCard {

    // Creates a virtual pair between two cards of different types.
    // These two cards count as a matching pair for scoring purposes.
    // Only marked as used if the player completes both selections.

    private String bondedType1 = null;
    private String bondedType2 = null;
    private boolean active = false;

    // Creates a new ElectronBond instance.
    public ElectronBond() {
        super("Electron Bond",
              "Create a pair between two different card types - they count as a matching pair for scoring");
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
            JOptionPane.showMessageDialog(null, "Not enough cards to bond!");
            return;
        }

        String[] options = numericCards.stream()
            .map(Card::toString)
            .toArray(String[]::new);

        String card1 = (String) JOptionPane.showInputDialog(
            null, "Select first card to bond:",
            "Electron Bond", JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]
        );
        if (card1 == null) return; // cancel - not used

        String[] options2 = numericCards.stream()
            .map(Card::toString)
            .filter(s -> !s.equals(card1))
            .toArray(String[]::new);

        if (options2.length == 0) return;

        String card2 = (String) JOptionPane.showInputDialog(
            null, "Select second card to bond:",
            "Electron Bond", JOptionPane.QUESTION_MESSAGE,
            null, options2, options2[0]
        );
        if (card2 == null) return; // cancel - not used

        for (NumericCard nc : numericCards) {
            if (nc.toString().equals(card1)) bondedType1 = nc.getType();
            if (nc.toString().equals(card2)) bondedType2 = nc.getType();
        }

        active = true;
        markAsUsed();
    }

    // Checks whether the effect is active.
    public boolean isActive() { return active; }
    // Returns the first bonded type.
    public String getBondedType1() { return bondedType1; }
    // Returns the second bonded type.
    public String getBondedType2() { return bondedType2; }

    // Resets the current state.
    @Override
    public void reset() {
        super.reset();
        active = false;
        bondedType1 = null;
        bondedType2 = null;
    }
}