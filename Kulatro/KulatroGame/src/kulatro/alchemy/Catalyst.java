package kulatro.alchemy;

import kulatro.Player;
import kulatro.SpecialCard;

public class Catalyst extends SpecialCard {

    // Increases the pair scoring multiplier from x2 to x3.
    // ScoreManager checks isActive() during score calculation.

    private boolean active = false;

    // Creates a new Catalyst instance.
    public Catalyst() {
        super("Catalyst",
              "Increases the multiplier of any pair scoring by +1 (x2 becomes x3)");
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