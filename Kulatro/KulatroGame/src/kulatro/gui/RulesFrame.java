package kulatro.gui;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class RulesFrame extends JFrame {

    private BufferedImage bgImage;

    // Creates a new RulesFrame instance.
    public RulesFrame() {
        setTitle("Kulatro - How to Play");
        setSize(800, 650);
        setLocationRelativeTo(null);
        setResizable(true);
        loadRulesBackground();
        arrangeRulesView();
        setVisible(true);
    }

    // Loads the background.
    private void loadRulesBackground() {
        try {
            InputStream is = getClass().getResourceAsStream("/kulatro_bg1.png");
            if (is != null) {
                bgImage = ImageIO.read(is);
            }
        } catch (Exception e) {
            bgImage = null;
        }
    }

    // Initializes the components.
    private void arrangeRulesView() {
        JPanel bgPanel = new JPanel(new BorderLayout()) {
            // Paints this component.
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
                } else {
                    g.setColor(new Color(10, 15, 25));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                g.setColor(new Color(5, 10, 20, 210));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Baslik
        JLabel titleLabel = new JLabel("HOW TO PLAY", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(212, 175, 55));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        bgPanel.add(titleLabel, BorderLayout.NORTH);

        // Icerik - sekmeli
        JTabbedPane tabs = new JTabbedPane();
        tabs.setOpaque(false);
        tabs.setBackground(new Color(10, 15, 30, 0));
        tabs.setForeground(new Color(212, 175, 55));
        tabs.setFont(new Font("Arial", Font.BOLD, 13));

        tabs.addTab("Overview", makeRulesTab(
            "KULATRO - A Strategy of Elements\n\n" +
            "Kulatro is a round-based card game where you use cards to reach target scores.\n\n" +
            "OBJECTIVE:\n" +
            "Play 4 rounds and score higher than the average threshold to win the game.\n\n" +
            "WINNING CONDITION:\n" +
            "At the end of 4 rounds, if your average score is greater than the average\n" +
            "of all target scores, you win!\n\n" +
            "Example:\n" +
            "  Round 1: Target 50  ->  Your Score: 70  (WIN)\n" +
            "  Round 2: Target 65  ->  Your Score: 55  (LOSE)\n" +
            "  Round 3: Target 80  ->  Your Score: 90  (WIN)\n" +
            "  Round 4: Target 95  ->  Your Score: 85  (LOSE)\n\n" +
            "  Threshold Average: 72.5\n" +
            "  Your Average: 75\n" +
            "  Result: YOU WIN!"
        ));

        tabs.addTab("Decks", makeRulesTab(
            "CHOOSE YOUR DECK\n\n" +
            "ALCHEMY SET\n" +
            "  Cards: Fire, Water, Earth, Air\n" +
            "  Special Cards:\n" +
            "    - Philosopher's Stone: Doubles your hand score (one-time)\n" +
            "    - Transmutation: Swap a card with random deck card\n" +
            "    - Elemental Fusion: 2+ same type = treat as 4 of that type\n" +
            "    - Catalyst: Pair multiplier x2 becomes x3\n\n" +
            "ELEMENT SET\n" +
            "  Cards: Hydrogen, Oxygen, Nitrogen, Carbon Dioxide\n" +
            "  Special Cards:\n" +
            "    - Periodic Boost: Add +2 to all card values\n" +
            "    - Noble Gas: Lock one card, its value counts twice\n" +
            "    - Isotope Decay: Discard all, draw 4 new (one-time)\n" +
            "    - Electron Bond: Two different types count as a pair\n\n" +
            "QUANTUM SET\n" +
            "  Cards: Quark, Boson, Gluon, Photon\n" +
            "  Special Cards:\n" +
            "    - Quantum Entanglement: Triple one card's score\n" +
            "    - Superposition: Play two patterns, take the better\n" +
            "    - Gluon Bind: Merge two cards (max value 9)\n" +
            "    - Photon Burst: Reveal 3 deck cards, swap one"
        ));

        tabs.addTab("Scoring", makeRulesTab(
            "SCORING RULES\n\n" +
            "Your score depends on the cards you submit:\n\n" +
            "FOUR OF A KIND (all 4 cards same type)\n" +
            "  Score = Sum of values x 10\n" +
            "  Example: Fire-5, Fire-3, Fire-7, Fire-2\n" +
            "           (5+3+7+2) x 10 = 170\n\n" +
            "ONE OF EACH TYPE (all 4 types present)\n" +
            "  Score = Sum of values x 5\n" +
            "  Example: Fire-4, Water-6, Earth-2, Air-8\n" +
            "           (4+6+2+8) x 5 = 100\n\n" +
            "ONE PAIR (two cards same type)\n" +
            "  Score = Pair sum x 2 + rest\n" +
            "  Example: Fire-5, Fire-3, Water-7, Earth-2\n" +
            "           (5+3) x 2 + 7 + 2 = 25\n\n" +
            "ALL OTHER CASES\n" +
            "  Score = Sum of all values\n" +
            "  Example: Fire-5, Water-3, Earth-7\n" +
            "           5+3+7 = 15"
        ));

        tabs.addTab("Gameplay", makeRulesTab(
            "HOW TO PLAY\n\n" +
            "1. SETUP\n" +
            "   - Choose your deck (Alchemy / Element / Quantum)\n" +
            "   - Choose difficulty (Easy / Medium / Hard)\n" +
            "   - Select one special card from your deck\n\n" +
            "2. EACH ROUND\n" +
            "   - You are dealt 4 cards from your deck\n" +
            "   - You can discard cards to get new ones\n" +
            "   - Total discards across all 4 rounds: max 6\n" +
            "   - Submit your hand when ready\n\n" +
            "3. DISCARDING\n" +
            "   - Click a card to select it (it highlights)\n" +
            "   - Press Discard to swap with new cards\n" +
            "   - You cannot discard more than 6 total\n\n" +
            "4. SPECIAL CARD\n" +
            "   - Press Special Card button to use your power\n" +
            "   - One-time cards disappear after use\n" +
            "   - Others stay active all game\n\n" +
            "5. DIFFICULTY\n" +
            "   - Easy:   Targets 40 / 55 / 70 / 85\n" +
            "   - Medium: Targets 50 / 65 / 80 / 95\n" +
            "   - Hard:   Targets 60 / 75 / 90 / 105"
        ));

        bgPanel.add(tabs, BorderLayout.CENTER);

        // Kapat butonu
        JButton closeBtn = new JButton("Close") {
            // Paints this component.
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(212, 175, 55));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        closeBtn.setForeground(new Color(212, 175, 55));
        closeBtn.setFont(new Font("Arial", Font.BOLD, 14));
        closeBtn.setFocusPainted(false);
        closeBtn.setOpaque(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        closeBtn.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)) {
            // Paints this component.
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(10, 15, 30, 220));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bottomPanel.setOpaque(false);
        bottomPanel.add(closeBtn);
        bgPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(bgPanel);
    }

    // Creates the tab.
    private JScrollPane makeRulesTab(String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setForeground(new Color(220, 220, 220));
        textArea.setFont(new Font("Arial", Font.PLAIN, 13));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(10, 15, 30, 180));
        return scroll;
    }
}
