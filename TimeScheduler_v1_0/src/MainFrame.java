package src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class MainFrame extends JFrame {
    ImageIcon icon = new ImageIcon("TimeScheduler_v1_0/lib/TimeSchedulerIcon.png");

    MainFrame(User user) {
        this.setIconImage(icon.getImage());
        this.setSize(700, 700);
        this.setPreferredSize(new Dimension(700, 700));
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
                new AddEventDialog(user);
            }

        });
        this.add(addButton, BorderLayout.EAST);
        this.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        try {
            SchedulerJava.createScheduler();
            Database.createConnection();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            new MainFrame(Database.getUser("admin", "D82494F05D6917BA02F7AAA29689CCB444BB73F20380876CB05D1F37537B7892"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
