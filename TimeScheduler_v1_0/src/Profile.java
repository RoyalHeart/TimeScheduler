package src;

// import java.util.*;
import java.awt.*;
// import java.awt.event.*;
import javax.swing.*;

public class Profile extends JPanel {
    JPanel panel = new JPanel();
    JLabel nameLabel = new JLabel("Name: ");
    JLabel emailLabel = new JLabel("Email: ");
    JLabel phoneLabel = new JLabel("Phone: ");

    Profile(User user) {
        this.setLayout(new BorderLayout());
        this.setSize(300, 200);
        this.setVisible(true);
        this.setLayout(new BorderLayout());
        JLabel label = new JLabel("Profile");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, BorderLayout.NORTH);
        JLabel name = new JLabel(user.getName());
        JLabel email = new JLabel(user.getEmail());
        JLabel phone = new JLabel(user.getPhone());
        panel.setLayout(new GridLayout(5, 2));
        panel.add(nameLabel);
        panel.add(name);
        panel.add(emailLabel);
        panel.add(email);
        panel.add(phoneLabel);
        panel.add(phone);
        this.add(panel, BorderLayout.CENTER);
    }
}
