package kulatro.gui;

import kulatro.Player;
import kulatro.saveload.GameLogger;
import kulatro.saveload.PlayerManager;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private PlayerManager playerManager;
    private BufferedImage bgImage;

    // Creates a new LoginFrame instance.
    public LoginFrame() {
        playerManager = new PlayerManager();
        setTitle("Kulatro - Login");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        loadLoginBackground();
        arrangeLoginView();
        setVisible(true);
    }

    // Loads the background.
    private void loadLoginBackground() {
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
    private void arrangeLoginView() {
        JPanel bgPanel = new JPanel(new GridBagLayout()) {
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
            }
        };

        JPanel loginPanel = new JPanel(new GridBagLayout()) {
            // Paints this component.
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(10, 15, 25, 180));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(new Color(212, 175, 55, 150));
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, 20, 20));
                g2.setColor(new Color(212, 175, 55, 30));
                g2.setStroke(new BasicStroke(6f));
                g2.draw(new RoundRectangle2D.Double(3, 3, getWidth() - 6, getHeight() - 6, 20, 20));
                g2.dispose();
            }
        };
        loginPanel.setOpaque(false);
        loginPanel.setPreferredSize(new Dimension(380, 430));

        GridBagConstraints lgbc = new GridBagConstraints();
        lgbc.insets = new Insets(8, 20, 8, 20);
        lgbc.fill = GridBagConstraints.HORIZONTAL;
        lgbc.gridwidth = 2;

        JLabel titleLabel = new JLabel("KULATRO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 42));
        titleLabel.setForeground(new Color(212, 175, 55));
        lgbc.gridx = 0; lgbc.gridy = 0;
        loginPanel.add(titleLabel, lgbc);

        JLabel subtitleLabel = new JLabel("A Strategy of Elements", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Serif", Font.ITALIC, 14));
        subtitleLabel.setForeground(Color.LIGHT_GRAY);
        lgbc.gridy = 1;
        loginPanel.add(subtitleLabel, lgbc);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(212, 175, 55, 120));
        lgbc.gridy = 2;
        lgbc.insets = new Insets(0, 30, 10, 30);
        loginPanel.add(sep, lgbc);

        lgbc.insets = new Insets(6, 20, 6, 20);

        lgbc.gridy = 3;
        loginPanel.add(makeInputFieldRow(usernameField = new JTextField(15), "Username"), lgbc);

        lgbc.gridy = 4;
        loginPanel.add(makeInputFieldRow(passwordField = new JPasswordField(15), "Password"), lgbc);

        JButton loginButton = makePrimaryButton("Login");
        lgbc.gridy = 5;
        lgbc.insets = new Insets(15, 20, 4, 20);
        loginPanel.add(loginButton, lgbc);

        JButton registerNewPlayerButton = makeOutlineButton("Register");
        lgbc.gridy = 6;
        lgbc.insets = new Insets(4, 20, 4, 20);
        loginPanel.add(registerNewPlayerButton, lgbc);

        JButton rulesButton = makeOutlineButton("How to Play");
        lgbc.gridy = 7;
        lgbc.insets = new Insets(4, 20, 15, 20);
        loginPanel.add(rulesButton, lgbc);

        loginButton.addActionListener(e -> tryLogin());
        registerNewPlayerButton.addActionListener(e -> tryRegister());
        rulesButton.addActionListener(e -> new RulesFrame());

        GridBagConstraints gbc = new GridBagConstraints();
        bgPanel.add(loginPanel, gbc);
        add(bgPanel);
    }

    // Creates the styled field.
    private JPanel makeInputFieldRow(JTextField field, String placeholder) {
        field.setBackground(new Color(0, 0, 0, 120));
        field.setForeground(Color.WHITE);
        field.setCaretColor(new Color(212, 175, 55));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(212, 175, 55, 150), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setOpaque(true);

        field.addFocusListener(new FocusAdapter() {
            // Handles focus gain events.
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(212, 175, 55), 2),
                    BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            // Handles focus loss events.
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(212, 175, 55, 150), 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });

        JPanel wrapper = new JPanel(new BorderLayout(5, 0));
        wrapper.setOpaque(false);
        JLabel label = new JLabel(placeholder + ":");
        label.setForeground(new Color(180, 180, 180));
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setPreferredSize(new Dimension(75, 20));
        wrapper.add(label, BorderLayout.WEST);
        wrapper.add(field, BorderLayout.CENTER);
        return wrapper;
    }

    // Creates the primary button.
    private JButton makePrimaryButton(String text) {
        JButton button = new JButton(text) {
            // Paints this component.
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(212, 175, 55),
                        0, getHeight(), new Color(159, 122, 28));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        return button;
    }

    // Creates the outline button.
    private JButton makeOutlineButton(String text) {
        JButton button = new JButton(text) {
            // Paints this component.
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 0));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(212, 175, 55));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setForeground(new Color(212, 175, 55));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    // Handles the login action.
    private void tryLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password!");
            return;
        }

        try {
            Player player = playerManager.login(username, password);
            if (player != null) {
                GameLogger.getInstance().log("LOGIN: " + username);
                dispose();
                new MainMenuFrame(player, playerManager);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // Handles the registerNewPlayer action.
    private void tryRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password!");
            return;
        }

        try {
            boolean success = playerManager.registerNewPlayer(username, password);
            if (success) {
                JOptionPane.showMessageDialog(this, "Registration successful! Please login.");
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists!");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}