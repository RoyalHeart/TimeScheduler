package src;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
// import java.util.Locale;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.awt.event.*;
import java.util.Random;

class MonthCell {
    public String date;
    public ArrayList<String> times = new ArrayList<String>();
    public ArrayList<String> titles = new ArrayList<String>();
    public ArrayList<Event> events = new ArrayList<Event>();
    public int row;
    public int column;
    DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    DateFormat yearFormat = new SimpleDateFormat("yyyy");
    DateFormat monthFormat = new SimpleDateFormat("MM");

    public MonthCell(String date) {
        this.date = date;
        this.row = 0;
        this.column = 0;
    }

    public MonthCell(String date, ArrayList<String> times, ArrayList<String> titles) {
        this.date = date;
        this.times = times;
        this.titles = titles;
    }

    public MonthCell(String date, ArrayList<String> times, ArrayList<String> titles, int row, int column) {
        this.date = date;
        this.times = times;
        this.titles = titles;
        this.row = row;
        this.column = column;
    }

    public MonthCell() {
        this.date = "";
    }

    public void addEvent(Event event) {
        this.events.add(event);
        this.times.add(timeFormat.format(event.getDate()));
        this.titles.add(event.getTitle());
    }

    public void removeEvent(Event event) {
        this.events.remove(event);
        this.times.remove(timeFormat.format(event.getDate()));
        this.titles.remove(event.getTitle());
    }

    public void removeEvent(int index) {
        this.events.remove(index);
        this.times.remove(index);
        this.titles.remove(index);
    }

    public ArrayList<Event> getEvents() {
        return this.events;
    }
}
// public static void main(ArrayList<String> args) {
// // Database.createConnection();
// // List<MonthCell> list = Database.getEvents(new Date(), new Date());
// ArrayList<List<MonthCell>> data = new ArrayList<List<MonthCell>>();
// Calendar cal = new GregorianCalendar();
// JFrame frame = new JFrame();
// frame.setSize(800, 600);
// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// frame.setLocationRelativeTo(null);
// frame.setLayout(new BorderLayout());

// cal.set(Calendar.DAY_OF_MONTH, 1);
// int startDay = cal.get(Calendar.DAY_OF_WEEK);
// int numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
// int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
// int day = 1;

// for (int i = 0; i < weeks; i++) {
// List<MonthCell> list = new ArrayList<MonthCell>();
// for (int j = 0; j < 7; j++) {
// if (i == 0 && j < startDay - 1) {
// list.add(new MonthCell());
// } else if (day > numberOfDays) {
// list.add(new MonthCell());
// } else {
// list.add(new MonthCell(String.valueOf(day)));
// day++;
// }
// }
// data.add(list);
// }
// // model.setValueAt(value, rowIndex, columnIndex);
// MyClassTableModel model = new MyClassTableModel(data);
// JTable myClassTable = new JTable(model);

// myClassTable.setDefaultRenderer(MonthCell.class, new MyClassCellRenderer());
// // myClassTable.setDefaultEditor(MonthCell.class, new MyClassCellEditor());

// myClassTable.setRowHeight(100);
// JScrollPane pane = new JScrollPane(myClassTable);
// JButton button = new JButton("test");
// frame.add(pane, BorderLayout.CENTER);
// frame.add(button, BorderLayout.SOUTH);
// frame.setVisible(true);

// button.addActionListener(new ActionListener() {
// @Override
// public void actionPerformed(ActionEvent e) {
// frame.remove(pane);
// frame.remove(button);
// ArrayList<List<MonthCell>> data = new ArrayList<List<MonthCell>>();
// // System.out.println(cell.date);
// cal.add(Calendar.MONTH, +1);
// cal.set(Calendar.DAY_OF_MONTH, 1);
// cal.set(Calendar.DAY_OF_MONTH, 1);
// int startDay = cal.get(Calendar.DAY_OF_WEEK);
// int numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
// int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
// int day = 1;
// int month = cal.get(Calendar.MONTH);
// // User user = Database.getUser("abc", Hash.hashPassword("aabc"));
// ArrayList<Event> events = new ArrayList<Event>();
// // create 10 random events
// for (int i = 0; i < 10; i++) {
// try {
// Random rand = new Random();
// // rand.nextInt() % 12 + 1
// String dateString = rand.nextInt() % 28 + 1 + "-" + rand.nextInt() % 12 + 1 +
// "-"
// + 2022;
// DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
// String title = "title" + i;
// // String time = "time" + i;
// Date date = df.parse(dateString);
// Date startTime = new Date(date.getTime() + rand.nextInt() % 100 * 60 * 60 *
// 1000);
// int duration = rand.nextInt() % 10 + 1;
// // int priority = rand.nextInt() % 3;
// Event event = new Event("0", title, date, startTime, duration);
// events.add(event);
// } catch (ParseException e1) {
// System.out.println("Error");
// }
// }
// int k = 0;
// ArrayList<String> titles = new String[times.length];
// for (Event event : events) {
// System.out.println("Date: " + event.getDate() + " Titles: " +
// event.getTitle());
// LocalDate localDate =
// event.getDate().toInstant().atZone(ZoneId.systemDefault())
// .toLocalDate();

