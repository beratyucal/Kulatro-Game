package kulatro.quantum;

import kulatro.Card;
import kulatro.NumericCard;
import kulatro.Player;
import kulatro.SpecialCard;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class QuantumEntanglement extends SpecialCard {

    private NumericCard entangledCard = null;
    private boolean active = false;

    // Creates a new QuantumEntanglement instance.
    public QuantumEntanglement() {
        super("Quantum Entanglement",
              "Triple the score of one card from the current hand");
    }

    // Applies the effect.
    @Override
    public void applyEffect(Player player) {
        List<Card> hand = player.getHand();
        List<NumericCard> numericCards = new ArrayList<>();

        for (Card card : hand) {
            if (card instanceof NumericCard) {
                numericCards.add((NumericCard) card);
            }
        }

        if (numericCards.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No numeric cards in hand!");
            return;
        }

        String[] options = new String[numericCards.size()];

        for (int i = 0; i < numericCards.size(); i++) {
            options[i] = (i + 1) + ". " + numericCards.get(i).toString();
        }

        String chosen = (String) JOptionPane.showInputDialog(
                null,
                "Select a card to triple its score:",
                "Quantum Entanglement",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (chosen == null) {
            return;
        }

        int selectedIndex = -1;

        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(chosen)) {
                selectedIndex = i;
                break;
            }
        }

        if (selectedIndex == -1) {
            return;
        }

        entangledCard = numericCards.get(selectedIndex);
        active = true;
        markAsUsed();

        JOptionPane.showMessageDialog(
                null,
                entangledCard.toString() + " will count as triple value!"
        );
    }

    // Checks whether the effect is active.
    public boolean isActive() {
        return active;
    }

    // Returns the entangled card.
    public NumericCard getEntangledCard() {
        return entangledCard;
    }

    // Checks whether the given card is entangled.
    public boolean isEntangledCard(NumericCard card) {
        return active && entangledCard == card;
    }

    // Resets the current state.
    @Override
    public void reset() {
        super.reset();
        active = false;
        entangledCard = null;
    }
}