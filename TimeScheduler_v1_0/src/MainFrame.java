package src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class MainFrame extends JFrame {
    ImageIcon icon = new ImageIcon("TimeScheduler_v1_0/lib/TimeSchedulerIcon.png");

    MainFrame(User user) {
        SchedulerJava.createScheduler();
        SchedulerJava.setFutureRemind(user);
        this.setIconImage(icon.getImage());
        this.setSize(700, 700);
        this.setPreferredSize(new Dimension(700, 700));
        this.setMinimumSize(new Dimension(580, 280));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Time Scheduler");
        this.setLayout(new BorderLayout());
        this.add(new Navigation(this, user), BorderLayout.WEST);
        JButton addButton = new JButton("+");
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                new AddEventDialog(user);
            }

        });
        this.add(addButton, BorderLayout.EAST);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int respone = JOptionPane.showOptionDialog(null,
                        "Are you sure you want to exit?\nIf you exit no reminder will be sent", "Exit",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (respone == JOptionPane.YES_OPTION) {
                    Database.closeConnection();
                    SchedulerJava.closeScheduler();
                    System.exit(0);
                } else if (respone == JOptionPane.NO_OPTION) {
                    // do nothing
                } else {
                    // do nothing
                }
            }
        });
        this.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        try {
            Database.createConnection();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new MainFrame(
                    Database.getUser("admin", "admin"));
            // SchedulerJava.createScheduler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
