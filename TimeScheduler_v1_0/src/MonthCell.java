package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * A {@code MonthCell} has all the information to display a month cell.
 * The {@code MonthCell} contains:
 * <ul>
 * <li>{@code date} - the date of the month cell</li>
 * <li>{@code events} - the events of the month cell</li>
 * <li>{@code times} - the times of the events</li>
 * <li>{@code titles} - the titles of the events</li>
 * </ul>
 * 
 * @author Tam Thai Hoang
 */
class MonthCell {
    public String date;
    public ArrayList<String> times = new ArrayList<String>();
    public ArrayList<String> titles = new ArrayList<String>();
    public ArrayList<Event> events = new ArrayList<Event>();
    DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    DateFormat yearFormat = new SimpleDateFormat("yyyy");
    DateFormat monthFormat = new SimpleDateFormat("MM");

    public MonthCell(String date) {
        this.date = date;
    }

    public MonthCell(String date, ArrayList<String> times, ArrayList<String> titles) {
        this.date = date;
        this.times = times;
        this.titles = titles;
    }

    public MonthCell() {
        this.date = "";
    }

    /**
     * Adds an event to the {@link MonthCell}.
     * 
     * @param event the {@link Event} to be added
     */
    public void addEvent(Event event) {
        this.events.add(event);
        this.times.add(timeFormat.format(event.getDate()));
        this.titles.add(event.getTitle());
    }

    /**
     * Removes an event from the month cell.
     * 
     * @param event {@link Event} to be removed.
     */
    public void removeEvent(Event event) {
        this.events.remove(event);
        this.times.remove(timeFormat.format(event.getDate()));
        this.titles.remove(event.getTitle());
    }

    /**
     * Removes an event from the month cell.
     * 
     * @param index index of {@code Event} in the events list to be removed.
     */
    public void removeEvent(int index) {
        this.events.remove(index);
        this.times.remove(index);
        this.titles.remove(index);
    }

    /**
     * Returns the {@link Event}s of the {@link MonthCell}.
     * 
     * @return the {@link Event}s of the {@link MonthCell}
     */
    public ArrayList<Event> getEvents() {
        return this.events;
    }
}

/**
 * {@code MonthTableModel} extends {@link DefaultTableModel} to be the table
 * model for the month view.
 * 
 */
class MonthTableModel extends DefaultTableModel {
    private ArrayList<List<MonthCell>> data;
    private String[] columns = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

    /**
     * Constructs a {@code MonthTableModel} with the given data.
     * 
     * @param data the data to be displayed in the table
     */
    public MonthTableModel(ArrayList<List<MonthCell>> data) {
        this.data = data;
    }

    public Class<?> getColumnClass(int columnIndex) {
        return MonthCell.class;
    }

    public int getColumnCount() {
        if (data.size() != 0) {
            return data.get(0).size();
        } else {
            return 0;
        }
    }

    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    public int getRowCount() {
        return (data == null) ? 0 : data.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex).get(columnIndex);
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        data.get(rowIndex).set(columnIndex, (MonthCell) value);
        this.fireTableDataChanged();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    /**
     * Update the data of the table model.
     * 
     * @param data the new data to be updated in the table
     */
    public void update(ArrayList<List<MonthCell>> data) {
        this.data = data;
    }
}

/**
 * {@code MonthTableCellComponent} extends {@link JPanel} to show all events
 * inside a month cell.
 * 
 */
class MonthCellComponent extends JPanel {
    private JLabel dateLabel;
    private JPanel datePanel = new JPanel();
    private ArrayList<JLabel> timeLabels = new java.util.ArrayList<JLabel>();
    private ArrayList<JLabel> titleLabels = new java.util.ArrayList<JLabel>();
    private JPanel eventsPanel = new JPanel(); // eventPanel has events
    private JButton button = new JButton("More"); // button to show all events if there are more than 3
    private JDialog dialog = new JDialog();
    private JPanel panel = this;

