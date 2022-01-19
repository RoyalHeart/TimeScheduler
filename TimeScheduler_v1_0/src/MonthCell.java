package src;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

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
    ArrayList<JLabel> timeLabels = new java.util.ArrayList<JLabel>();
    ArrayList<JLabel> titleLabels = new java.util.ArrayList<JLabel>();
    JPanel eventsPanel = new JPanel(); // eventPanel has events
    JButton button = new JButton("More"); // button to show all events if there are more than 3

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

        for (int i = 0; i < myClass.events.size(); i++) {
            String time = timeLabels.get(i).getText();
            String title = titleLabels.get(i).getText();
            JButton eventButton = new JButton(time + " " + title);
            eventButton.setBorderPainted(false);
            eventButton.setContentAreaFilled(false);
            eventButton.setFocusPainted(false);
            eventButton.setHorizontalAlignment(SwingConstants.LEFT);
            eventButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Event: " + title + "\nTime: " + time);
                }
            });
            eventsPanel.add(eventButton); // add each event Button to eventsPanel

        }

        this.add(datePanel, BorderLayout.NORTH);
        this.add(eventsPanel, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(100, 100));
        this.setSize(100, 100);

        if (myClass.events.size() > 3) {
            button = new JButton("More");
            this.add(button, BorderLayout.SOUTH);
            button.addActionListener(l -> {
                JDialog dialog = new JDialog();
                dialog.setSize(100, 40 * myClass.events.size());
                dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                dialog.setLocationRelativeTo(button);
                dialog.setVisible(true);
                dialog.setLayout(new BorderLayout());
                dialog.add(datePanel, BorderLayout.NORTH);
                dialog.add(eventsPanel, BorderLayout.CENTER);
                dialog.setVisible(true);
            });
            // JScrollPane scrollPane = new JScrollPane(eventsPanel);
            // this.add(scrollPane, BorderLayout.CENTER);
        }
        // add action listeners
    }

    public void updateData(MonthCell myClass, boolean isSelected) {
        // add action listeners
    }
}

class MyClassCellRenderer implements TableCellRenderer {
    MyClassCellComponent panel;
    static MonthCell monthCell;

    public MyClassCellRenderer() {

    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        // value = table.get(row).get(column);
        MonthCell myClass = (MonthCell) value;
        monthCell = myClass;
        panel = new MyClassCellComponent(myClass);
        return panel;
    }
}

class MyClassCellEditor extends AbstractCellEditor implements TableCellEditor {
    MyClassCellComponent panel;
    MonthCell monthCell;

    public MyClassCellEditor() {
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        // scrollPane = new JScrollPane(panel);
        MonthCell myClass = (MonthCell) value;
        monthCell = myClass;
        System.out.println("Date: " + myClass.date);
        System.out.println("Times: " + myClass.times);
        System.out.println("Titles: " + myClass.titles + "\n");
        panel = new MyClassCellComponent(myClass);
        panel.updateData(myClass, isSelected);
        System.out.println("Cell" + row + "," + column + " is selected: " + isSelected);
        return panel;
    }

    public Object getCellEditorValue() {
        return monthCell;
    }
}
