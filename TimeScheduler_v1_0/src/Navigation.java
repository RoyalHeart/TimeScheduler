package src;

import java.awt.event.*;
import javax.swing.*;
// import java.util.*;
import java.awt.*;

public class Navigation extends JPanel {
    private ImageIcon homeImageIcon = new ImageIcon("TimeScheduler_v1_0/src/Images/homeIcon.png");
    private Image homeImage = homeImageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_AREA_AVERAGING);
    private Icon homeIcon = new ImageIcon(homeImage);

    private ImageIcon profileImageIcon = new ImageIcon("TimeScheduler_v1_0/src/Images/profileIcon.jpg");
    private Image profileImage = profileImageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    private Icon profileIcon = new ImageIcon(profileImage);

    private ImageIcon logoutImageIcon = new ImageIcon("TimeScheduler_v1_0/src/Images/logoutIcon.png");
    private Image logoutImage = logoutImageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_AREA_AVERAGING);
    private Icon logoutIcon = new ImageIcon(logoutImage);
    Dimension size = new Dimension(40, 30);

    Navigation(JFrame frame, User user) {
        // panel in navigation
        SwingCalendar calendar = new SwingCalendar(user);
        Profile profilePanel = new Profile(user);

        // default panel
        frame.add(calendar, BorderLayout.CENTER);
        setLayout(new GridLayout(6, 1));

        JButton homeButton = new JButton(homeIcon);
        homeButton.setPreferredSize(size);
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                profilePanel.setVisible(false);
                frame.remove(profilePanel);
                frame.add(calendar, BorderLayout.CENTER);
                calendar.setVisible(true);
            }
        });

        JButton profileButton = new JButton(profileIcon);
        profileButton.setPreferredSize(size);

        profileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                calendar.setVisible(false);
                frame.remove(calendar);
                frame.add(profilePanel, BorderLayout.CENTER);
                profilePanel.setVisible(true);
            }
        });

        JButton logoutButton = new JButton(logoutIcon);
        logoutButton.setPreferredSize(size);
        logoutButton.addActionListener(new ActionListener() {
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

        this.add(homeButton);
        this.add(profileButton);
        this.add(logoutButton);
        this.setVisible(true);
    }
}
