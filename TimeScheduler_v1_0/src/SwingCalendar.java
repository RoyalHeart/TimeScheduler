package src;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;

public class SwingCalendar extends JPanel {
    User user;
    JPanel panel; // panel to display the calendar month, year and buttons
    JLabel label; // label to display the selected month, year
    ArrayList<List<MonthCell>> data = new ArrayList<List<MonthCell>>();
    MyClassTableModel model = new MyClassTableModel(data); // table model
    JTable table = new JTable(model); // table to display the calendar
    JScrollPane pane = new JScrollPane(table); // panel to display the calendar ScrollPane
    Calendar cal = new GregorianCalendar();
    Date today = cal.getTime();
    String viewList[] = { "Month", "Week" }; // list of views
    JComboBox<String> viewComboBox = new JComboBox<>(viewList); // combo box to select the view
    static String view = "Month"; // default view
    String[] weekDate = { "", "", "", "", "", "", "" };
    String[] columns = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
    static boolean isUsed = false; // flag to check if the view is used

    SwingCalendar(User user) {
        this.user = user;
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
        System.out.println(today);
        // initialize the panel
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

        // int startDay = cal.get(Calendar.DAY_OF_WEEK);
        // int numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
        // int day = 1;
        // // data to test the month view of events
        // String[] titles = { "title", "title" };
        // String[] titlesEmpty = {};
        // String[] times = { "time", "time" };
        // String[] timesEmpty = {};
        // for (int i = 0; i < weeks; i++) {
        // List<MonthCell> list = new ArrayList<MonthCell>();
        // for (int j = 0; j < 7; j++) {
        // if (i == 0 && j < startDay - 1) {
        // list.add(new MonthCell("", timesEmpty, titlesEmpty));
        // } else if (day > numberOfDays) {
        // list.add(new MonthCell("", timesEmpty, titlesEmpty));
        // } else {
        // list.add(new MonthCell(String.valueOf(day), times, titles));
        // day++;
        // }
        // }
        // data.add(list);
        // }
        // model = new MyClassTableModel(data);
        // table = new JTable(model);
        // table.setDefaultRenderer(MonthCell.class, new MyClassCellRenderer());// show
        // the table
        // table.setRowHeight(100);
        // table.setFillsViewportHeight(true);
        // table.setEnabled(false);
        // table.getTableHeader().setReorderingAllowed(false);
        // // table.setDefaultRenderer(MonthCell.class, new MyClassCellRenderer());
        // // table.setDefaultEditor(EventCell.class, new MyClassCellEditor());

        // pane = new JScrollPane(table);

        // // panel is the part with week/month and year
        // // pane is the date table/time slot table
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
            int day = 1;

            data = new ArrayList<List<MonthCell>>();
            for (int i = 0; i < weeks; i++) {
                List<MonthCell> list = new ArrayList<MonthCell>();
                for (int j = 0; j < 7; j++) {
                    if (i == 0 && j < startDay - 1) {
                        list.add(new MonthCell());
                    } else if (day > numberOfDays) {
                        list.add(new MonthCell());
                    } else {
                        list.add(new MonthCell(String.valueOf(day)));
                        day++;
                    }
                }
                data.add(list);
            }
            data = ShowEvents.updateDataEvent(data, cal, user);
            this.remove(pane);
            // model = new MyClassTableModel(data);
            model.update(data);
            table = new JTable(model);
            table.setDefaultRenderer(MonthCell.class, new MyClassCellRenderer());//
            // render the table cell
            table.setRowHeight(100);
            table.setFillsViewportHeight(true);
            table.setEnabled(false);
            table.getTableHeader().setReorderingAllowed(false);
            pane = new JScrollPane(table);
            th = table.getTableHeader();
            tcm = th.getColumnModel();
            this.add(pane, BorderLayout.CENTER);

            // the table
            // set the column header
            for (int i = 0; i < 7; i++) {
                TableColumn tc = tcm.getColumn(i);
                tc.setHeaderValue(columns[i]);
                th.repaint();
            }

            // } else if (view == "Week") {
            // this.remove(pane);
            // DefaultTableModel modelWeek = new DefaultTableModel(null, columns);
            // table = new JTable(modelWeek);
            // table.setRowHeight(100);
            // table.setDefaultRenderer(MonthCell.class, new MyClassCellRenderer());// show

            // // set initial data, add the time slot
            // if (model.getColumnCount() < 8) {
            // model.addColumn("Time");
            // table.moveColumn(table.getColumnCount() - 1, 0);
            // cal.set(Calendar.DAY_OF_MONTH, 1);
            // isUsed = true;
            // } else if (table.getColumnCount() < 8) {
            // TableColumn time = new TableColumn();
            // time.setHeaderValue("Time");
            // table.addColumn(time);
            // table.moveColumn(time.getModelIndex(), 0);
            // cal.set(Calendar.DAY_OF_MONTH, 1);
            // isUsed = true;
            // }

            // String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
            // int year = cal.get(Calendar.YEAR);
            // int week = cal.get(Calendar.WEEK_OF_MONTH);
            // label.setText(week + "/" + month + " " + year);

            // int numberOfTimeSlot = 25; // number of time slot 0:00 - 24:00
            // model.setRowCount(0);
            // // model.data = new ArrayList<List<MonthCell>>();

            // model.setRowCount(numberOfTimeSlot);

            // // print the date
            // int j = 0;
            // for (int day = 1; day <= 7; day++) {
            // TableColumn tc = tcm.getColumn(day);
            // cal.set(Calendar.DAY_OF_WEEK, day);
            // int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            // weekDate[day - 1] = String.valueOf(dayOfMonth);
            // tc.setHeaderValue(columns[day - 1] + " " + weekDate[day - 1]);
            // th.repaint();
            // j = j + 1;
            // }

            // // print the time slot
            // int timeSlot = 0;
            // for (int i = 0; i <= numberOfTimeSlot * 8; i += 8) {
            // if (timeSlot < 25) {
            // model.setValueAt(new MonthCell(String.valueOf(timeSlot)), i / 8, i % 8 + 7);
            // // model.setValueAt(i, i / 8, i % 8 + 7);
            // timeSlot = timeSlot + 1;
            // }
            // }
            // pane = new JScrollPane(table);
            // this.add(pane, BorderLayout.CENTER);
            // this.setVisible(true);
            // }
            // }
        }
    }
}
