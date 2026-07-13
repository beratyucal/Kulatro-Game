package kulatro.element;

import kulatro.Player;
import kulatro.SpecialCard;

public class IsotopeDecay extends SpecialCard {

    // Discards the entire hand and draws 4 new cards.
    // Does not consume the discard limit. One-time use only.

    // Creates a new IsotopeDecay instance.
    public IsotopeDecay() {
        super("Isotope Decay",
              "Discard your entire hand and draw 4 new cards without using discard limit (one-time)");
    }

    // Applies the effect.
    @Override
    public void applyEffect(Player player) {
        player.emptyHand();
        player.drawSomeCards(4);
        markAsUsed();
    }
}