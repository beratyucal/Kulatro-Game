package kulatro.element;

import kulatro.AbstractDeck;
import kulatro.NumericCard;

public class ElementDeck extends AbstractDeck {
    private static final String[] TYPES = {"Hydrogen", "Oxygen", "Nitrogen", "Carbon Dioxide"};

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
        cards.add(new PeriodicBoost());
        cards.add(new NobleGas());
        cards.add(new IsotopeDecay());
        cards.add(new ElectronBond());
    }
}