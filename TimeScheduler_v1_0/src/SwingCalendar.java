package src;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class SwingCalendar extends JPanel {

    DefaultTableModel model;
    JTable table;
    Calendar cal = new GregorianCalendar();
    JLabel label;
    String viewList[] = { "Month", "Week" };
    JComboBox<String> viewComboBox = new JComboBox<>(viewList);
    static String view = "Month";
    JPanel panel;
    JPanel pane;
    static boolean isUsed = false;

    SwingCalendar() {
        viewComboBox.addActionListener(ae -> {
            if (viewComboBox.getSelectedItem().equals("Month")) {
                view = "Month";
                update();
                // System.out.println("Month view");
            } else if (viewComboBox.getSelectedItem().equals("Week")) {
                view = "Week";
                update();
                // System.out.println("Week view");
            } else {
                view = "Month";
            }
        });
        this.setSize(300, 200);
        this.setLayout(new BorderLayout());
        this.setVisible(true);

        label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JButton b1 = new JButton("<-");
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // cal.add(Calendar.MONTH, -1);
                update(0);
                update();
            }
        });

        JButton b2 = new JButton("->");
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // cal.add(Calendar.MONTH, +1);
                update(1);
                update();
            }
        });

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout());
        rightPanel.add(viewComboBox);
        rightPanel.add(b2);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(b1, BorderLayout.WEST);
        panel.add(label, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);

        String[] columns = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        model = new DefaultTableModel(null, columns);
        table = new JTable(model);
        table.setRowHeight(100);
        table.setFillsViewportHeight(true);
        table.setEnabled(false);
        JScrollPane pane = new JScrollPane(table);

        // panel is the part with week/month and year
        // pane is the date table/time slot table
        this.add(panel, BorderLayout.NORTH);
        this.add(pane, BorderLayout.CENTER);
        this.update();
    }

    // update when the left or right button is clicked
    void update(int side) {
        if (view == "Month") {
            if (side == 0) {
                cal.add(Calendar.MONTH, -1);
            } else {
                cal.add(Calendar.MONTH, +1);
            }
        } else if (view == "Week") {
            if (side == 0) {
                cal.add(Calendar.WEEK_OF_MONTH, -1);
            } else {
                cal.add(Calendar.WEEK_OF_MONTH, +1);
            }
        }
    }

    // update the date table
    void update() {
        if (view == "Month") {
            cal.set(Calendar.DAY_OF_MONTH, 1);
            String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
            int year = cal.get(Calendar.YEAR);
            label.setText(month + " " + year);

            int startDay = cal.get(Calendar.DAY_OF_WEEK);
            int numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);

            model.setRowCount(0);
            model.setRowCount(weeks);

            // print the date
            int i = startDay - 1;
            for (int day = 1; day <= numberOfDays; day++) {
                model.setValueAt(day, i / 7, i % 7);
                i = i + 1;
            }
        } else if (view == "Week") {

            // set initial data
            if (isUsed == false) {
                model.addColumn("Time");
                table.moveColumn(table.getColumnCount() - 1, 0);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                isUsed = true;
            }

            String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
            int year = cal.get(Calendar.YEAR);
            int week = cal.get(Calendar.WEEK_OF_MONTH);
            label.setText(week + "/" + month + " " + year);

            int numberOfTimeSlot = 25 + 1;
            model.setRowCount(0);
            model.setRowCount(numberOfTimeSlot);

            // print the time slot
            int i = 0;
            for (int timeSlot = 8; timeSlot <= numberOfTimeSlot * 8; timeSlot += 8) {
                if (i < 25) {
                    // System.out.println(timeSlot + " " + timeSlot / 8 + " " + timeSlot % 8 + " " +
                    // i);
                    // System.out.println(i);
                    model.setValueAt(i, timeSlot / 8, timeSlot % 8 + 7);
                    i = i + 1;
                }
            }

            // print the date
            int j = 0;
            for (int day = 1; day <= 7; day++) {
                cal.set(Calendar.DAY_OF_WEEK, day);
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                model.setValueAt(dayOfMonth, j / 7, j % 7);
                j = j + 1;
            }
        }
    }
}
