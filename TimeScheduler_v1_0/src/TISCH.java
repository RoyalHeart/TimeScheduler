package src;

import javax.swing.UIManager;

import src.database.Database;

// import src.database.Database;

/**
 * Main class for the Time Scheduler.
 *
 * @author Tam Thai Hoang
 */
public class TISCH {
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // create and show the GUI
                Database.createConnection();
                new LoginScreen();
            }
        });
    }
}
