package kulatro.element;

import kulatro.Card;
import kulatro.NumericCard;
import kulatro.Player;
import kulatro.SpecialCard;

public class PeriodicBoost extends SpecialCard {

    // Adds +2 to the numeric value of all cards currently in hand.
    // Value is capped at 9. Applied immediately when activated.

    // Creates a new PeriodicBoost instance.
    public PeriodicBoost() {
        super("Periodic Boost",
              "Add +2 to the numeric value of all cards in current hand");
    }

    // Applies the effect.
    @Override
    public void applyEffect(Player player) {
        for (Card card : player.getHand()) {
            if (card instanceof NumericCard) {
                NumericCard nc = (NumericCard) card;
                nc.addBonus(2);
            }
        }
        markAsUsed();
    }
}