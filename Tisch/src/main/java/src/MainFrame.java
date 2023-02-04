package src;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
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

import src.database.Database;

/**
 * Main frame of user interface.
 * 
 * @author Tam Thai Hoang
 * @author Huy Truong Quang
 */
public class MainFrame extends JFrame {
    ImageIcon icon = new ImageIcon(getClass().getResource("/TimeSchedulerIcon.png"));
    Container panel = new Container();
    SystemTray tray;
    TrayIcon trayIcon;

    /**
     * Constructor of MainFrame.
     * 
     * @param user the {@code user} who is logged in.
     */
    MainFrame(User user) {
        panel = this.getContentPane();
        SwingCalendar calendar = new SwingCalendar(user);
        AddEventDialog addEventDialog = new AddEventDialog(user, calendar);
        SchedulerJava.createScheduler();
        SchedulerJava.setFutureRemind(user);
        SchedulerJava.refreshConnection();
        this.setIconImage(icon.getImage());
        this.setSize(700, 700);
        this.setPreferredSize(new Dimension(700, 700));
        this.setMinimumSize(new Dimension(580, 280));
        this.setLocationRelativeTo(null);
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

        if (SystemTray.isSupported()) {
            tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("/TimeSchedulerIcon.png");

            ActionListener exitListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            };
            ActionListener showListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tray.remove(trayIcon);
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            };
            PopupMenu popup = new PopupMenu();
            MenuItem item1 = new MenuItem("Exit");
            item1.addActionListener(exitListener);
            MenuItem item2 = new MenuItem("Show");
            item2.addActionListener(showListener);
            popup.add(item2);
            popup.add(item1);
            trayIcon = new TrayIcon(image, "TISCH", popup);
            trayIcon.setImageAutoSize(true);
        } else {
            System.out.println("System Tray is not supported");
        }

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String[] options = { "Yes", "Hide on System Task" };
                int respone = JOptionPane.showOptionDialog(null,
                        "Do you want to exit?\nIf you exit no reminder will be sent", "Exit",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (respone == 0) {
                    System.out.println("0");
                    Database.closeConnection();
                    SchedulerJava.closeScheduler();
                    System.exit(0);

                } else if (respone == 1) {
                    try {
                        tray.add(trayIcon);
                        setVisible(false);
                    } catch (AWTException exc) {
                        exc.printStackTrace();
                    }
                } else {
                    // do nothing
                }
            }
        });
        this.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        try {
            System.out.println("11a49523df95839188846cd5a5f87100473e51d84745072b6043375409d95459".toUpperCase());
            Database.createConnection();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new MainFrame(
                    Database.getUser("admin", "admin"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
