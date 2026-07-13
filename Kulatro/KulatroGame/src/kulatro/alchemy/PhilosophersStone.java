package kulatro.alchemy;

import kulatro.Hand;
import kulatro.Player;
import kulatro.SpecialCard;
import java.util.List;
import kulatro.Card;

public class PhilosophersStone extends SpecialCard {

    // Doubles the score of the submitted hand. One-time use only.
    // When activated, sets a flag. GameFrame checks this flag after submit
    // and doubles the round score before storing it.

    private boolean activated = false;

    // Creates a new PhilosophersStone instance.
    public PhilosophersStone() {
        super("Philosopher's Stone",
              "Doubles the score of the current hand (one-time use)");
    }

    // Applies the effect.
    @Override
    public void applyEffect(Player player) {
        activated = true;
        markAsUsed();
    }

    // Checks whether the effect has been activated.
    public boolean isActivated() {
        return activated;
    }

    // Resets the activation.
    public void resetActivation() {
        activated = false;
    }
}