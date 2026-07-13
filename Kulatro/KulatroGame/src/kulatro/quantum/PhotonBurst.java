package kulatro.quantum;

import kulatro.Card;
import kulatro.NumericCard;
import kulatro.Player;
import kulatro.SpecialCard;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class PhotonBurst extends SpecialCard {

    // Reveals the next 3 cards in the deck.
    // Player may optionally swap one of the revealed cards with a card in hand.
    // Unswapped revealed cards are returned to the top of the deck.

    // Creates a new PhotonBurst instance.
    public PhotonBurst() {
        super("Photon Burst",
              "Reveal the next 3 cards in deck, you may swap one with a card in hand");
    }

   
    // Applies the effect.
    @Override
    public void applyEffect(Player player) {
        List<Card> revealed = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Card drawn = player.getDeck().draw();
            if (drawn != null) revealed.add(drawn);
        }

        if (revealed.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No cards left in deck!");
            return;
        }

        StringBuilder sb = new StringBuilder("Revealed cards:\n");
        for (Card c : revealed) sb.append("- ").append(c.toString()).append("\n");
        sb.append("\nDo you want to swap one with a card in hand?");

        int swap = JOptionPane.showConfirmDialog(null, sb.toString(),
            "Photon Burst", JOptionPane.YES_NO_OPTION);

        boolean used = false;

        if (swap == JOptionPane.YES_OPTION) {
            String[] revOptions = revealed.stream()
                .map(Card::toString)
                .toArray(String[]::new);

            String chosenRevealed = (String) JOptionPane.showInputDialog(
                null, "Select a revealed card to take:",
                "Photon Burst", JOptionPane.QUESTION_MESSAGE,
                null, revOptions, revOptions[0]
            );

            if (chosenRevealed != null) {
                List<Card> hand = player.getHand();
                String[] handOptions = hand.stream()
                    .map(Card::toString)
                    .toArray(String[]::new);

                String chosenHand = (String) JOptionPane.showInputDialog(
                    null, "Select a hand card to swap:",
                    "Photon Burst", JOptionPane.QUESTION_MESSAGE,
                    null, handOptions, handOptions[0]
                );

                if (chosenHand != null) {
                    Card takenCard = null;
                    for (Card c : revealed) {
                        if (c.toString().equals(chosenRevealed)) {
                            takenCard = c;
                            break;
                        }
                    }
                    for (int i = 0; i < hand.size(); i++) {
                        if (hand.get(i).toString().equals(chosenHand)) {
                            hand.remove(i);
                            break;
                        }
                    }
                    if (takenCard != null) {
                        hand.add(takenCard);
                        revealed.remove(takenCard);
                        used = true; // sadece swap yapilinca kullanilmis say
                    }
                }
            }
        }

        // Kalan kartlari desteye geri koy
        for (int i = revealed.size() - 1; i >= 0; i--) {
            player.getDeck().getCards().add(0, revealed.get(i));
        }

        // Sadece swap yapildiysa markAsUsed
        if (used) markAsUsed();
    }
}