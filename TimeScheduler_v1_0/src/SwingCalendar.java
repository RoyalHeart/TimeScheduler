package src;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class SwingCalendar extends JPanel {

    JPanel panel; // panel to display the calendar month, year and buttons
    JLabel label; // label to display the selected month, year
    JPanel pane; // panel to display the calendar ScrollPane
    DefaultTableModel model; // table model
    JTable table; // table to display the calendar
    Calendar cal = new GregorianCalendar();
    String viewList[] = { "Month", "Week" }; // list of views
    JComboBox<String> viewComboBox = new JComboBox<>(viewList); // combo box to select the view
    static String view = "Month"; // default view
    String[] weekDate = { "1", "2", "3", "4", "5", "6", "7" };
    String[] columns = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
    static boolean isUsed = false; // flag to check if the view is used

    SwingCalendar() {

        // function for the drop down menu to change view
        viewComboBox.addActionListener(ae -> {
            if (viewComboBox.getSelectedItem().equals("Month")) {
                view = "Month";
                if (table.getColumnCount() > 7) {
                    table.removeColumn(table.getColumnModel().getColumn(0));
                    isUsed = false;
                }
                update();
            } else if (viewComboBox.getSelectedItem().equals("Week")) {
                if (model.getColumnCount() < 8) {
                    model.addColumn("Time");
                    table.moveColumn(table.getColumnCount() - 1, 0);
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    isUsed = true;
                } else if (table.getColumnCount() < 8) {
                    TableColumn time = new TableColumn();
                    time.setHeaderValue("Time");
                    time.setModelIndex(7);
                    table.addColumn(time);
                    table.moveColumn(table.getColumnCount() - 1, 0);
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    isUsed = true;
                }
                view = "Week";
                update();
            } else {
                System.out.println("Error");
            }
        });

        //
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

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(b1, BorderLayout.WEST);
        panel.add(label, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);

        columns = new String[] { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        model = new DefaultTableModel(null, columns);
        table = new JTable(model);
        table.setRowHeight(100);
        table.setFillsViewportHeight(true);
        table.setEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);
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
        JTableHeader th = table.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();

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

            for (int i = 0; i < 7; i++) {
                TableColumn tc = tcm.getColumn(i);
                tc.setHeaderValue(columns[i]);
                th.repaint();
            }

            // print the date
            int i = startDay - 1;
            for (int day = 1; day <= numberOfDays; day++) {

                model.setValueAt(day, i / 7, i % 7);
                i = i + 1;
            }
        } else if (view == "Week") {
            System.out.println("Model: " + model.getColumnCount());
            System.out.println("Table: " + table.getColumnCount());
            // set initial data
            if (model.getColumnCount() < 8) {
                model.addColumn("Time");
                table.moveColumn(table.getColumnCount() - 1, 0);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                isUsed = true;
            } else if (table.getColumnCount() < 8) {
                TableColumn time = new TableColumn();
                time.setHeaderValue("Time");
                table.addColumn(time);
                table.moveColumn(time.getModelIndex(), 0);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                isUsed = true;
            }

            String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
            int year = cal.get(Calendar.YEAR);
            int week = cal.get(Calendar.WEEK_OF_MONTH);
            label.setText(week + "/" + month + " " + year);

            int numberOfTimeSlot = 25;
            model.setRowCount(0);
            model.setRowCount(numberOfTimeSlot);

            // print the date
            int j = 0;
            for (int day = 1; day <= 7; day++) {
                TableColumn tc = tcm.getColumn(day);
                cal.set(Calendar.DAY_OF_WEEK, day);
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                // model.setValueAt(dayOfMonth, j / 7, j % 7);
                weekDate[day - 1] = String.valueOf(dayOfMonth);
                tc.setHeaderValue(columns[day - 1] + " " + weekDate[day - 1]);
                th.repaint();
                j = j + 1;
            }

            // print the time slot
            int timeSlot = 0;
            for (int i = 0; i <= numberOfTimeSlot * 8; i += 8) {
                if (timeSlot < 25) {
                    System.out.println("Time Slot: " + timeSlot);
                    System.out.println("Model: " + model.getColumnCount());
                    System.out.println("Table: " + table.getColumnCount());
                    model.setValueAt(timeSlot + ":00", i / 8, i % 8 + 7);
                    // model.setValueAt(i, i / 8, i % 8 + 7);
                    timeSlot = timeSlot + 1;
                }
            }
        }
    }
}
