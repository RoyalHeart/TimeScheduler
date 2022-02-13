package src;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The settings panel of the user. It will contains the settings of the user.
 * Now it is only a test feature.
 * 
 * @author Tam Thai Hoang
 */
public class Settings extends JPanel {
    Settings(User user) {
        JLabel label = new JLabel("Settings");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.setLayout(new BorderLayout());
        this.add(label, BorderLayout.NORTH);
        JPanel systemPanel = new JPanel();
        JLabel systemLabel = new JLabel(
                "<html>This is an upcoming features for the application.<br> In the next version each user can have a setting to change personal preferences."
                        +
                        "<br>For example, the color of each event, the priority color, etc."
                        +
                        "<br>You can choose whether you want to receive email when adding, editing or deleting event."
                        + "</html>");
        Font font = new Font("Arial", Font.PLAIN, 12);
        systemLabel.setFont(font);
        systemPanel.add(systemLabel);
        this.add(systemPanel, BorderLayout.CENTER);
    }
}
