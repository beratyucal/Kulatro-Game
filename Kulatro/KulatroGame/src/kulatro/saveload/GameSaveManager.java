package kulatro.saveload;

import kulatro.AbstractDeck;
import kulatro.Card;
import kulatro.NumericCard;
import kulatro.Player;
import kulatro.Round;
import kulatro.SpecialCard;
import kulatro.alchemy.*;
import kulatro.element.*;
import kulatro.quantum.*;
import kulatro.engine.Game;
import kulatro.engine.GameEngine;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameSaveManager {

    private static final String SAVE_DIR = "saves/";

    // Creates a new GameSaveManager instance.
    public GameSaveManager() {
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // Saves the game.
    public void writeGameSave(Game game) throws IOException {
        String filename = SAVE_DIR + game.getPlayer().getUsername()
                + "_" + game.getSessionName() + ".txt";

        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        writer.write("username=" + game.getPlayer().getUsername()); writer.newLine();
        writer.write("sessionName=" + game.getSessionName()); writer.newLine();
        writer.write("difficulty=" + game.getDifficulty()); writer.newLine();
        writer.write("deckType=" + game.getDeckType()); writer.newLine();
        writer.write("currentRound=" + game.getCurrentRoundIndex()); writer.newLine();
        writer.write("totalScore=" + game.getPlayer().currentTotalScore()); writer.newLine();
        writer.write("remainingDiscards=" + game.getPlayer().getRemainingDiscards()); writer.newLine();
        writer.write("roundDiscards=" + game.getPlayer().getRoundDiscards()); writer.newLine();

        Round[] rounds = game.getRounds();
        for (int i = 0; i < rounds.length; i++) {
            writer.write("round" + (i + 1) + "Score=" + rounds[i].getRoundScore()); writer.newLine();
            writer.write("round" + (i + 1) + "SubmittedCount=" + rounds[i].getSubmittedCardCount()); writer.newLine();
        }

        StringBuilder handStr = new StringBuilder();
        for (Card card : game.getPlayer().getHand()) {
            if (card instanceof NumericCard) {
                handStr.append(card.getType()).append("-").append(((NumericCard) card).getValue()).append(";");
            } else if (card instanceof SpecialCard) {
                handStr.append("SPECIAL-").append(card.getType()).append(";");
            }
        }
        writer.write("hand=" + handStr); writer.newLine();

        StringBuilder deckStr = new StringBuilder();
        for (Card card : game.getPlayer().getDeck().getCards()) {
            if (card instanceof NumericCard) {
                deckStr.append(card.getType()).append("-").append(((NumericCard) card).getValue()).append(";");
            } else if (card instanceof SpecialCard) {
                deckStr.append("SPECIAL-").append(card.getType()).append(";");
            }
        }
        writer.write("remainingDeck=" + deckStr); writer.newLine();

        StringBuilder discardStr = new StringBuilder();
        for (Card card : game.getPlayer().getDiscardPile()) {
            if (card instanceof NumericCard) {
                discardStr.append(card.getType()).append("-").append(((NumericCard) card).getValue()).append(";");
            } else if (card instanceof SpecialCard) {
                discardStr.append("SPECIAL-").append(card.getType()).append(";");
            }
        }
        writer.write("discardPile=" + discardStr); writer.newLine();

        SpecialCard sc = game.getPlayer().getChosenSpecialCard();
        if (sc != null) {
            writer.write("specialCard=" + sc.getType()); writer.newLine();
            writer.write("specialCardUsed=" + sc.isUsed()); writer.newLine();
        } else {
            writer.write("specialCard=none"); writer.newLine();
            writer.write("specialCardUsed=false"); writer.newLine();
        }

        writer.close();
    }

    // Loads the game.
    public Game readGameSave(String username, String sessionName, Player player) throws IOException {
        String filename = SAVE_DIR + username + "_" + sessionName + ".txt";
        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException("Save file not found: " + filename);
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        String difficulty = "Medium";
        String deckType = "Alchemy";
        int currentRound = 0;
        int totalScore = 0;
        int remainingDiscards = 6;
        int roundDiscards = 0;
        int[] roundScores = new int[4];
        String handData = "";
        String deckData = "";
        String specialCardName = "none";
        boolean specialCardUsed = false;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("difficulty="))
                difficulty = line.substring(11);
            else if (line.startsWith("deckType="))
                deckType = line.substring(9);
            else if (line.startsWith("currentRound="))
                currentRound = Integer.parseInt(line.substring(13));
            else if (line.startsWith("totalScore="))
                totalScore = Integer.parseInt(line.substring(11));
            else if (line.startsWith("remainingDiscards="))
                remainingDiscards = Integer.parseInt(line.substring(18));
            else if (line.startsWith("roundDiscards="))
                roundDiscards = Integer.parseInt(line.substring(14));
            else if (line.startsWith("round1Score="))
                roundScores[0] = Integer.parseInt(line.substring(12));
            else if (line.startsWith("round2Score="))
                roundScores[1] = Integer.parseInt(line.substring(12));
            else if (line.startsWith("round3Score="))
                roundScores[2] = Integer.parseInt(line.substring(12));
            else if (line.startsWith("round4Score="))
                roundScores[3] = Integer.parseInt(line.substring(12));
            else if (line.startsWith("hand="))
                handData = line.substring(5);
            else if (line.startsWith("remainingDeck="))
                deckData = line.substring(14);
            else if (line.startsWith("specialCard="))
                specialCardName = line.substring(12);
            else if (line.startsWith("specialCardUsed="))
                specialCardUsed = Boolean.parseBoolean(line.substring(16));
        }
        reader.close();

        GameEngine engine = new GameEngine();
        Game game = engine.startGameSession(player, sessionName, difficulty, deckType);
        game.setCurrentRoundIndex(currentRound);

        for (int i = 0; i < currentRound && i < 4; i++) {
            game.getRounds()[i].setRoundScore(roundScores[i]);
        }

        AbstractDeck deck = engine.makeDeckForType(deckType);
        deck.getCards().clear();

        if (!deckData.isEmpty()) {
            String[] deckCards = deckData.split(";");
            for (String cardStr : deckCards) {
                if (cardStr.isEmpty()) continue;
                Card card = cardFromText(cardStr);
                if (card != null) deck.getCards().add(card);
            }
        }
        player.setDeck(deck);

        player.emptyHand();
        if (!handData.isEmpty()) {
            String[] handCards = handData.split(";");
            for (String cardStr : handCards) {
                if (cardStr.isEmpty()) continue;
                Card card = cardFromText(cardStr);
                if (card != null) player.getHand().add(card);
            }
        }

        player.restoreTotalScore(totalScore);
        player.restoreRemainingDiscards(remainingDiscards);
        player.restoreRoundDiscards(roundDiscards);

        if (!specialCardName.equals("none")) {
            SpecialCard sc = specialCardFromText(specialCardName);
            if (sc != null) {
                if (specialCardUsed) sc.markAsUsed();
                player.chooseSpecialCard(sc);
            }
        }

        return game;
    }

    // Parses the card.
    private Card cardFromText(String cardStr) {
        if (cardStr.startsWith("SPECIAL-")) {
            return specialCardFromText(cardStr.substring(8));
        }
        int lastDash = cardStr.lastIndexOf("-");
        if (lastDash == -1) return null;
        try {
            String type = cardStr.substring(0, lastDash);
            int value = Integer.parseInt(cardStr.substring(lastDash + 1));
            return new NumericCard(type, value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Creates the special card.
    private SpecialCard specialCardFromText(String name) {
        switch (name) {
            case "Philosopher's Stone": return new PhilosophersStone();
            case "Transmutation":       return new Transmutation();
            case "Elemental Fusion":    return new ElementalFusion();
            case "Catalyst":            return new Catalyst();
            case "Periodic Boost":      return new PeriodicBoost();
            case "Noble Gas":           return new NobleGas();
            case "Isotope Decay":       return new IsotopeDecay();
            case "Electron Bond":       return new ElectronBond();
            case "Quantum Entanglement":return new QuantumEntanglement();
            case "Superposition":       return new Superposition();
            case "Gluon Bind":          return new GluonBind();
            case "Photon Burst":        return new PhotonBurst();
            default:                    return null;
        }
    }

    // Checks whether the save file exists.
    public boolean hasSaveForSession(String username, String sessionName) {
        return new File(SAVE_DIR + username + "_" + sessionName + ".txt").exists();
    }

    // Returns the saved sessions.
    public String[] listSavedSessions(String username) {
        File dir = new File(SAVE_DIR);
        File[] files = dir.listFiles((d, name) -> name.startsWith(username + "_"));
        if (files == null) return new String[0];
        String[] sessions = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            sessions[i] = files[i].getName()
                    .replace(username + "_", "")
                    .replace(".txt", "");
        }
        return sessions;
    }

    // Deletes the save.
    public void deleteSaveFile(String username, String sessionName) {
        new File(SAVE_DIR + username + "_" + sessionName + ".txt").delete();
    }
}