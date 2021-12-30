package src;

import java.awt.event.*;
import javax.swing.*;
// import java.util.*;
import java.awt.*;

public class Navigation extends JPanel {

    Navigation(JFrame frame, User user) {
        // panel in navigation
        SwingCalendar calendar = new SwingCalendar();
        Profile profilePanel = new Profile(user);

        // default panel
        frame.add(calendar, BorderLayout.CENTER);
        setLayout(new GridLayout(6, 1));

        JButton home = new JButton("Home");
        home.setPreferredSize(new Dimension(70, 20));
        home.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                profilePanel.setVisible(false);
                frame.remove(profilePanel);
                frame.add(calendar, BorderLayout.CENTER);
                calendar.setVisible(true);
            }
        });

        JButton profileButton = new JButton("Profile");
        profileButton.setPreferredSize(new Dimension(70, 20));

        profileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                calendar.setVisible(false);
                frame.remove(calendar);
                frame.add(profilePanel, BorderLayout.CENTER);
                profilePanel.setVisible(true);
            }
        });

        JButton logout = new JButton("Logout");
        logout.setPreferredSize(new Dimension(70, 20));
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int respone = JOptionPane.showOptionDialog(null, "Are you sure you want to logout?", "Logout",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (respone == JOptionPane.YES_OPTION) {
                    frame.dispose();
                    new LoginScreen();
                } else if (respone == JOptionPane.NO_OPTION) {
                    // do nothing
                } else {
                    // do nothing
                }
            }
        });

        this.add(home);
        this.add(profileButton);
        this.add(logout);
        this.setVisible(true);
        this.setVisible(true);
    }
}
