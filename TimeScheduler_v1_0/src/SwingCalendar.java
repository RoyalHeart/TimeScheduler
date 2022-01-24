package src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class SwingCalendar extends JPanel {
    User user;
    JPanel panel; // panel to display the calendar month, year and buttons
    JLabel label; // label to display the selected month, year
    // month data
    ArrayList<List<MonthCell>> data = new ArrayList<List<MonthCell>>();
    MyClassTableModel model = new MyClassTableModel(data); // month table model
    JTable table = new JTable(model); // table to display the calendar
    // week data
    ArrayList<WeekCell> weekData = new ArrayList<WeekCell>();
    WeekTableModel weekModel = new WeekTableModel(weekData); // week table model

    JScrollPane pane = new JScrollPane(table); // panel to display the calendar ScrollPane
    Calendar cal = new GregorianCalendar();
    Date today = cal.getTime();
    String viewList[] = { "Month", "Week" }; // list of views
    JComboBox<String> viewComboBox = new JComboBox<>(viewList); // combo box to select the view
    static String view = "Month"; // default view
    String[] weekDate = { "", "", "", "", "", "", "" };
    String[] columns = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

    SwingCalendar(User user) {
        this.user = user;
        // function for the drop down menu to change view
        viewComboBox.setPreferredSize(new Dimension(70, 20));
        viewComboBox.addActionListener(ae -> {
            if (viewComboBox.getSelectedItem().equals("Month")) {
                view = "Month";
                update();
            } else if (viewComboBox.getSelectedItem().equals("Week")) {
                view = "Week";
                cal.set(Calendar.DAY_OF_WEEK, 0);
                update();
            } else {
                System.out.println("Error");
            }
        });
        // initialize the panel
        this.setSize(400, 200);
        this.setPreferredSize(new Dimension(400, 200));
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

        JButton todayButton = new JButton("Today");
        todayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cal.setTime(today);
                update();
            }
        });

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout());
        leftPanel.add(b1);
        leftPanel.add(todayButton);

        JButton b2 = new JButton("->");
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                update(1);
                update();
            }
        });

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout());
        rightPanel.setPreferredSize(new Dimension(150, 40));
        rightPanel.add(viewComboBox);
        rightPanel.add(b2);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(label, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);

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
                System.out.println("WEEK of month: " + cal.get(Calendar.WEEK_OF_MONTH));
                cal.add(Calendar.WEEK_OF_MONTH, -1);
                System.out.println("WEEK of month: " + cal.get(Calendar.WEEK_OF_MONTH));
            } else {
                cal.add(Calendar.WEEK_OF_MONTH, +1);
            }
        }
    }

    // update the date table
    void update() {

        if (view == "Month") {
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
            data = ShowEvents.updateMonthDataEvent(data, cal, user);
            this.remove(pane);
            // model = new MyClassTableModel(data);
            model.update(data);
            table = new JTable(model);
            table.setDefaultRenderer(MonthCell.class, new MyClassCellRenderer());// render the table cell
            table.setDefaultEditor(MonthCell.class, new MyClassCellEditor());
            table.setRowHeight(100);
            table.setFillsViewportHeight(true);
            table.setEnabled(true);
            table.getTableHeader().setReorderingAllowed(false);
            pane = new JScrollPane(table);
            pane.setVisible(true);
            this.add(pane, BorderLayout.CENTER);

            // // the table
            // // set the column header
            // for (int i = 0; i < 7; i++) {
            // TableColumn tc = tcm.getColumn(i);
            // tc.setHeaderValue(columns[i]);
            // th.repaint();
            // }

        } else if (view == "Week") {
            this.remove(pane);
            int week = cal.get(Calendar.WEEK_OF_MONTH);
            String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
            int year = cal.get(Calendar.YEAR);
            label.setText(week + "/" + month + " " + year);
            weekData = ShowEvents.updateWeekDataEvent(weekData, cal, user);
            weekModel.update(weekData);
            table = new JTable(weekModel);
            table.setRowHeight(24 * 60 * 2);
            table.setPreferredSize(new Dimension(1000, 24 * 60 * 2));
            table.setSize(1000, 24 * 60 * 2);
            table.setDefaultRenderer(WeekCell.class, new WeekCellRenderer());// render the table cell
            table.setDefaultEditor(WeekCell.class, new WeekCellEditor());
            table.setEnabled(true);
            table.getTableHeader().setReorderingAllowed(false);
            pane = new JScrollPane(table);
            pane.setViewportView(table);
            pane.getVerticalScrollBar().setUnitIncrement(2 * 15);
            pane.getVerticalScrollBar().setValue(6 * 60 * 2);
            this.add(pane, BorderLayout.CENTER);
            JPanel timeline = new JPanel();
            JViewport viewport = new JViewport();
            timeline.setLayout(new GridLayout(24, 1));
            timeline.setPreferredSize(new Dimension(30, 24 * 60 * 2));
            timeline.setSize(30, 24 * 60 * 2);
            for (int i = 0; i < 24; i++) {
                JLabel label = new JLabel(i + ":00");
                label.setBounds(0, 0, 20, 60 * 2);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setVerticalAlignment(JLabel.TOP);
                timeline.add(label);
            }
            viewport.setView(timeline);
            pane.setRowHeader(viewport);

            JTableHeader th = table.getTableHeader();
            TableColumnModel tcm = th.getColumnModel();

            // change table header to: date of month + day of week
            for (int day = 0; day < 7; day++) {
                TableColumn tc = tcm.getColumn(day);
                cal.set(Calendar.DAY_OF_WEEK, day + 1);
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                weekDate[day] = String.valueOf(dayOfMonth);
                tc.setHeaderValue(columns[day] + " " + weekDate[day]);
                th.repaint();
            }
        }
    }
}
