package kulatro.gui;

import kulatro.Card;
import kulatro.CardImageLoader;
import kulatro.NumericCard;
import kulatro.Player;
import kulatro.Round;
import kulatro.SpecialCard;
import kulatro.engine.Game;
import kulatro.engine.GameEngine;
import kulatro.saveload.GameLogger;
import kulatro.saveload.GameSaveManager;
import kulatro.saveload.PlayerManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GameFrame extends JFrame {

    private static final double HUD_Y_RATIO = 0.012;
    private static final double HUD_H_RATIO = 0.095;

    private static final double CARD_AREA_X_RATIO = 0.10;
    private static final double CARD_AREA_Y_RATIO = 0.15;
    private static final double CARD_AREA_W_RATIO = 0.66;
    private static final double CARD_AREA_H_RATIO = 0.62;

    private static final int CARD_W = 155;
    private static final int CARD_H = 225;

    private static final double RIGHT_X_RATIO = 0.795;
    private static final double RIGHT_Y_RATIO = 0.255;
    private static final double RIGHT_W_RATIO = 0.175;
    private static final double RIGHT_H_RATIO = 0.47;

    private static final double BTN_X_RATIO = 0.12;
    private static final double BTN_Y_RATIO = 0.910;
    private static final double BTN_W_RATIO = 0.61;

    private static final int BTN_W = 110;
    private static final int BTN_H = 34;

    private Player player;
    private Game game;
    private GameEngine gameEngine;
    private GameSaveManager saveManager;
    private GameLogger logger;
    private String deckType;
    private CardImageLoader cardImageLoader;

    private List<Card> selectedCards;
    private BufferedImage bgImage;

    private JPanel mainPanel;
    private JPanel hudPanel;
    private JPanel cardAreaPanel;
    private JPanel handPanel;
    private JPanel rightPanel;
    private JPanel buttonPanel;

    private JLabel gameInfoLabel;
    private JLabel roundLabel;
    private JLabel targetLabel;
    private JLabel scoreLabel;
    private JLabel discardsLabel;
    private JLabel deckCountLabel;
    private JLabel deckTypeLabel;

    private JLabel handTitle;

    private JLabel infoTitle;
    private JLabel specialCardLabel;
    private JLabel selTitle;
    private JLabel selectedCardsLabel;
    private JLabel logTitle;
    private JTextArea logArea;
    private JScrollPane logScroll;

    private JButton discardBtn;
    private JButton submitBtn;
    private JButton specialBtn;
    private JButton saveBtn;
    private JButton menuBtn;

    // Creates a new GameFrame instance.
    public GameFrame(Player player, String deckType, String difficulty, String sessionName) {
        this.player = player;
        this.deckType = deckType;
        this.gameEngine = new GameEngine();
        this.saveManager = new GameSaveManager();
        this.logger = GameLogger.getInstance();
        this.selectedCards = new ArrayList<>();
        this.cardImageLoader = new CardImageLoader();

        prepareFreshGameState();
        game = gameEngine.startGameSession(player, sessionName, difficulty, deckType);
        player.setDeck(gameEngine.makeDeckForType(deckType));

        logger.logGameStart(player.getUsername(), deckType, difficulty);

        loadGameBackground();
        prepareGameWindow();
        askForSpecialCard();
        beginVisibleRound();
    }

    // Resets player state for a fresh game session.
    private void prepareFreshGameState() {
        player.restoreTotalScore(0);
        player.restoreRemainingDiscards(6);
        player.restoreRoundDiscards(0);
        player.chooseSpecialCard(null);
        player.emptyHand();
    }

    // Creates a new GameFrame instance.
    public GameFrame(Player player, String sessionName, GameSaveManager saveManager) {
        this.player = player;
        this.saveManager = saveManager;
        this.gameEngine = new GameEngine();
        this.logger = GameLogger.getInstance();
        this.selectedCards = new ArrayList<>();
        this.cardImageLoader = new CardImageLoader();
        this.deckType = "";

        loadGameBackground();
        prepareGameWindow();

        try {
            game = saveManager.readGameSave(player.getUsername(), sessionName, player);
            deckType = game.getDeckType();

            refreshGameScreen();

            logArea.append("Game loaded: " + sessionName + "\n");
            logArea.append("Round: " + (game.getCurrentRoundIndex() + 1) + "\n");
            logArea.append("Cards in hand: " + player.getHand().size() + "\n");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Loads the background.
    private void loadGameBackground() {
        try {
            InputStream is = getClass().getResourceAsStream("/game_bg1.png");

            if (is != null) {
                bgImage = ImageIO.read(is);
                System.out.println("Game background loaded from resource.");
                return;
            }

            File file = new File("game_bg1.png");

            if (file.exists()) {
                bgImage = ImageIO.read(file);
                System.out.println("Game background loaded from external file.");
                return;
            }

            file = new File("src/game_bg1.png");

            if (file.exists()) {
                bgImage = ImageIO.read(file);
                System.out.println("Game background loaded from src file.");
                return;
            }

            System.out.println("Game background image not found!");

        } catch (Exception e) {
            e.printStackTrace();
            bgImage = null;
        }
    }

    // Initializes the frame.
    private void prepareGameWindow() {
        setTitle("Kulatro - Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        mainPanel = makeMainGamePanel();

        buildHudPanel();
        buildCardArea();
        buildSideInfoPanel();
        buildActionButtons();

        mainPanel.add(hudPanel);
        mainPanel.add(cardAreaPanel);
        mainPanel.add(rightPanel);
        mainPanel.add(buttonPanel);

        installResizeHandler();

        setContentPane(mainPanel);
        setVisible(true);
    }

    // Creates the main panel.
    private JPanel makeMainGamePanel() {
        return new JPanel(null) {
            // Paints this component.
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
                } else {
                    Graphics2D g2 = (Graphics2D) g.create();

                    g2.setPaint(new GradientPaint(
                            0, 0, new Color(25, 10, 10),
                            getWidth(), getHeight(), new Color(5, 5, 15)
                    ));

                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }
            }
        };
    }

    // Creates the hud panel.
    private void buildHudPanel() {
        hudPanel = new JPanel(new GridLayout(1, 7, 8, 0));
        hudPanel.setOpaque(false);

        gameInfoLabel = new JLabel("", SwingConstants.CENTER);
        roundLabel = new JLabel("", SwingConstants.CENTER);
        targetLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel = new JLabel("", SwingConstants.CENTER);
        discardsLabel = new JLabel("", SwingConstants.CENTER);
        deckCountLabel = new JLabel("", SwingConstants.CENTER);
        deckTypeLabel = new JLabel("", SwingConstants.CENTER);

        hudPanel.add(makeHudStatBox(gameInfoLabel, "Game Info"));
        hudPanel.add(makeHudStatBox(roundLabel, "Round"));
        hudPanel.add(makeHudStatBox(targetLabel, "Target"));
        hudPanel.add(makeHudStatBox(scoreLabel, "Score"));
        hudPanel.add(makeHudStatBox(discardsLabel, "Discards"));
        hudPanel.add(makeHudStatBox(deckCountLabel, "Deck"));
        hudPanel.add(makeHudStatBox(deckTypeLabel, "Type"));
    }

    // Creates the card area panel.
    private void buildCardArea() {
        cardAreaPanel = new JPanel(null);
        cardAreaPanel.setOpaque(false);

        handTitle = new JLabel("YOUR HAND", SwingConstants.CENTER);
        handTitle.setFont(new Font("Serif", Font.BOLD, 24));
        handTitle.setForeground(new Color(230, 190, 50));

        handPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        handPanel.setOpaque(false);

        cardAreaPanel.add(handTitle);
        cardAreaPanel.add(handPanel);
    }

    // Creates the right panel.
    private void buildSideInfoPanel() {
        rightPanel = new JPanel(null);
        rightPanel.setOpaque(false);

        infoTitle = makeSectionTitle("GAME INFO");
        selTitle = makeSectionTitle("SELECTED CARDS");
        logTitle = makeSectionTitle("GAME LOG");

        specialCardLabel = new JLabel("<html>Special: None</html>");
        specialCardLabel.setFont(new Font("Georgia", Font.BOLD, 12));
        specialCardLabel.setForeground(new Color(245, 215, 150));
        specialCardLabel.setVerticalAlignment(SwingConstants.TOP);

        selectedCardsLabel = new JLabel("-");
        selectedCardsLabel.setFont(new Font("Georgia", Font.BOLD, 12));
        selectedCardsLabel.setForeground(new Color(245, 215, 150));
        selectedCardsLabel.setVerticalAlignment(SwingConstants.TOP);

        logArea = new JTextArea("Game started...\n");
        logArea.setEditable(false);
        logArea.setFocusable(false);
        logArea.setOpaque(false);
        logArea.setBackground(new Color(0, 0, 0, 0));
        logArea.setForeground(new Color(245, 215, 150));

        // Game Log yazısı biraz küçültüldü. Böylece daha az satır kırıyor.
        logArea.setFont(new Font("Georgia", Font.BOLD, 10));

        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBorder(null);

        logScroll = new JScrollPane(logArea);
        logScroll.setOpaque(false);
        logScroll.getViewport().setOpaque(false);
        logScroll.getViewport().setBackground(new Color(0, 0, 0, 0));
        logScroll.setBorder(null);
        logScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        logScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JScrollBar vertical = logScroll.getVerticalScrollBar();
        vertical.setOpaque(false);
        vertical.setPreferredSize(new Dimension(6, 0));

        rightPanel.add(infoTitle);
        rightPanel.add(specialCardLabel);
        rightPanel.add(selTitle);
        rightPanel.add(selectedCardsLabel);
        rightPanel.add(logTitle);
        rightPanel.add(logScroll);
    }

    // Creates the button panel.
    private void buildActionButtons() {
        buttonPanel = new JPanel(null);
        buttonPanel.setOpaque(false);

        discardBtn = makeFantasyButton("Discard", new Color(165, 50, 30));
        submitBtn = makeFantasyButton("Submit", new Color(170, 125, 20));
        specialBtn = makeFantasyButton("Special", new Color(85, 50, 140));
        saveBtn = makeFantasyButton("Save", new Color(40, 75, 130));
        menuBtn = makeFantasyButton("Menu", new Color(75, 55, 35));

        discardBtn.addActionListener(e -> discardPickedCards());
        submitBtn.addActionListener(e -> submitPickedCards());
        specialBtn.addActionListener(e -> useSpecialCardNow());
        saveBtn.addActionListener(e -> saveCurrentSession());

        menuBtn.addActionListener(e -> {
            dispose();
            new MainMenuFrame(player, new PlayerManager());
        });

        buttonPanel.add(discardBtn);
        buttonPanel.add(submitBtn);
        buttonPanel.add(specialBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(menuBtn);
    }

    // Adds the resize handler.
    private void installResizeHandler() {
        mainPanel.addComponentListener(new ComponentAdapter() {
            // Handles component resize events.
            @Override
            public void componentResized(ComponentEvent e) {
                placeGameComponents();
            }
        });
    }

    // Resizes the game components.
    private void placeGameComponents() {
        int W = mainPanel.getWidth();
        int H = mainPanel.getHeight();

        int hudX = 10;
        int hudY = (int) (H * HUD_Y_RATIO);
        int hudW = W - 20;
        int hudH = (int) (H * HUD_H_RATIO);
        hudPanel.setBounds(hudX, hudY, hudW, hudH);

        int caX = (int) (W * CARD_AREA_X_RATIO);
        int caY = (int) (H * CARD_AREA_Y_RATIO);
        int caW = (int) (W * CARD_AREA_W_RATIO);
        int caH = (int) (H * CARD_AREA_H_RATIO);

        cardAreaPanel.setBounds(caX, caY, caW, caH);

        handTitle.setBounds(0, 25, caW, 34);

        int handY = 75;
        int handH = CARD_H + 35;
        handPanel.setBounds(0, handY, caW, handH);

        int rX = (int) (W * RIGHT_X_RATIO);
        int rY = (int) (H * RIGHT_Y_RATIO);
        int rW = (int) (W * RIGHT_W_RATIO);
        int rH = (int) (H * RIGHT_H_RATIO);

        rightPanel.setBounds(rX, rY, rW, rH);

        /*
         * Sağ panel içeriği:
         * px küçültüldü, cw büyüdü.
         * Game Log alanı sağa-sola genişletildi ve aşağı doğru uzatıldı.
         */
        int px = 25;
        int cw = rW - 50;
        int y = 55;

        infoTitle.setBounds(px, y, cw, 24);
        y += 30;

        specialCardLabel.setBounds(px, y, cw, 75);
        y += 95;

        selTitle.setBounds(px, y, cw, 24);
        y += 32;

        selectedCardsLabel.setBounds(px, y, cw, 80);
        y += 95;

        logTitle.setBounds(px - 5, y, cw + 10, 24);
        y += 28;

        int logX = px - 8;
        int logW = cw + 22;
        int logH = rH - y + 35;

        logScroll.setBounds(logX, y, logW, logH);

        int buttonPanelX = (int) (W * BTN_X_RATIO);
        int buttonPanelY = (int) (H * BTN_Y_RATIO);
        int buttonPanelW = (int) (W * BTN_W_RATIO);
        int buttonPanelH = 42;

        buttonPanel.setBounds(buttonPanelX, buttonPanelY, buttonPanelW, buttonPanelH);

        int totalButtonWidth = 5 * BTN_W;
        int gap = 0;

        if (buttonPanelW > totalButtonWidth) {
            gap = (buttonPanelW - totalButtonWidth) / 4;
        }

        int x = 0;
        int yButton = 4;

        discardBtn.setBounds(x + 18, yButton, BTN_W, BTN_H);
        x += BTN_W + gap;

        submitBtn.setBounds(x, yButton, BTN_W, BTN_H);
        x += BTN_W + gap;

        specialBtn.setBounds(x, yButton, BTN_W, BTN_H);
        x += BTN_W + gap;

        saveBtn.setBounds(x - 18, yButton, BTN_W, BTN_H);
        x += BTN_W + gap;

        menuBtn.setBounds(x - 35, yButton, BTN_W, BTN_H);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Creates the section title.
    private JLabel makeSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Serif", Font.BOLD, 15));
        label.setForeground(new Color(235, 180, 70));
        return label;
    }

    // Builds the hud box.
    private JPanel makeHudStatBox(JLabel valueLabel, String title) {
        JPanel box = new JPanel(new GridLayout(2, 1)) {
            // Paints this component.
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(new Color(5, 3, 2, 120));
                g2.fill(new RoundRectangle2D.Double(
                        3,
                        3,
                        getWidth() - 6,
                        getHeight() - 6,
                        10,
                        10
                ));

                g2.setColor(new Color(212, 175, 55, 95));
                g2.setStroke(new BasicStroke(1.1f));
                g2.draw(new RoundRectangle2D.Double(
                        3,
                        3,
                        getWidth() - 6,
                        getHeight() - 6,
                        10,
                        10
                ));

                g2.dispose();
            }
        };

        box.setOpaque(false);

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        titleLbl.setForeground(new Color(185, 160, 120));

        valueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        valueLabel.setForeground(new Color(235, 195, 45));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        box.add(titleLbl);
        box.add(valueLabel);

        return box;
    }

    // Creates the fantasy button.
    private JButton makeFantasyButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            // Paints this component.
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                GradientPaint gp = new GradientPaint(
                        0,
                        0,
                        baseColor.brighter(),
                        0,
                        getHeight(),
                        baseColor.darker()
                );

                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(new Color(255, 220, 95, 170));
                g2.setStroke(new BasicStroke(1.4f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 10, 10);

                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(BTN_W, BTN_H));

        return button;
    }

    // Opens the special card selection.
    private void askForSpecialCard() {
        List<SpecialCard> specialCards = new ArrayList<>();
        for (Card card : player.getDeck().getCards()) {
            if (card instanceof SpecialCard) {
                specialCards.add((SpecialCard) card);
            }
        }
        if (specialCards.isEmpty()) return;

        String[] options = new String[specialCards.size()];
        for (int i = 0; i < specialCards.size(); i++) {
            options[i] = specialCards.get(i).getType() + " - " +
                    specialCards.get(i).getEffectDescription();
        }

        String chosen = null;
        while (chosen == null) {
            chosen = (String) JOptionPane.showInputDialog(this,
                    "Select your special card:", "Special Card Selection",
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (chosen == null) {
                JOptionPane.showMessageDialog(this,
                    "You must select a special card to continue!");
            }
        }

        for (SpecialCard sc : specialCards) {
            if (chosen.startsWith(sc.getType())) {
                player.chooseSpecialCard(sc);
                player.getDeck().getCards().remove(sc);
                break;
            }
        }
    }

    // Starts the round.
    private void beginVisibleRound() {
        selectedCards.clear();

        game.beginRound();

        int roundNum = game.getCurrentRoundIndex() + 1;
        int target = game.getCurrentRound().getTargetScore();

        logger.logRoundStart(roundNum, target);

        refreshGameScreen();
        appendHandToLog();
    }

    // Updates the game interface.
    private void refreshGameScreen() {
        if (game == null) {
            return;
        }

        Round currentRound = game.getCurrentRound();

        if (currentRound == null) {
            return;
        }

        int roundNum = game.getCurrentRoundIndex() + 1;

        gameInfoLabel.setText("Active");
        roundLabel.setText(roundNum + "/4");
        targetLabel.setText(String.valueOf(currentRound.getTargetScore()));
        scoreLabel.setText(String.valueOf(player.currentTotalScore()));

        int globalLeft = player.getRemainingDiscards();
        int roundLeft = player.getMaxRoundDiscards() - player.getRoundDiscards();

        discardsLabel.setText(globalLeft + "(" + roundLeft + ")");

        if (discardBtn != null) {
            discardBtn.setEnabled(player.hasDiscardAvailable());
        }

        if (player.getDeck() != null) {
            deckCountLabel.setText(String.valueOf(player.getDeck().countCardsLeft()));
        } else {
            deckCountLabel.setText("-");
        }

        deckTypeLabel.setText(deckType != null && !deckType.equals("") ? deckType : "-");

        refreshSpecialCardInfo();
        refreshHandPanel();
        refreshSelectedCardsText();
    }

    // Updates the special card label.
    private void refreshSpecialCardInfo() {
        SpecialCard sc = player.getChosenSpecialCard();

        if (sc != null) {
            specialCardLabel.setText(
                    "<html>"
                            + "<b>" + sc.getType() + "</b><br>"
                            + wrapAsHtml(sc.getEffectDescription(), 28)
                            + (sc.isUsed() ? "<br><i>(used)</i>" : "")
                            + "</html>"
            );
        } else {
            specialCardLabel.setText("<html>Special: None</html>");
        }
    }

    // Wraps the html text.
    private String wrapAsHtml(String text, int maxChars) {
        StringBuilder result = new StringBuilder();

        String[] words = text.split(" ");
        int count = 0;

        for (String word : words) {
            if (count + word.length() > maxChars) {
                result.append("<br>");
                count = 0;
            }

            result.append(word).append(" ");
            count += word.length() + 1;
        }

        return result.toString();
    }

    // Updates the hand panel.
    private void refreshHandPanel() {
        handPanel.removeAll();

        for (Card card : player.getHand()) {
            handPanel.add(makeCardPanel(card));
        }

        handPanel.revalidate();
        handPanel.repaint();
    }

    // Updates the selected cards label.
    private void refreshSelectedCardsText() {
        if (selectedCards.isEmpty()) {
            selectedCardsLabel.setText("-");
        } else {
            StringBuilder sb = new StringBuilder("<html>");

            for (Card c : selectedCards) {
                sb.append(c.toString()).append("<br>");
            }

            sb.append("</html>");
            selectedCardsLabel.setText(sb.toString());
        }
    }

    // Logs the hand.
    private void appendHandToLog() {
        StringBuilder sb = new StringBuilder("Hand: ");

        for (Card card : player.getHand()) {
            sb.append(card.toString()).append(" ");
        }

        logger.logInitialHand(sb.toString());

        if (logArea != null) {
            logArea.append(sb + "\n");
        }
    }

    // Creates the card panel.
    private JPanel makeCardPanel(Card card) {
        boolean isSpecial = card instanceof SpecialCard;

        String tempValueStr = "";

        if (!isSpecial && card instanceof NumericCard) {
            tempValueStr = String.valueOf(((NumericCard) card).getValue());
        }

        final String valueStr = tempValueStr;

        BufferedImage cardImg = cardImageLoader.imageForCard(card.getType());

        JPanel cardPanel = new JPanel(null) {
            // Paints this component.
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                );

                if (cardImg != null) {
                    g2.drawImage(cardImg, 0, 0, getWidth(), getHeight(), null);

                    if (selectedCards.contains(card)) {
                        paintSelectedGlow(g2);
                    }

                    if (!isSpecial && !valueStr.equals("")) {
                        paintCardNumber(g2, valueStr);
                    }

                } else {
                    paintTextOnlyCard(g2, card);
                }

                g2.dispose();
            }
        };

        cardPanel.setOpaque(false);
        cardPanel.setPreferredSize(new Dimension(CARD_W, CARD_H));
        cardPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (cardImg == null) {
            addBackupCardLabels(cardPanel, card, valueStr, isSpecial);
        }

        attachCardMouseHandlers(cardPanel, card, isSpecial);

        return cardPanel;
    }

    // Draws the selected glow.
    private void paintSelectedGlow(Graphics2D g2) {
        g2.setColor(new Color(255, 215, 0, 65));
        g2.fillRoundRect(0, 0, CARD_W, CARD_H, 16, 16);

        g2.setColor(new Color(255, 215, 0));
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect(2, 2, CARD_W - 5, CARD_H - 5, 16, 16);

        g2.setColor(new Color(255, 215, 0, 45));
        g2.setStroke(new BasicStroke(8f));
        g2.drawRoundRect(6, 6, CARD_W - 13, CARD_H - 13, 16, 16);
    }

    // Draws the card value.
    private void paintCardNumber(Graphics2D g2, String valueStr) {
        g2.setFont(new Font("Serif", Font.BOLD, 42));

        FontMetrics fm = g2.getFontMetrics();

        int strW = fm.stringWidth(valueStr);
        int x = (CARD_W - strW) / 2;

        g2.setColor(new Color(0, 0, 0, 160));
        g2.drawString(valueStr, x + 2, 48);

        g2.setColor(Color.WHITE);
        g2.drawString(valueStr, x, 46);
    }

    // Draws the fallback card.
    private void paintTextOnlyCard(Graphics2D g2, Card card) {
        Color cardColor = pickCardColor(card);

        GradientPaint gp = new GradientPaint(
                0,
                0,
                new Color(45, 22, 8),
                0,
                CARD_H,
                new Color(25, 10, 4)
        );

        g2.setPaint(gp);
        g2.fill(new RoundRectangle2D.Double(0, 0, CARD_W, CARD_H, 16, 16));

        if (selectedCards.contains(card)) {
            g2.setColor(new Color(255, 215, 0));
        } else {
            g2.setColor(cardColor);
        }

        g2.setStroke(new BasicStroke(2.3f));
        g2.draw(new RoundRectangle2D.Double(1, 1, CARD_W - 3, CARD_H - 3, 16, 16));

        if (selectedCards.contains(card)) {
            g2.setColor(new Color(255, 215, 0, 55));
            g2.setStroke(new BasicStroke(8f));
            g2.draw(new RoundRectangle2D.Double(6, 6, CARD_W - 13, CARD_H - 13, 16, 16));
        }
    }

    // Adds the fallback labels.
    private void addBackupCardLabels(JPanel cardPanel, Card card, String valueStr, boolean isSpecial) {
        Color cardColor = pickCardColor(card);

        if (!isSpecial) {
            JLabel valueLabel = new JLabel(valueStr, SwingConstants.CENTER);
            valueLabel.setFont(new Font("Serif", Font.BOLD, 38));
            valueLabel.setForeground(cardColor);
            valueLabel.setBounds(0, 8, CARD_W, 42);
            cardPanel.add(valueLabel);

            JLabel symbolLabel = new JLabel(symbolForCardType(card), SwingConstants.CENTER);
            symbolLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
            symbolLabel.setForeground(cardColor);
            symbolLabel.setBounds(0, 60, CARD_W, 90);
            cardPanel.add(symbolLabel);

            JLabel nameLabel = new JLabel(card.getType(), SwingConstants.CENTER);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
            nameLabel.setForeground(new Color(230, 190, 50));
            nameLabel.setBounds(0, CARD_H - 45, CARD_W, 28);
            cardPanel.add(nameLabel);

        } else {
            JLabel nameLabel = new JLabel(
                    "<html><center>" + card.getType() + "</center></html>",
                    SwingConstants.CENTER
            );

            nameLabel.setFont(new Font("Serif", Font.BOLD, 13));
            nameLabel.setForeground(new Color(230, 190, 50));
            nameLabel.setBounds(8, 10, CARD_W - 16, 50);
            cardPanel.add(nameLabel);

            JLabel symbolLabel = new JLabel(symbolForCardType(card), SwingConstants.CENTER);
            symbolLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
            symbolLabel.setForeground(pickCardColor(card));
            symbolLabel.setBounds(0, 70, CARD_W, 95);
            cardPanel.add(symbolLabel);
        }
    }

    // Adds the card mouse listener.
    private void attachCardMouseHandlers(JPanel cardPanel, Card card, boolean isSpecial) {
        if (!isSpecial) {
            cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                // Handles mouse click events.
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    toggleCardPick(card, cardPanel);
                }
            });
        } else {
            SpecialCard sc = (SpecialCard) card;

            cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                // Handles mouse click events.
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (!sc.isUsed()) {
                        int confirm = JOptionPane.showConfirmDialog(
                                null,
                                "Use " + sc.getType() + "?\n" + sc.getEffectDescription(),
                                "Special Card",
                                JOptionPane.YES_NO_OPTION
                        );

                        if (confirm == JOptionPane.YES_OPTION) {
                            sc.applyEffect(player);
                            if (sc.isUsed() && changesSubmittedScore(sc)) {
                                player.chooseSpecialCard(sc);
                            }
                            player.getHand().remove(card);

                            logger.logSpecialCardUsage(sc.getType());

                            logArea.append("Special used: " + sc.getType() + "\n");

                            refreshGameScreen();
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Already used!");
                    }
                }
            });
        }
    }

    // Checks whether this special card should be used when scoring the submitted hand.
    private boolean changesSubmittedScore(SpecialCard sc) {
        return sc instanceof kulatro.alchemy.Catalyst
                || sc instanceof kulatro.alchemy.ElementalFusion
                || sc instanceof kulatro.alchemy.PhilosophersStone
                || sc instanceof kulatro.element.ElectronBond
                || sc instanceof kulatro.quantum.QuantumEntanglement
                || sc instanceof kulatro.quantum.Superposition;
    }

    // Toggles the card selection.
    private void toggleCardPick(Card card, JPanel cardPanel) {
        if (selectedCards.contains(card)) {
            selectedCards.remove(card);
        } else {
            selectedCards.add(card);
        }

        cardPanel.repaint();
        refreshSelectedCardsText();

        if (logArea != null) {
            logArea.append("Selected: " + card.toString() + "\n");
        }
    }

    // Handles the discard action.
    private void discardPickedCards() {
        if (selectedCards.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select cards to discard!");
            return;
        }

        if (!player.hasDiscardAvailable()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Discard limit reached!\n"
                            + "Global remaining: " + player.getRemainingDiscards() + "\n"
                            + "Round remaining: "
                            + (player.getMaxRoundDiscards() - player.getRoundDiscards())
            );
            return;
        }

        for (Card card : new ArrayList<>(selectedCards)) {
            logger.logDiscard(card.toString(), player.getRemainingDiscards() - 1);
            player.discardFromHand(card);

            if (logArea != null) {
                logArea.append("Discarded: " + card.toString() + "\n");
            }
        }

        selectedCards.clear();
        refreshGameScreen();
    }

    // Handles the submit action.
    private void submitPickedCards() {
        if (selectedCards.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least 1 card to submit!");
            return;
        }
        if (selectedCards.size() > 4) {
            JOptionPane.showMessageDialog(this, "You can submit at most 4 cards!");
            return;
        }

        List<Card> submitCards = new ArrayList<>();
        for (Card card : selectedCards) {
            if (card instanceof NumericCard) {
                submitCards.add(card);
            }
        }

        if (submitCards.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least 1 numeric card!");
            return;
        }

        int roundScore = game.scoreSubmittedHand(submitCards);

        // Philosopher's Stone - double the score
        SpecialCard sc = player.getChosenSpecialCard();
        if (sc instanceof kulatro.alchemy.PhilosophersStone) {
            kulatro.alchemy.PhilosophersStone ps = (kulatro.alchemy.PhilosophersStone) sc;
            if (ps.isActivated()) {
                int idx = game.getCurrentRoundIndex() - 1;
                int originalScore = game.getRounds()[idx].getRoundScore();
                roundScore = originalScore * 2;
                game.getRounds()[idx].setRoundScore(roundScore);
                player.restoreTotalScore(player.currentTotalScore() + roundScore -
                        originalScore);
                ps.resetActivation();
            }
        }

        int idx    = game.getCurrentRoundIndex() - 1;
        int target = game.getRounds()[idx].getTargetScore();
        boolean won = game.getRounds()[idx].didReachTarget();

        logger.logRoundResult(game.getCurrentRoundIndex(), roundScore, target, won);
        logArea.append("Score: " + roundScore + " | Target: " + target + "\n");

        JOptionPane.showMessageDialog(this,
                "Round Score: " + roundScore + "\nTarget: " + target +
                "\n" + (won ? "Round WON!" : "Round LOST!"));

        selectedCards.clear();

        if (game.isGameFinished()) {
            showFinalSummary();
        } else {
            beginVisibleRound();
        }
    }

    // Handles the special card action.
    private void useSpecialCardNow() {
        SpecialCard sc = player.getChosenSpecialCard();

        if (sc == null) {
            JOptionPane.showMessageDialog(this, "No special card selected!");
            return;
        }

        if (sc.isUsed()) {
            JOptionPane.showMessageDialog(this, "Special card already used!");
            return;
        }

        sc.applyEffect(player);

        logger.logSpecialCardUsage(sc.getType());

        if (logArea != null) {
            logArea.append("Special used: " + sc.getType() + "\n");
        }

        refreshGameScreen();
    }

    // Handles the save action.
    private void saveCurrentSession() {
        try {
            saveManager.writeGameSave(game);
            new PlayerManager().writeGameSaveResult(game);

            JOptionPane.showMessageDialog(this, "Game saved!");

            if (logArea != null) {
                logArea.append("Game saved.\n");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // Handles the game over action.
    private void showFinalSummary() {
        boolean won = game.hasPlayerWonGame();

        logger.logGameCompletion(player.getUsername(), player.currentTotalScore(), won);

        try {
            new PlayerManager().writeGameSaveResult(game);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();

        sb.append("=== GAME OVER ===\n\n");
        sb.append("Player: ").append(player.getUsername()).append("\n");
        sb.append("Deck: ").append(game.getDeckType()).append("\n");
        sb.append("Difficulty: ").append(game.getDifficulty()).append("\n\n");
        sb.append("Round Results:\n");

        Round[] rounds = game.getRounds();

        for (int i = 0; i < 4; i++) {
            sb.append("  Round ").append(i + 1)
                    .append(": Score ").append(rounds[i].getRoundScore())
                    .append(" / Target ").append(rounds[i].getTargetScore())
                    .append(rounds[i].didReachTarget() ? "  WIN" : "  LOSE")
                    .append("\n");
        }

        sb.append("\nTotal Score: ").append(player.currentTotalScore()).append("\n");
        sb.append("Rounds Won: ").append(game.countWonRounds()).append("/4\n");
        sb.append(String.format("Your Average: %.1f%n", game.calculatePlayerAverage()));
        sb.append(String.format("Target Average: %.1f%n", game.calculateTargetAverage()));
        sb.append("\n").append(won ? "YOU WIN!" : "YOU LOSE!");

        JOptionPane.showMessageDialog(
                this,
                sb.toString(),
                won ? "Victory!" : "Game Over",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();

        new MainMenuFrame(player, new PlayerManager());
    }

    // Returns the card color.
    private Color pickCardColor(Card card) {
        switch (card.getType()) {
            case "Fire":
                return new Color(220, 80, 40);
            case "Water":
                return new Color(40, 140, 220);
            case "Earth":
                return new Color(80, 160, 60);
            case "Air":
                return new Color(180, 200, 220);
            case "Hydrogen":
                return new Color(100, 180, 255);
            case "Oxygen":
                return new Color(60, 200, 180);
            case "Nitrogen":
                return new Color(80, 120, 200);
            case "Carbon Dioxide":
                return new Color(150, 100, 60);
            case "Quark":
                return new Color(180, 60, 220);
            case "Boson":
                return new Color(220, 60, 180);
            case "Gluon":
                return new Color(60, 220, 200);
            case "Photon":
                return new Color(255, 220, 60);
            default:
                return new Color(212, 175, 55);
        }
    }

    // Returns the card symbol.
    private String symbolForCardType(Card card) {
        if (card instanceof SpecialCard) {
            return symbolForSpecialCard(card.getType());
        }

        switch (card.getType()) {
            case "Fire":
                return "\uD83D\uDD25";
            case "Water":
                return "\uD83D\uDCA7";
            case "Earth":
                return "\u26F0";
            case "Air":
                return "\uD83C\uDF2A";
            case "Hydrogen":
                return "H\u2082";
            case "Oxygen":
                return "O\u2082";
            case "Nitrogen":
                return "N\u2082";
            case "Carbon Dioxide":
                return "CO\u2082";
            case "Quark":
                return "\u29C1";
            case "Boson":
                return "\u2B51";
            case "Gluon":
                return "\u223C";
            case "Photon":
                return "\u2B50";
            default:
                return "\u2605";
        }
    }

    // Returns the special card symbol.
    private String symbolForSpecialCard(String cardType) {
        switch (cardType) {
            case "Philosopher's Stone":
                return "\u2697";
            case "Transmutation":
                return "\u21BA";
            case "Elemental Fusion":
                return "\u26A1";
            case "Catalyst":
                return "\u2605";
            case "Periodic Boost":
                return "\u2191\u2191";
            case "Noble Gas":
                return "\uD83D\uDD12";
            case "Isotope Decay":
                return "\u2622";
            case "Electron Bond":
                return "\u26D3";
            case "Quantum Entanglement":
                return "\u221E";
            case "Superposition":
                return "\u2295";
            case "Gluon Bind":
                return "\uD83E\uDDF2";
            case "Photon Burst":
                return "\u2600";
            default:
                return "\u2734";
        }
    }
}
