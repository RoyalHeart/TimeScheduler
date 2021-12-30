package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MainFrame extends JFrame {
    ImageIcon icon = new ImageIcon("TimeScheduler_v1_0/lib/TimeSchedulerIcon.png");

    MainFrame(User user) {
        this.setIconImage(icon.getImage());
        this.setSize(600, 700);
        this.setPreferredSize(new Dimension(600, 700));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Time Scheduler");
        this.setLayout(new BorderLayout());
        this.add(new Navigation(this, user), BorderLayout.WEST);
        JButton addButton = new JButton("+");
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                new AddEventDialog();
            }

        });
        this.add(addButton, BorderLayout.EAST);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Database.createConnection();
        new MainFrame(Database.getUser("abc", Hash.hashPassword("abc").toUpperCase()));
    }
}
