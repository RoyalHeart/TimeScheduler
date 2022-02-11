package src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Container;
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
    Container panel = new Container();

    MainFrame(User user) {
        panel = this.getContentPane();
        SwingCalendar calendar = new SwingCalendar(user);
        AddEventDialog addEventDialog = new AddEventDialog(user, calendar);
        SchedulerJava.createScheduler();
        SchedulerJava.setFutureRemind(user);
        this.setIconImage(icon.getImage());
        this.setSize(700, 700);
        this.setPreferredSize(new Dimension(700, 700));
        this.setMinimumSize(new Dimension(580, 280));
        this.setLocationRelativeTo(null);
        // this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Time Scheduler");
        this.setLayout(new BorderLayout());
        this.add(new Navigation(this, user, calendar), BorderLayout.WEST);
        JButton addButton = new JButton("+");
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addEventDialog.setVisible(true);
            }

        });
        this.add(addButton, BorderLayout.EAST);
        
         // Minimize to hide on system tray code - Added by Huy to test new feature
         if(SystemTray.isSupported()){
            tray = SystemTray.getSystemTray();

            Image image = Toolkit.getDefaultToolkit().getImage("TimeScheduler_v1_0/lib/TimeSchedulerIcon.png");
            ActionListener exitListener=new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            };
            PopupMenu popup = new PopupMenu();
            MenuItem item1 = new MenuItem("Exit");
            item1.addActionListener(exitListener);
            MenuItem item2 = new MenuItem("Show");
            item2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            });
            popup.add(item2);
            popup.add(item1);
            trayIcon = new TrayIcon(image, "TISCH", popup);
            trayIcon.setImageAutoSize(true);
        }else{
            System.out.println("System Tray is not supported");
        }
        addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
                if(e.getNewState() == ICONIFIED){
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                    } catch (AWTException exc) {
                    	exc.printStackTrace();
                    }
                }
        if(e.getNewState() == 7){
            try{
            	tray.add(trayIcon);
            	setVisible(false);
            } catch(AWTException exc){
            	exc.printStackTrace();
            }
        }
        if(e.getNewState() == MAXIMIZED_BOTH){
                    tray.remove(trayIcon);
                    setVisible(true);
                }
                if(e.getNewState() == NORMAL){
                    tray.remove(trayIcon);
                    setVisible(true);
                }
            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Code for the feature "Minimize to hide on System Tray" ends here
        
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
