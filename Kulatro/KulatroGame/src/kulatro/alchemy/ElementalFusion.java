package kulatro.alchemy;

import kulatro.Player;
import kulatro.SpecialCard;

public class ElementalFusion extends SpecialCard {

    // If the player has at least 2 cards of the same type,
    // treats them as 4 of that type for scoring purposes.
    // ScoreManager checks isActive() during score calculation.

    private boolean active = false;

    // Creates a new ElementalFusion instance.
    public ElementalFusion() {
        super("Elemental Fusion",
              "If you have at least 2 cards of the same type, treat them as 4 of that type for scoring");
    }

    // Applies the effect.
    @Override
    public void applyEffect(Player player) {
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