    /**
     * Constructor for {@code MonthCellComponent} class. It create a {@link JPanel}
     * to display a month cell.
     * 
     * @param monthCell the {@link MonthCell} class that has all the information to
     *                  display a month cell.
     */
    public MonthCellComponent(MonthCell monthCell, User user, SwingCalendar calendar) {
        // initialize components (labels, buttons, etc.)
        this.setLayout(new BorderLayout());
        dateLabel = new JLabel(monthCell.date);
        datePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        datePanel.add(dateLabel);

        for (String time : monthCell.times) {
            JLabel timeLabel = new JLabel(time);
            timeLabel.setHorizontalAlignment(SwingConstants.LEFT);
            timeLabels.add(timeLabel);
        }

        for (String title : monthCell.titles) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
            titleLabels.add(titleLabel);
        }

        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        for (int i = 0; i < monthCell.events.size(); i++) {
            String time = timeLabels.get(i).getText();
            String title = titleLabels.get(i).getText();
            JButton eventButton = new JButton(time + " " + title);
            Event event = monthCell.events.get(i);
            if (event.getPriority() == 0) {
                eventButton.setBackground(Color.RED);
            } else if (event.getPriority() == 1) {
                eventButton.setBackground(Color.YELLOW);
            } else if (event.getPriority() == 2) {
                eventButton.setBackground(Color.GREEN);
            }
            String timeEnd = (timeFormat.format(new Date(event.getDate().getTime() + event.getDuration() * 60000)));
            eventButton.setHorizontalAlignment(SwingConstants.LEFT);
            eventButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new EditEventDialog(event, time, timeEnd, panel, user, calendar);
                }
            });
            eventsPanel.add(eventButton); // add each event Button to eventsPanel
        }

        this.add(datePanel, BorderLayout.NORTH);
        this.add(eventsPanel, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(100, 100));
        this.setSize(100, 100);

        if (monthCell.events.size() > 3) {
            button = new JButton("More");
            this.add(button, BorderLayout.SOUTH);
            button.addActionListener(l -> {
                dialog.setAlwaysOnTop(true); // always on top
                dialog.setTitle(dateLabel.getText());
                dialog.setIconImage(null);
                dialog.setSize(100, 40 * monthCell.events.size());
                dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                dialog.setLocationRelativeTo(button);
                dialog.setVisible(true);
                dialog.setLayout(new BorderLayout());
                dialog.add(datePanel, BorderLayout.NORTH);
                dialog.add(eventsPanel, BorderLayout.CENTER);
            });
        }
    }
}

/**
 * {@code MonthCellRender} implements {@link TableCellRenderer} to show all
 * events inside a month cell.
 */
class MonthCellRenderer implements TableCellRenderer {
    private static MonthCellComponent panel;
    private static MonthCell monthCell;
    private User user;
    private SwingCalendar calendar;

    /**
     * Constructor for {@code MonthCellRender} class. It create a
     * {@link MonthCellComponent} to display a month cell.
     * 
     * @param user the {@link User} class that will be show the calendar.
     * @param calendar the {@link SwingCalendar} class that will be show the
     *                calendar.
     */
    public MonthCellRenderer(User user, SwingCalendar calendar) {
        this.user = user;
        this.calendar = calendar;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        monthCell = (MonthCell) value;
        panel = new MonthCellComponent(monthCell, user, calendar);
        return panel;
    }
}

/**
 * {@code MonthTableCellEditor} extends {@link AbstractCellEditor} implements
 * {@link TableCellEditor} to edit all events inside a
 * month cell.
 */
class MonthCellEditor extends AbstractCellEditor implements TableCellEditor {
    private static MonthCellComponent panel;
    private static MonthCell monthCell;
    private User user;
    private SwingCalendar calendar;

    /**
     * Constructor for {@code MonthCellEditor} class. It create a
     * {@link MonthCellComponent} to display a month cell.
     * 
     * @param user the {@link User} class that will be show the calendar.
     * @param calendar the {@link SwingCalendar} class that will be show the
     *                calendar.
     */
    public MonthCellEditor(User user, SwingCalendar calendar) {
        this.user = user;
        this.calendar = calendar;
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        monthCell = (MonthCell) value;
        // System.out.println("Date: " + monthCell.date);
        // System.out.println("Times: " + monthCell.times);
        // System.out.println("Titles: " + monthCell.titles + "\n");
        panel = new MonthCellComponent(monthCell, user, calendar);
        // System.out.println("Cell" + row + "," + column + " is selected: " +
        // isSelected);
        return panel;
    }

    public Object getCellEditorValue() {
        return monthCell;
    }
}
