package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * {@code Navigation} class extends {@link JPanel} is used to navigate between
 * different screens
 * 
 * @author Tam Thai Hoang
 */
public class Navigation extends JPanel {
    private ImageIcon homeImageIcon = new ImageIcon(getClass().getResource("/Images/homeIconTransparent.png"));
    private Image homeImage = homeImageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    private Icon homeIcon = new ImageIcon(homeImage);

    private ImageIcon profileImageIcon = new ImageIcon(getClass().getResource("/Images/profileIconTransparent.png"));
    private Image profileImage = profileImageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    private Icon profileIcon = new ImageIcon(profileImage);

    private ImageIcon logoutImageIcon = new ImageIcon(getClass().getResource("/Images/logoutIconTransparent.png"));
    private Image logoutImage = logoutImageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    private Icon logoutIcon = new ImageIcon(logoutImage);

    private ImageIcon settingsImageIcon = new ImageIcon(getClass().getResource("/Images/settingsIconTransparent.png"));
    private Image settingsImage = settingsImageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    private Icon settingsIcon = new ImageIcon(settingsImage);
    private Dimension size = new Dimension(40, 30);

    /**
     * Constructor of {@code Navigation} class
     * 
     * @param frame    the main frame of the application
     * @param user     the user of the application
     * @param calendar the calendar of the application
     */
    Navigation(JFrame frame, User user, SwingCalendar calendar) {
        // panel in navigation
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
