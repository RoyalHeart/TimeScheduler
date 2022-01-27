package src;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Make a {@link JFileChooser} to choose a location to save export {@link PDF}.
 * 
 * @author Tam Thai Hoang
 *
 */
public class FileChoose extends JPanel implements ActionListener {
    JFileChooser chooser;
    String choosertitle;
    User user;
    Calendar cal;
    String view = "";
    JFrame frame = new JFrame(); // just to get the icon of the frame to set to the chooser
    ImageIcon icon = new ImageIcon("TimeScheduler_v1_0/lib/TimeSchedulerIcon.png");

    public FileChoose(User user, Calendar cal, String view) {
        this.user = user;
        this.cal = cal;
        this.view = view;
        frame.setIconImage(icon.getImage());
    }

    public void actionPerformed(ActionEvent e) {
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Export this week to a PDF file");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        chooser.setAcceptAllFileFilterUsed(false);
        //
        if (chooser.showDialog(frame, "Export to PDF") == JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): "
                    + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : "
                    + chooser.getSelectedFile());
            PDF.filePath = chooser.getSelectedFile().toString() + "\\";
            System.out.println("File path: " + PDF.filePath);
            if (view == "Week") {
                PDF.exportWeeklyEvents(user, cal);
            } else if (view == "Month") {
                PDF.exportMonthlyEvents(user, cal);
            }
        } else {
            System.out.println("No Selection ");
        }
    }
}
