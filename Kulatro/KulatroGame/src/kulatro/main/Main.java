package kulatro.main;

import kulatro.gui.LoginFrame;
import kulatro.saveload.SpecialCardLoader;
import javax.swing.SwingUtilities;

public class Main {

    // Starts the application.
    public static void main(String[] args) {
        try {
            new SpecialCardLoader();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}