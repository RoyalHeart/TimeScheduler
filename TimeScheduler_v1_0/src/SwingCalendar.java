package src;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class SwingCalendar extends JPanel {

    DefaultTableModel model;
    Calendar cal = new GregorianCalendar();
    JLabel label;

    // month view
    SwingCalendar() {

        // this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this.setTitle("Swing Calandar");
        this.setSize(300, 200);
        this.setLayout(new BorderLayout());
        // this.setLocationRelativeTo(null);
        this.setVisible(true);

        label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JButton b1 = new JButton("<-");
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cal.add(Calendar.MONTH, -1);
                updateMonth();
            }
        });

        JButton b2 = new JButton("->");
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cal.add(Calendar.MONTH, +1);
                updateMonth();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(b1, BorderLayout.WEST);
        panel.add(label, BorderLayout.CENTER);
        panel.add(b2, BorderLayout.EAST);

        String[] columns = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        model = new DefaultTableModel(null, columns);
        JTable table = new JTable(model);
        table.setRowHeight(100);
        table.setFillsViewportHeight(true);
        table.setEnabled(false);
        // JScrollPane pane = new JScrollPane(table);
        TableColumn column = null;
        for (int i = 0; i < columns.length; i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(200); // third column is bigger
        }

        // JPanel navigation = new Navigation();
        // panel is the part with month and year
        // pane is the date table
        // this.setPreferredSize(new Dimension(600, 700));
        // this.pack();
        this.add(panel, BorderLayout.NORTH);
        this.add(table, BorderLayout.CENTER);
        this.updateMonth();

    }

    void updateMonth() {
        cal.set(Calendar.DAY_OF_MONTH, 1);

        String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        int year = cal.get(Calendar.YEAR);
        label.setText(month + " " + year);

        int startDay = cal.get(Calendar.DAY_OF_WEEK);
        int numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);

        model.setRowCount(0);
        model.setRowCount(weeks);

        int i = startDay - 1;
        for (int day = 1; day <= numberOfDays; day++) {
            model.setValueAt(day, i / 7, i % 7);
            i = i + 1;
        }
    }

    public static void main(String[] arguments) {
        // JFrame.setDefaultLookAndFeelDecorated(true);
        new SwingCalendar();
    }

}