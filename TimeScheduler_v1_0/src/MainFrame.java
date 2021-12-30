package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class MainFrame extends JFrame {
    ImageIcon icon = new ImageIcon("ProgramJava/Project/TimeScheduler_v1_0/libTimeSchedulerIcon.png");

    MainFrame() {
        this.setIconImage(icon.getImage());
        JPanel calendar = new SwingCalendar();
        this.setSize(600, 700);
        this.setPreferredSize(new Dimension(600, 700));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Time Scheduler");
        this.setLayout(new BorderLayout());
        this.add(calendar, BorderLayout.CENTER);
        this.add(new Navigation(calendar, this), BorderLayout.WEST);
        JButton addButton = new JButton("+");
        addButton.addActionListener(new ActionListener()
        {

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
        new MainFrame();
    }
}
