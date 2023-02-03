package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.ImageIcon;
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

/**
 * <p>
 * {@code SwingCalendar} extends {@code JPanel}, and is used to display the
 * calendar, which can be shown in a {@code JFrame}.
 * </p>
 * <p>
 * This {@code SwingCalendar} has two different view:
 * </p>
 * <ul>
 * <li>{@code Month}</li>
 * <li>{@code Week}</li>
 * </ul>
 * 
 * @author Tam Thai Hoang
 */
public class SwingCalendar extends JPanel {
    private User user;
    private JPanel panel; // panel to display the calendar month, year and buttons
    private JLabel label = new JLabel(); // label to display the selected month, year
    /**
     * {@code LEFT} is the int left for the left button to go to the previous
     * month/week.
     */
    private static final int LEFT = 0;

    /**
     * {@code RIGHT} is the int right for the right button to go to the next
     * month/week.
     */
    private static final int RIGHT = 1;

    /**
     * {@code monthData} is the {@code ArrayList<List>} of {@link MonthCell}s to be
     * displayed in the calendar.
     */
    private ArrayList<List<MonthCell>> monthData = new ArrayList<List<MonthCell>>();

    /**
     * {@code monthModel} is the {@link MonthTableModel} to display month table.
     */
    private MonthTableModel monthModel = new MonthTableModel(monthData);

    /**
     * {@code table} is the {@link JTable} to show the calendar.
     */
    private JTable table = new JTable(monthModel);

    /**
     * {@code monthData} is the {@code ArrayList<List>} of {@link MonthCell}s to be
     * displayed in the calendar.
     */
    private ArrayList<WeekCell> weekData = new ArrayList<WeekCell>();

    /**
     * {@code weekModel} is the {@link WeekTableModel} to display week table.
     */
    private WeekTableModel weekModel = new WeekTableModel(weekData);

    /**
     * {@code pane} is the {@link JScrollPane} view the calendar with a scroll bar.
     */
    private JScrollPane pane = new JScrollPane(table);

    /**
     * {@code cal} is the {@link Calendar} to get calendar information.
     */
    Calendar cal = new GregorianCalendar();

    /**
     * {@code today} is the current {@link Date} at runtime.
     */
    private Date today = new GregorianCalendar().getTime();

    private JButton todayButton = new JButton("Today");

    private ImageIcon exportImageIcon = new ImageIcon("TimeScheduler_v1_0/src/Images/exportIcon.png");
    private Image exportImage = exportImageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    private Icon exportIcon = new ImageIcon(exportImage);

    /**
     * {@code exportButton} is the {@link JButton} to export the calendar to a
     * PDF file.
     */
    private JButton exportButton = new JButton(exportIcon);

    private String viewList[] = { "Month", "Week" }; // list of views

    /**
     * {@code viewComboBox} is the {@link JComboBox} to select the view of
     * {@link SwingCalendar}.
     */
    private JComboBox<String> viewComboBox = new JComboBox<>(viewList); // combo box to select the view

    /**
     * {@code view} is the view of {@link SwingCalendar}.
     */
    private static String view = "Month"; // default view

    /**
     * {@code weekDate} is used to display the week date in week view.
     */
    private String[] weekDate = { "", "", "", "", "", "", "" };

    /**
     * {@code columns} is used to display the day of week in month view.
     */
    private String[] columns = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

