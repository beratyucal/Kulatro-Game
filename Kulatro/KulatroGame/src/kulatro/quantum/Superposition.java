package kulatro.quantum;

import kulatro.Card;
import kulatro.NumericCard;
import kulatro.Player;
import kulatro.ScoreManager;
import kulatro.SpecialCard;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class Superposition extends SpecialCard {

    /*
     * Superposition:
     * Plays two scoring patterns simultaneously
     * and takes the better result.
     */

    private boolean active = false;

    // Creates a new Superposition instance.
    public Superposition() {
        super(
            "Superposition",
            "Play two scoring patterns simultaneously - take the better result"
        );
    }

    // Applies the effect.
    @Override
    public void applyEffect(Player player) {
        List<Card> hand = player.getHand();
        List<Card> numericOnly = new ArrayList<>();

        for (Card card : hand) {
            if (card instanceof NumericCard) {
                numericOnly.add(card);
            }
        }

        if (numericOnly.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No numeric cards in hand!");
            return;
        }

        ScoreManager scoreManager = new ScoreManager();

        int normalScore = scoreManager.scoreCards(numericOnly, null);
        int superScore = scoreManager.previewSuperpositionScore(numericOnly);
        int bestScore = Math.max(normalScore, superScore);

        int confirm = JOptionPane.showConfirmDialog(
            null,
            "Normal score: " + normalScore + "\n" +
            "Superposition score: " + superScore + "\n" +
            "Result used: " + bestScore + "\n\n" +
            "Activate Superposition?",
            "Superposition",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        active = true;
        markAsUsed();
    }

    // Checks whether the effect is active.
    public boolean isActive() {
        return active;
    }

    // Resets the current state.
    @Override
    public void reset() {
        super.reset();
        active = false;
    }
}
