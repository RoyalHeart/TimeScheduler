package src;

// import java.util.*;
import java.awt.*;
// import java.awt.event.*;
import javax.swing.*;

public class Profile extends JPanel {
    JPanel panel = new JPanel();

    Profile() {
        this.setLayout(new BorderLayout());
        this.setSize(300, 200);
        this.setVisible(true);
        JLabel label = new JLabel("Profile");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        JButton b1 = new JButton("<-");
        JButton b2 = new JButton("->");
        setLayout(new BorderLayout());
        add(b1, BorderLayout.WEST);
        add(label, BorderLayout.CENTER);
        add(b2, BorderLayout.EAST);
        panel.setLayout(new BorderLayout());
        panel.add(b1, BorderLayout.WEST);
        panel.add(label, BorderLayout.CENTER);
        panel.add(b2, BorderLayout.EAST);
        add(panel, BorderLayout.NORTH);
    }
}
