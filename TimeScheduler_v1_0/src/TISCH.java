package src;

public class TISCH {
    public static void main(String[] args) throws Exception {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // create and show the GUI
                new LoginScreen();
            }
        });
    }
}