    /**
     * Constructs a {@code SwingCalendar} object using {@link User} events.
     */
    SwingCalendar(User user) {
        this.user = user;
        // function for the drop down menu to change view
        viewComboBox.setOpaque(false);
        viewComboBox.setPreferredSize(new Dimension(70, 30));
        viewComboBox.addActionListener(ae -> {
            if (viewComboBox.getSelectedItem().equals("Month")) {
                view = "Month";
                cal.add(Calendar.MONTH, +1);
                update();
                cal.add(Calendar.MONTH, -1);
                update();

            } else if (viewComboBox.getSelectedItem().equals("Week")) {
                view = "Week";
                cal.add(Calendar.MONTH, +1);
                update();
                cal.add(Calendar.MONTH, -1);
                update();

            } else {
                System.out.println("Error");
            }
        });
        // initialize the panel
        this.setSize(400, 200);
        this.setPreferredSize(new Dimension(400, 200));
        this.setMinimumSize(new Dimension(300, 200));
        this.setLayout(new BorderLayout());
        this.setVisible(true);

        label.setHorizontalAlignment(SwingConstants.CENTER);

        JButton b1 = new JButton("<-");
        b1.setOpaque(false);
        b1.setPreferredSize(new Dimension(50, 30));
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                update(LEFT);
                update();
            }
        });

        todayButton.setPreferredSize(new Dimension(60, 30));
        todayButton.setOpaque(false);
        todayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cal.setTime(today);
                cal.add(Calendar.MONTH, +1);
                update();
                cal.add(Calendar.MONTH, -1);
                update();
            }
        });

        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new FlowLayout());
        leftPanel.setPreferredSize(new Dimension(150, 40));
        leftPanel.add(b1);
        leftPanel.add(todayButton);

        JButton b2 = new JButton("->");
        b2.setOpaque(false);
        b2.setPreferredSize(new Dimension(50, 30));
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                update(RIGHT);
                update();
            }
        });

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new FlowLayout());
        rightPanel.setPreferredSize(new Dimension(200, 40));
        exportButton.setOpaque(false);
        rightPanel.add(exportButton);
        rightPanel.add(viewComboBox);
        rightPanel.add(b2);

        panel = new JPanel();
        panel.setBackground(Color.PINK);
        panel.setLayout(new BorderLayout());
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(label, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);

        // panel is the part with week/month and year
        // pane is the date table/time slot table
        this.add(panel, BorderLayout.NORTH);
        this.add(pane, BorderLayout.CENTER);
        this.setBackground(Color.PINK);
        this.update();
    }

    /**
     * update when the left or right button is clicked
     * 
     * @param side {@code 0} for {@code LEFT}, {@code 1} for {@code RIGHT}
     */
    void update(int side) {
        if (view == "Month") {
            if (side == LEFT) {
                cal.add(Calendar.MONTH, -1);
            } else {
                cal.add(Calendar.MONTH, +1);
            }
        } else if (view == "Week") {
            if (side == LEFT) {
                cal.add(Calendar.DAY_OF_MONTH, -7);
            } else {
                cal.add(Calendar.DAY_OF_MONTH, +7);
            }
        }
    }

    /**
     * update the {@link SwingCalendar} when the month, year, or {@code view} is
     * changed
     */
    void update() {
        today = new Date();
        if (view == "Month") {
            this.remove(pane);
            if (exportButton.getActionListeners().length == 0) {
                exportButton.addActionListener(new FileChoose(user, cal, view));
            } else if (exportButton.getActionListeners().length == 1) {
                exportButton.removeActionListener(exportButton.getActionListeners()[0]);
                exportButton.addActionListener(new FileChoose(user, cal, view));
            }
            String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
            int year = cal.get(Calendar.YEAR);
            label.setText(month + " " + year);

            monthData = LoadEvents.updateMonthDataEvent(monthData, cal, user);
            monthModel.update(monthData);

            table = new JTable(monthModel);
            table.setDefaultRenderer(MonthCell.class, new MonthCellRenderer(user, this));
            table.setDefaultEditor(MonthCell.class, new MonthCellEditor(user, this));
            table.setRowHeight(100);
            table.setFillsViewportHeight(true);
            table.setEnabled(true);
            table.getTableHeader().setReorderingAllowed(false);

            pane = new JScrollPane(table);
            pane.setVisible(true);
            this.add(pane, BorderLayout.CENTER);
        } else if (view == "Week") {
            this.remove(pane);

            if (exportButton.getActionListeners().length == 0) {
                exportButton.addActionListener(new FileChoose(user, cal, view));
            } else if (exportButton.getActionListeners().length == 1) {
                exportButton.removeActionListener(exportButton.getActionListeners()[0]);
                exportButton.addActionListener(new FileChoose(user, cal, view));
            }

            int week = cal.get(Calendar.WEEK_OF_MONTH);
            String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
            int year = cal.get(Calendar.YEAR);
            label.setText(week + "/" + month + " " + year);

            weekData = LoadEvents.updateWeekDataEvent(weekData, cal, user);
            weekModel.update(weekData);

            table = new JTable(weekModel);
            table.setRowHeight(24 * 60 * 2);
            table.setPreferredSize(new Dimension(1000, 24 * 60 * 2));
            // table.setSize(1000, 24 * 60 * 2);
            table.setDefaultRenderer(WeekCell.class, new WeekCellRenderer(user, this));
            table.setDefaultEditor(WeekCell.class, new WeekCellEditor(user, this));
            table.setEnabled(true);
            table.getTableHeader().setReorderingAllowed(false);

            pane = new JScrollPane(table);
            pane.setViewportView(table);
            pane.getVerticalScrollBar().setUnitIncrement(2 * 15);
            pane.getVerticalScrollBar().setValue(9 * 60 * 2);
            this.add(pane, BorderLayout.CENTER);

            // set the time line panel from 0:00 to 23:00
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
                Calendar calendar = cal;
                calendar.set(Calendar.DAY_OF_WEEK, day + 1);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                weekDate[day] = String.valueOf(dayOfMonth);
                tc.setHeaderValue(columns[day] + " " + weekDate[day]);
                th.repaint();
            }
        }
    }
}
