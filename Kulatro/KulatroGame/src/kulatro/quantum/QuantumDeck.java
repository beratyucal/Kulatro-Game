package kulatro.quantum;

import kulatro.AbstractDeck;
import kulatro.NumericCard;

public class QuantumDeck extends AbstractDeck {
    private static final String[] TYPES = {"Quark", "Boson", "Gluon", "Photon"};

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
        cards.add(new QuantumEntanglement());
        cards.add(new Superposition());
        cards.add(new GluonBind());
        cards.add(new PhotonBurst());
    }
}