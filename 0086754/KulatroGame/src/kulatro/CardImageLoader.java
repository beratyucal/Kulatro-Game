package kulatro;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CardImageLoader {

    private Map<String, BufferedImage> images;

    // Creates a new CardImageLoader instance.
    public CardImageLoader() {
        images = new HashMap<>();
        loadCardArtLibrary();
    }

    // Loads all card images.
    private void loadCardArtLibrary() {
        load("Fire",                 "fire.png");
        load("Water",                "water.png");
        load("Earth",                "earth.png");
        load("Air",                  "air.png");
        load("Hydrogen",             "hydrogen.png");
        load("Oxygen",               "oxygen.png");
        load("Nitrogen",             "nitrogen.png");
        load("Carbon Dioxide",       "carbon_dioxide.png");
        load("Quark",                "quark.png");
        load("Boson",                "boson.png");
        load("Gluon",                "gluon.png");
        load("Photon",               "photon.png");
        load("Philosopher's Stone",  "philosophers_stone.png");
        load("Transmutation",        "transmutation.png");
        load("Elemental Fusion",     "elemental_fusion.png");
        load("Catalyst",             "catalyst.png");
        load("Periodic Boost",       "periodic_boost.png");
        load("Noble Gas",            "noble_gas.png");
        load("Isotope Decay",        "isotope_decay.png");
        load("Electron Bond",        "electron_bond.png");
        load("Quantum Entanglement", "quantum_entanglement.png");
        load("Superposition",        "superposition.png");
        load("Gluon Bind",           "gluon_bind.png");
        load("Photon Burst",         "photon_burst.png");
    }

    // Loads a single card image.
    private void load(String cardType, String filename) {
        try {
            // Oncelikle classpath'ten yukle (JAR icin)
            InputStream is = getClass().getResourceAsStream("/cards/" + filename);
            if (is != null) {
                images.put(cardType, ImageIO.read(is));
                return;
            }

            // Sonra src/cards/ klasoründen dene
            File file = new File("src/cards/" + filename);
            if (file.exists()) {
                images.put(cardType, ImageIO.read(file));
                return;
            }

            // Son olarak cards/ klasoründen dene
            File file2 = new File("cards/" + filename);
            if (file2.exists()) {
                images.put(cardType, ImageIO.read(file2));
            }

        } catch (Exception e) {
            System.err.println("Could not load card image: " + filename);
        }
    }

    // Returns the image.
    public BufferedImage imageForCard(String cardType) {
        return images.get(cardType);
    }

    // Checks whether a card image exists.
    public boolean hasCardImage(String cardType) {
        return images.containsKey(cardType) && images.get(cardType) != null;
    }
}