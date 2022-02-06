package src;

import java.awt.event.*;
import javax.swing.*;
// import java.util.*;
import java.awt.*;

/**
 * {@code Navigation} class extends {@link JPanel} is used to navigate between
 * different screens
 * 
 * @author Tam Thai Hoang
 */
public class Navigation extends JPanel {
    private ImageIcon homeImageIcon = new ImageIcon("TimeScheduler_v1_0/src/Images/homeIconTransparent.png");
    private Image homeImage = homeImageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    private Icon homeIcon = new ImageIcon(homeImage);

    private ImageIcon profileImageIcon = new ImageIcon("TimeScheduler_v1_0/src/Images/profileIconTransparent.png");
    private Image profileImage = profileImageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    private Icon profileIcon = new ImageIcon(profileImage);

    private ImageIcon logoutImageIcon = new ImageIcon("TimeScheduler_v1_0/src/Images/logoutIconTransparent.png");
    private Image logoutImage = logoutImageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    private Icon logoutIcon = new ImageIcon(logoutImage);

    private ImageIcon settingsImageIcon = new ImageIcon("TimeScheduler_v1_0/src/Images/settingsIconTransparent.png");
    private Image settingsImage = settingsImageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    private Icon settingsIcon = new ImageIcon(settingsImage);
    private Dimension size = new Dimension(40, 30);

    Navigation(JFrame frame, User user) {
        // panel in navigation
        SwingCalendar calendar = new SwingCalendar(user);
        Profile profilePanel = new Profile(user);
        Settings settingsPanel = new Settings(user);
        // default panel
        frame.add(calendar, BorderLayout.CENTER);
        setLayout(new GridLayout(6, 1));

        JButton homeButton = new JButton(homeIcon);
        homeButton.setOpaque(false);
        homeButton.setPreferredSize(size);
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                profilePanel.setVisible(false);
                settingsPanel.setVisible(false);
                frame.remove(profilePanel);
                frame.remove(settingsPanel);
                frame.add(calendar, BorderLayout.CENTER);
                calendar.setVisible(true);
            }
        });

        JButton profileButton = new JButton(profileIcon);
        profileButton.setOpaque(false);
        profileButton.setPreferredSize(size);

        profileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                calendar.setVisible(false);
                settingsPanel.setVisible(false);
                frame.remove(calendar);
                frame.remove(settingsPanel);
                frame.add(profilePanel, BorderLayout.CENTER);
                profilePanel.setVisible(true);
            }
        });

        JButton logoutButton = new JButton(logoutIcon);
        logoutButton.setOpaque(false);
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

        JButton settingsButton = new JButton(settingsIcon);
        settingsButton.setOpaque(false);
        settingsButton.setPreferredSize(size);
        settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                calendar.setVisible(false);
                profilePanel.setVisible(false);
                frame.remove(calendar);
                frame.remove(profilePanel);
                frame.add(settingsPanel, BorderLayout.CENTER);
                settingsPanel.setVisible(true);
            }
        });

        this.add(homeButton);
        this.add(profileButton);
        this.add(logoutButton);
        this.add(settingsButton);
        this.setBackground(Color.PINK);
        this.setVisible(true);
    }
}
