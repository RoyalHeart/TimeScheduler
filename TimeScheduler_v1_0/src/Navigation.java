package src;

import java.awt.event.*;
import javax.swing.*;
// import java.util.*;
import java.awt.*;

public class Navigation extends JPanel {
    Navigation(JPanel panel, JFrame frame) {
        setLayout(new GridLayout(6, 1));

        JButton home = new JButton("Home");
        home.setPreferredSize(new Dimension(70, 20));
        home.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                panel.setVisible(true);
            }
        });

        JButton profile = new JButton("Profile");
        profile.setPreferredSize(new Dimension(70, 20));
        profile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                panel.setVisible(false);
                frame.add(new Profile());
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
        this.add(profile);
        this.add(logout);
        this.setVisible(true);
        this.setVisible(true);
    }
}
