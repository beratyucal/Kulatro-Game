package kulatro.gui;

import kulatro.Player;
import kulatro.saveload.GameSaveManager;
import kulatro.saveload.PlayerManager;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainMenuFrame extends JFrame {

    private Player player;
    private PlayerManager playerManager;
    private GameSaveManager saveManager;
    private BufferedImage bgImage;

    // Creates a new MainMenuFrame instance.
    public MainMenuFrame(Player player, PlayerManager playerManager) {
        this.player = player;
        this.playerManager = playerManager;
        this.saveManager = new GameSaveManager();
        setTitle("Kulatro - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        loadMenuBackground();
        arrangeMenuView();
        setVisible(true);
    }

    // Loads the background.
    private void loadMenuBackground() {
        try {
            InputStream is = getClass().getResourceAsStream("/menu_bg.png");
            if (is != null) {
                bgImage = ImageIO.read(is);
            }
        } catch (Exception e) {
            bgImage = null;
        }
    }

    // Initializes the components.
    private void arrangeMenuView() {
        JPanel mainPanel = new JPanel(null) {
            // Paints this component.
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
                } else {
                    g.setColor(new Color(8, 12, 28));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        // Sol panel
        JPanel leftPanel = new JPanel(null);
        leftPanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Welcome back,", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 20));
        welcomeLabel.setForeground(new Color(212, 175, 55));

        JLabel usernameLabel = new JLabel(player.getUsername(), SwingConstants.CENTER);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        usernameLabel.setForeground(Color.WHITE);

        JLabel scoreLabel = new JLabel("Total Score: " + player.currentTotalScore(), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        scoreLabel.setForeground(new Color(180, 180, 180));

        JSeparator sep1 = new JSeparator() {
            // Paints this component.
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(212, 175, 55, 80));
                g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
            }
        };

        JButton newGameBtn   = makeMenuButton("New Game",    new Color(212, 175, 55), true,  15);
        JButton readGameSaveBtn  = makeMenuButton("Load Game",   new Color(212, 175, 55), false, 14);
        JButton selectDeckBtn= makeMenuButton("Select Deck", new Color(212, 175, 55), false, 14);

        JSeparator sep2 = new JSeparator() {
            // Paints this component.
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(212, 175, 55, 80));
                g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
            }
        };

        JButton logoutBtn = makeMenuButton("Logout", new Color(180, 60, 60), false, 14);

        newGameBtn.addActionListener(e -> openNewGameFlow());
        readGameSaveBtn.addActionListener(e -> openLoadGameFlow());
        selectDeckBtn.addActionListener(e -> openNewGameFlow());
        logoutBtn.addActionListener(e -> logoutToLogin());

        leftPanel.add(welcomeLabel);
        leftPanel.add(usernameLabel);
        leftPanel.add(scoreLabel);
        leftPanel.add(sep1);
        leftPanel.add(newGameBtn);
        leftPanel.add(readGameSaveBtn);
        leftPanel.add(selectDeckBtn);
        leftPanel.add(sep2);
        leftPanel.add(logoutBtn);

        // Sag panel - leaderboard
        JPanel rightPanel = new JPanel(new BorderLayout(0, 5));
        rightPanel.setOpaque(false);

        JLabel lbTitle = new JLabel("Leaderboard", SwingConstants.CENTER);
        lbTitle.setFont(new Font("Serif", Font.BOLD, 20));
        lbTitle.setForeground(new Color(212, 175, 55));
        lbTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        rightPanel.add(lbTitle, BorderLayout.NORTH);

        String[] columns = {"Rank", "Username", "Score", "Status"};
        Object[][] data = prepareLeaderboardTableRows();

        JTable table = new JTable(data, columns) {
            // Checks whether a table cell can be edited.
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
            // Prepares the table cell renderer.
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (row == 0) c.setBackground(new Color(180, 140, 20, 150));
                else if (row == 1) c.setBackground(new Color(150, 150, 150, 80));
                else if (row == 2) c.setBackground(new Color(150, 80, 30, 80));
                else c.setBackground(row % 2 == 0 ? new Color(20, 25, 40, 180) : new Color(15, 20, 35, 180));
                c.setForeground(Color.WHITE);
                ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return c;
            }
        };
        table.setBackground(new Color(10, 15, 30, 150));
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 2));
        table.getTableHeader().setBackground(new Color(10, 15, 30, 200));
        table.getTableHeader().setForeground(new Color(212, 175, 55));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 32));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBackground(new Color(10, 15, 30, 120));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55, 80), 1));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        mainPanel.addComponentListener(new ComponentAdapter() {
            // Handles component resize events.
            @Override
            public void componentResized(ComponentEvent e) {
                int W = mainPanel.getWidth();
                int H = mainPanel.getHeight();

                int lx = (int)(W * 0.12);
                int ly = (int)(H * 0.35);
                int lw = (int)(W * 0.20);
                int lh = (int)(H * 0.60);
                leftPanel.setBounds(lx, ly, lw, lh);

                int bw = lw - 20;
                int bx = 10;

                welcomeLabel.setBounds(bx, 15, bw, 24);
                usernameLabel.setBounds(bx, 43, bw, 22);
                scoreLabel.setBounds(bx, 70, bw, 18);
                sep1.setBounds(bx, 98, bw, 8);
                newGameBtn.setBounds(bx, 115, bw, 42);
                readGameSaveBtn.setBounds(bx, 167, bw, 38);
                selectDeckBtn.setBounds(bx, 215, bw, 38);
                sep2.setBounds(bx, 263, bw, 8);
                logoutBtn.setBounds(bx, 280, bw, 38);

                rightPanel.setBounds(
                    (int)(W * 0.53),
                    (int)(H * 0.28),
                    (int)(W * 0.42),
                    (int)(H * 0.50)
                );

                mainPanel.revalidate();
            }
        });

        setContentPane(mainPanel);
    }

    // Creates the menu button.
    private JButton makeMenuButton(String text, Color color, boolean filled, int fontSize) {
        JButton button = new JButton(text) {
            // Paints this component.
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (filled) {
                    GradientPaint gp = new GradientPaint(0, 0, color, 0, getHeight(), color.darker());
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                } else {
                    g2.setColor(color);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setForeground(filled ? Color.BLACK : color);
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        return button;
    }

    // Returns the leaderboard data.
    private Object[][] prepareLeaderboardTableRows() {
        try {
            List<String[]> history = playerManager.readLeaderboardData();
            if (history.isEmpty()) {
                List<String> usernames = playerManager.listAllUsernames();
                Object[][] data = new Object[usernames.size()][4];
                for (int i = 0; i < usernames.size(); i++) {
                    data[i][0] = "#" + (i + 1);
                    data[i][1] = usernames.get(i);
                    data[i][2] = "-";
                    data[i][3] = "No games yet";
                }
                return data;
            }
            Object[][] data = new Object[history.size()][4];
            for (int i = 0; i < history.size(); i++) {
                String[] entry = history.get(i);
                data[i][0] = "#" + (i + 1);
                data[i][1] = entry[0];  // username
                data[i][2] = entry[2];  // score
                data[i][3] = entry[3];  // WIN / LOSS / ONGOING
            }
            return data;
        } catch (IOException e) {
            return new Object[0][4];
        }
    }

    // Handles the new game action.
    private void openNewGameFlow() {
        String[] decks = {"Alchemy", "Element", "Quantum"};
        String deck = (String) JOptionPane.showInputDialog(this,
                "Select deck:", "Deck Selection",
                JOptionPane.QUESTION_MESSAGE, null, decks, decks[0]);
        if (deck == null) return;

        String[] difficulties = {"Easy", "Medium", "Hard"};
        String difficulty = (String) JOptionPane.showInputDialog(this,
                "Select difficulty:", "Difficulty",
                JOptionPane.QUESTION_MESSAGE, null, difficulties, difficulties[1]);
        if (difficulty == null) return;

        String sessionName = JOptionPane.showInputDialog(this, "Enter session name:");
        if (sessionName == null || sessionName.isEmpty()) return;

        dispose();
        new GameFrame(player, deck, difficulty, sessionName);
    }

    // Handles the load game action.
    private void openLoadGameFlow() {
        String[] sessions = saveManager.listSavedSessions(player.getUsername());
        if (sessions.length == 0) {
            JOptionPane.showMessageDialog(this, "No saved games found!");
            return;
        }
        String session = (String) JOptionPane.showInputDialog(this,
                "Select session:", "Load Game",
                JOptionPane.QUESTION_MESSAGE, null, sessions, sessions[0]);
        if (session == null) return;

        dispose();
        new GameFrame(player, session, saveManager);
    }

    // Handles the logout action.
    private void logoutToLogin() {
        dispose();
        new LoginFrame();
    }
}