// if (localDate.getMonthValue() == month && localDate.getDayOfMonth() == day) {
// titles[k] = event.getTitle();
// k++;
// }
// }

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
// // model.setValueAt(value, rowIndex, columnIndex);
// MyClassTableModel model = new MyClassTableModel(data);
// JTable myClassTable = new JTable(model);
// myClassTable.setDefaultRenderer(MonthCell.class, new MyClassCellRenderer());
// myClassTable.setRowHeight(100);
// JScrollPane pane = new JScrollPane(myClassTable);
// frame.add(pane, BorderLayout.CENTER);
// frame.add(button, BorderLayout.SOUTH);
// frame.setVisible(true);
// // if (y < 6) {
// // y++;
// // } else if (x < model.getRowCount() - 1) {
// // y = 0;
// // x++;
// // }
// }
// });
// }
// }

class MyClassTableModel extends DefaultTableModel {
    private ArrayList<List<MonthCell>> data;
    private String[] columns = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

    public MyClassTableModel(ArrayList<List<MonthCell>> data) {
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

    public void update(ArrayList<List<MonthCell>> data) {
        this.data = data;
    }
}

class MyClassCellComponent extends JPanel {
    JLabel dateLabel = new JLabel();
    JPanel datePanel = new JPanel();
    JScrollPane scrollPane = new JScrollPane();
    // JLabel timeLabel = new JLabel();
    ArrayList<JLabel> timeLabels = new java.util.ArrayList<JLabel>();
    // JLabel titleLabel = new JLabel();
    ArrayList<JLabel> titleLabels = new java.util.ArrayList<JLabel>();
    JPanel eventPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // event has time and title
    JPanel eventsPanel = new JPanel(); // eventPanel has events

    public MyClassCellComponent(MonthCell myClass) {
        // initialize components (labels, buttons, etc.)
        this.setLayout(new BorderLayout());
        dateLabel = new JLabel(myClass.date);

        datePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        datePanel.add(dateLabel);

        for (String time : myClass.times) {
            JLabel timeLabel = new JLabel(time);
            timeLabel.setHorizontalAlignment(SwingConstants.LEFT);
            timeLabels.add(timeLabel);
        }

        for (String title : myClass.titles) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
            titleLabels.add(titleLabel);
        }

        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < myClass.events.size(); i++) {
            eventPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // eventPanel has time and title
            eventPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            eventPanel.add(timeLabels.get(i));
            eventPanel.add(titleLabels.get(i));
            eventsPanel.add(eventPanel); // add each event Panel to eventsPanel
        }

        this.add(datePanel, BorderLayout.NORTH);
        scrollPane = new JScrollPane(eventsPanel);
        this.add(eventsPanel, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(100, 100));
        this.setSize(100, 100);
        // add action listeners
    }

    public void updateData(MonthCell myClass, boolean isSelected, JTable table, int row, int column) {
        // this.data.get(row).set(column, myClass);

        dateLabel = new JLabel(myClass.date);

        for (String time : myClass.times) {
            JLabel timeLabel = new JLabel(time);
            timeLabel.setHorizontalAlignment(SwingConstants.LEFT);
            timeLabels.add(timeLabel);
        }

        for (String title : myClass.titles) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
            titleLabels.add(titleLabel);
        }

        // add action listeners
    }
}

class MyClassCellRenderer implements TableCellRenderer {
    MyClassCellComponent panel;
    // ArrayList<List<MonthCell>> data;

    public MyClassCellRenderer() {
    }

    public MyClassCellRenderer(MonthCell myClass) {
        panel = new MyClassCellComponent(myClass);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        // value = table.get(row).get(column);
        MonthCell myClass = (MonthCell) value;
        panel = new MyClassCellComponent(myClass);
        return panel;
    }
}

class MyClassCellEditor extends AbstractCellEditor implements TableCellEditor {
    MyClassCellComponent panel;
    JScrollPane scrollPane;

    public MyClassCellEditor() {
    }

    public MyClassCellEditor(MonthCell myClass) {
        panel = new MyClassCellComponent(myClass);
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        // scrollPane = new JScrollPane(panel);
        return panel;
    }

    public Object getCellEditorValue() {
        return null;
    }
}
