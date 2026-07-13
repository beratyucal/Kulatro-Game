package kulatro.alchemy;

import kulatro.AbstractDeck;
import kulatro.NumericCard;

public class AlchemyDeck extends AbstractDeck {
    private static final String[] TYPES = {"Fire", "Water", "Earth", "Air"};

    // Initializes the numeric card list.
    @Override
    protected void buildNumberCards() {
        for (String type : TYPES) {
            for (int value = 1; value <= 9; value++) {
                cards.add(new NumericCard(type, value));
            }
        }
    }

    // Initializes the special card list.
    @Override
    protected void buildSpecialCards() {
        cards.add(new PhilosophersStone());
        cards.add(new Transmutation());
        cards.add(new ElementalFusion());
        cards.add(new Catalyst());
    }
}