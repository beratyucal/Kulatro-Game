package kulatro.engine;

import kulatro.AbstractDeck;
import kulatro.Card;
import kulatro.Player;
import kulatro.SpecialCard;
import kulatro.alchemy.AlchemyDeck;
import kulatro.element.ElementDeck;
import kulatro.quantum.QuantumDeck;
import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    private Game currentGame;
    private List<Player> registerNewPlayeredPlayers;

    // Creates a new GameEngine instance.
    public GameEngine() {
        registerNewPlayeredPlayers = new ArrayList<>();
    }

    
    // Registers a player through the manager.
    public boolean createPlayerAccount(String username, String password) {
        for (Player p : registerNewPlayeredPlayers) {
            if (p.getUsername().equals(username)) {
                return false; 
            }
        }
        registerNewPlayeredPlayers.add(new Player(username, password));
        return true;
    }

    
    // Logs in a player.
    public Player login(String username, String password) {
        for (Player p : registerNewPlayeredPlayers) {
            if (p.getUsername().equals(username) && p.checkPassword(password)) {
                return p;
            }
        }
        return null;
    }

    
    // Creates the deck.
    public AbstractDeck makeDeckForType(String deckType) {
        switch (deckType) {
            case "Alchemy":
                return new AlchemyDeck();
            case "Element":
                return new ElementDeck();
            case "Quantum":
                return new QuantumDeck();
            default:
                throw new IllegalArgumentException("Invalid deck type: " + deckType);
        }
    }

    
    // Returns the special cards.
    public List<SpecialCard> findSpecialCardsInDeck(AbstractDeck deck) {
        List<SpecialCard> specialCards = new ArrayList<>();
        for (Card card : deck.getCards()) {
            if (card instanceof SpecialCard) {
                specialCards.add((SpecialCard) card);
            }
        }
        return specialCards;
    }

    
    // Creates the game.
    public Game startGameSession(Player player, String sessionName, String difficulty, String deckType) {
        currentGame = new Game(player, sessionName, difficulty, deckType);
        return currentGame;
    }

    // Returns the current game.
    public Game currentGame() {
        return currentGame;
    }

    // Returns the registerNewPlayered players.
    public List<Player> registerNewPlayeredPlayers() {
        return registerNewPlayeredPlayers;
    }
}