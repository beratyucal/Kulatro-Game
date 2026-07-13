package kulatro.saveload;

import kulatro.SpecialCard;
import kulatro.alchemy.*;
import kulatro.element.*;
import kulatro.quantum.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SpecialCardLoader {

    private static final String ALCHEMY_FILE = "cards/alchemy_special.txt";
    private static final String ELEMENT_FILE = "cards/element_special.txt";
    private static final String QUANTUM_FILE = "cards/quantum_special.txt";

    // Creates a new SpecialCardLoader instance.
    public SpecialCardLoader() {
        File dir = new File("cards/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        prepareDefaultSpecialFiles();
    }

    // Creates the default files.
    private void prepareDefaultSpecialFiles() {
        makeFileIfMissing(ALCHEMY_FILE,
            "PhilosophersStone,Doubles the score of the current hand (one-time use)\n" +
            "Transmutation,Swap one card in hand with a random card from deck\n" +
            "ElementalFusion,Treat 2+ same type cards as 4 of that type for scoring\n" +
            "Catalyst,Increases pair multiplier by +1 (x2 becomes x3)");

        makeFileIfMissing(ELEMENT_FILE,
            "PeriodicBoost,Add +2 to the numeric value of all cards in current hand\n" +
            "NobleGas,Lock one card - cannot be discarded but value counts twice\n" +
            "IsotopeDecay,Discard entire hand and draw 4 new cards (one-time)\n" +
            "ElectronBond,Create a pair between two different card types");

        makeFileIfMissing(QUANTUM_FILE,
            "QuantumEntanglement,Triple the score of one card from the current hand\n" +
            "Superposition,Play two scoring patterns - take the better result\n" +
            "GluonBind,Merge two cards into one with combined value (max 9)\n" +
            "PhotonBurst,Reveal next 3 cards in deck, swap one with a card in hand");
    }

    // Creates the file if it is missing.
    private void makeFileIfMissing(String filename, String defaultContent) {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(defaultContent);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Loads the alchemy cards.
    public List<SpecialCard> readAlchemySpecialCards() throws IOException {
        return readSpecialCardFile(ALCHEMY_FILE, "alchemy");
    }

    // Loads the element cards.
    public List<SpecialCard> readElementSpecialCards() throws IOException {
        return readSpecialCardFile(ELEMENT_FILE, "element");
    }

    // Loads the quantum cards.
    public List<SpecialCard> readQuantumSpecialCards() throws IOException {
        return readSpecialCardFile(QUANTUM_FILE, "quantum");
    }

    // Loads the cards.
    private List<SpecialCard> readSpecialCardFile(String filename, String deckType) throws IOException {
        List<SpecialCard> cards = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) {
            throw new FileNotFoundException("Special card file not found: " + filename);
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) continue;
            String[] parts = line.split(",", 2);
            if (parts.length < 2) continue;

            String cardName = parts[0];
            SpecialCard card = makeSpecialCardByName(cardName, deckType);
            if (card != null) {
                cards.add(card);
            }
        }
        reader.close();
        return cards;
    }

    // Creates the card.
    private SpecialCard makeSpecialCardByName(String cardName, String deckType) {
        switch (deckType) {
            case "alchemy":
                switch (cardName) {
                    case "PhilosophersStone": return new PhilosophersStone();
                    case "Transmutation": return new Transmutation();
                    case "ElementalFusion": return new ElementalFusion();
                    case "Catalyst": return new Catalyst();
                }
                break;
            case "element":
                switch (cardName) {
                    case "PeriodicBoost": return new PeriodicBoost();
                    case "NobleGas": return new NobleGas();
                    case "IsotopeDecay": return new IsotopeDecay();
                    case "ElectronBond": return new ElectronBond();
                }
                break;
            case "quantum":
                switch (cardName) {
                    case "QuantumEntanglement": return new QuantumEntanglement();
                    case "Superposition": return new Superposition();
                    case "GluonBind": return new GluonBind();
                    case "PhotonBurst": return new PhotonBurst();
                }
                break;
        }
        return null;
    }
}