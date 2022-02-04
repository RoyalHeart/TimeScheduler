package src;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

class WeekCell {
    public String date;
    public ArrayList<String> timesStart = new ArrayList<String>();
    public ArrayList<String> timesEnd = new ArrayList<String>();
    public ArrayList<String> titles = new ArrayList<String>();
    public ArrayList<Event> events = new ArrayList<Event>();
    DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    DateFormat yearFormat = new SimpleDateFormat("yyyy");
    DateFormat monthFormat = new SimpleDateFormat("MM");

    public WeekCell(String date) {
        this.date = date;
    }

    public WeekCell(String date, ArrayList<String> timesStart, ArrayList<String> titles) {
        this.date = date;
        this.timesStart = timesStart;
        this.titles = titles;
    }

    public WeekCell() {
        this.date = "Test WeekCell";
    }

    public void addEvent(Event event) {
        this.events.add(event);
        this.timesStart.add(timeFormat.format(event.getDate()));
        this.timesEnd.add(timeFormat.format(new Date(event.getDate().getTime() + event.getDuration() * 60000)));
        this.titles.add(event.getTitle());
    }

    public void removeEvent(Event event) {
        this.events.remove(event);
        this.timesStart.remove(timeFormat.format(event.getDate()));
        this.titles.remove(event.getTitle());
    }

    public void removeEvent(int index) {
        this.events.remove(index);
        this.timesStart.remove(index);
        this.titles.remove(index);
    }

    public ArrayList<Event> getEvents() {
        return this.events;
    }
}

class WeekTableModel extends DefaultTableModel {
    private ArrayList<WeekCell> data;
    private String[] columns = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

    public WeekTableModel(ArrayList<WeekCell> data) {
        this.data = data;
    }

    public Class<?> getColumnClass(int columnIndex) {
        return WeekCell.class;
    }

    public int getColumnCount() {
        return 7;
    }

    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    public int getRowCount() {
        return 1;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(columnIndex);
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        data.set(columnIndex, (WeekCell) value);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public void update(ArrayList<WeekCell> data) {
        this.data = data;
    }
}

class WeekCellComponent extends JPanel {
    DateFormat hourFormat = new SimpleDateFormat("HH");
    DateFormat minuteFormat = new SimpleDateFormat("mm");
    JLayeredPane layeredPane = new JLayeredPane();
    JPanel panel = this;

    public WeekCellComponent(WeekCell myClass, JTable table) {
        // initialize components (labels, buttons, etc.)
        this.setPreferredSize(new Dimension(table.getSize().width, 120 * 25));
        TableColumn column = table.getColumnModel().getColumn(0);

        // every minute is 2 pixels
        layeredPane.setPreferredSize(new Dimension(column.getWidth(), 120 * 24));
        layeredPane.setBounds(0, 0, table.getSize().width, 120 * 24);
        layeredPane.setBackground(new Color(255, 200, 200));
        layeredPane.setOpaque(true);
        JButton eventButton = new JButton();
        Event lastEvent;
        int lastWidth = column.getWidth();

        // add events to the layered pane one by one
        // a better way is to get all the bounds of the events and add them to the
        // layered pane
        for (int i = 0; i < myClass.events.size(); i++) {
            if (i > 0) {
                lastEvent = myClass.events.get(i - 1);
            } else {
                lastEvent = myClass.events.get(i);
            }
            Event event = myClass.events.get(i);
            String timeStart = myClass.timesStart.get(i);
            String timeEnd = myClass.timesEnd.get(i);
            String title = myClass.titles.get(i);
            long startTime = event.getDate().getTime();
            long lastStartTime = lastEvent.getDate().getTime();
            int y = (Integer.parseInt(hourFormat.format(event.getDate()))) * 120
                    + Integer.parseInt(minuteFormat.format(event.getDate())) * 2;
            eventButton = new JButton();
            JLabel titleLabel = new JLabel(title);
            titleLabel.setBounds(5, 2, column.getWidth(), 15);
            JLabel time = new JLabel(timeStart + " - " + timeEnd);
            time.setBounds(5, 14, column.getWidth(), 15);
            eventButton.setLayout(null);
            eventButton.setOpaque(true);
            eventButton.add(titleLabel);
            eventButton.add(time);
            if (startTime > lastStartTime && startTime < lastStartTime + lastEvent.getDuration() * 60000
                    && i > 0) {
                eventButton.setBounds(0, y, lastWidth - 10, 2 * event.getDuration());
                lastWidth -= 10;
            } else if (startTime == lastStartTime
                    && startTime < lastStartTime + lastEvent.getDuration() * 60000
                    && i > 0) {
                eventButton.setBounds((int) lastWidth / 2, y, (int) (lastWidth / 2) - 10,
                        2 * event.getDuration());
                lastWidth = lastWidth / 2 - 10;
            } else {
                eventButton.setBounds(0, y, column.getWidth(), 2 * event.getDuration());
                lastWidth = column.getWidth();
            }
            if (event.getPriority() == 0) {
                eventButton.setBorder(BorderFactory.createEtchedBorder(0, Color.RED,
                        Color.RED));
                eventButton.setBackground(Color.RED);
            } else if (event.getPriority() == 1) {
                eventButton.setBorder(BorderFactory.createBevelBorder(0, Color.YELLOW,
                        Color.YELLOW));
                eventButton.setBackground(Color.YELLOW);
            } else if (event.getPriority() == 2) {
                eventButton.setBorder(BorderFactory.createBevelBorder(0, Color.GREEN,
                        Color.GREEN));
                eventButton.setBackground(Color.GREEN);
            }
            eventButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // JOptionPane.showMessageDialog(null, "Event: " + event.getDate() + "\nTime: " + time.getText());
                    new EditEventDialog(event, timeStart, timeEnd, panel);
                }
            });

            layeredPane.add(eventButton, Integer.valueOf(i));
        }
        this.add(layeredPane);
    }
}

class WeekCellRenderer implements TableCellRenderer {
    WeekCellComponent panel;

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        WeekCell myClass = (WeekCell) value;
        panel = new WeekCellComponent(myClass, table);
        return panel;
    }
}

class WeekCellEditor extends AbstractCellEditor implements TableCellEditor {
    WeekCellComponent panel;
    WeekCell weekCell;

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        WeekCell myClass = (WeekCell) value;
        weekCell = myClass;
        panel = new WeekCellComponent(myClass, table);
        System.out.println("Cell" + row + "," + column + " is selected: " + isSelected);
        return panel;
    }

    public Object getCellEditorValue() {
        return weekCell;
    }
}

