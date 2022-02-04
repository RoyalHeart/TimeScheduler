package src;

// import oracle.jdbc.proxy.annotation.OnError;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.sql.Time;
import java.awt.event.FocusEvent;
import java.text.ParseException;
// import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
// import javax.tools.DocumentationTool.Location;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class EditEventDialog extends JDialog{
    Event event;
    JDialog dialog;
    JPanel panel;
    String timeStart;
    String timeEnd;

    EditEventDialog(Event event, String timeStart, String timeEnd, JPanel panel)
    {
        super(SwingUtilities.windowForComponent(panel));
        this.event = event;
        this.dialog = this;
        this.panel = panel;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.setTitle("Event");
        this.setSize(300, 300);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setLayout(new BorderLayout());
        this.add(new displayPanel(), BorderLayout.CENTER);
        this.add(new Btn(), BorderLayout.SOUTH);
    }

    class displayPanel extends JPanel 
    {
        JLabel title = new JLabel();
        JLabel time = new JLabel();
        GridBagConstraints gbc = new GridBagConstraints();

        displayPanel()
        {
            this.setLayout(new GridBagLayout());
            title.setText(event.getTitle());
            gbc.gridx = 0;
            gbc.gridy = 0;
            this.add(title, gbc);

            String titleDate = String.format("%s - %s-%s", event.getDate().toString(), timeStart, timeEnd);
            time.setText(titleDate);
            gbc.gridx = 0;
            gbc.gridy = 1;
            this.add(time, gbc);
        }
    }

    class Btn extends JPanel
    {
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");
        Btn()
        {
            this.setLayout(new FlowLayout());
            this.add(editBtn);
            this.add(delBtn);

            try {
                delBtn.addActionListener(e -> {
                    if (Database.delEvent(event))
                    {
                        JOptionPane.showMessageDialog(null, "Event deleted successfully.");
                        dialog.dispose();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                editBtn.addActionListener(e ->
                {
                    dialog.dispose();
                    new MainPanel(event, panel, timeStart, timeEnd);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MainPanel extends JDialog
    {
        MainPanel(Event event, JPanel panel, String timeStart, String timeEnd)
        {
            super(SwingUtilities.windowForComponent(panel));
            this.setTitle("Event");
            this.setSize(800, 500);
            this.setLayout(new BorderLayout());
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            this.setLocationRelativeTo(null);
            this.add(new TitlePanel(event), BorderLayout.NORTH);
            this.add(new EventMainPanel(event, timeStart, timeEnd), BorderLayout.CENTER);
            this.add(new EditBtn(event), BorderLayout.SOUTH);
            this.setResizable(false);
            this.setVisible(true);
        }
    }

    class TitlePanel extends JPanel
    {
        static JTextField titleField = new JTextField(50);

        // Constructor for edit event
        TitlePanel(Event event)
        {
            this.setLayout(new FlowLayout());
            JLabel titleLabel = new JLabel("Title");
            titleField.setText(event.getTitle());
            titleField.selectAll();

            // 1 click to select all title
            titleField.addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton((java.awt.event.MouseEvent) e)) {
                        titleField.selectAll();
                    }
                }
            });
            this.add(titleLabel);
            this.add(titleField);
        }

        static String getTitle() {
            return titleField.getText();
        }
    }

    class EventMainPanel extends JPanel
    {
        GridBagConstraints gbc = new GridBagConstraints();

        EventMainPanel(Event event, String timeStart, String timeEnd) {
            this.setLayout(new GridBagLayout());

            DateTime datetime = new DateTime(event, timeStart, timeEnd);
            gbc.gridx = 0;
            gbc.gridy = 1;
            this.add(datetime, gbc);

            /*FriendField friendField = new FriendField();
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 0.5;
            this.add(friendField, gbc); */

             LocationField locationField = new LocationField(event);
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 0.5;
            this.add(locationField, gbc);

             Description description = new Description(event);
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 0.5;
            this.add(description, gbc); 

             Reminder reminder = new Reminder(event);
            gbc.gridx = 0;
            gbc.gridy = 5;
            this.add(reminder, gbc);

            Priority priority = new Priority(event);
            gbc.gridx = 0;
            gbc.gridy = 6;
            this.add(priority, gbc); 
        }

    }

    class DateTime extends JPanel
    {
        JLabel dateLabel = new JLabel();
        JLabel timeLabel = new JLabel();
        static Event event;
        static String timeStart;
        static String timeEnd;
        static TimeComboBox startTime;
        static TimeComboBox endTime;
        JLabel iconLabel;
        ImageIcon icon;
        UtilDateModel model = new UtilDateModel();
        // Properties for JDateTimePicker
        Properties p = new Properties();
        // Variable to store return value from jdatepicker
        static JDatePickerImpl datePicker;

        DateTime(Event event, String timeStart, String timeEnd) {
            this.setLayout(new FlowLayout());
            DateTime.timeStart = timeStart;
            DateTime.timeEnd = timeEnd;
            DateTime.startTime = new TimeComboBox(event, timeStart);
            DateTime.endTime = new TimeComboBox(event, timeEnd);
            try {
                icon = new ImageIcon(getClass().getResource("Images/icondatetime.png"));
                Image image = icon.getImage();
                Image newing = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
                icon = new ImageIcon(newing);
                iconLabel = new JLabel(icon);
                this.add("Hello", iconLabel);
            } catch (Exception e) {
                System.out.println("Image cannot be found");
            }
            dateLabel.setText("Date");
            this.add(dateLabel);
            // Implements properties
            p.put("text.month", "Month");
            p.put("text.today", "Today");
            p.put("text.year", "Year");
            model.setDate(event.getDate().getYear() + 1900, event.getDate().getMonth() + 1, event.getDate().getDate());
            model.setSelected(true);
            JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
            datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
            // return value from datePicker
            // date = (Date)datePicker.getModel().getValue();
            this.add(datePicker);
            timeLabel.setText("Time");
            this.add(timeLabel);
            this.add(startTime);
            this.add(endTime);
        }

        static Date getDateTime() {
            // if Date is not selected will take current date
            if ((Date) datePicker.getModel().getValue() == null) {
                return new Date();
            }
            // return selected date
            return (Date) datePicker.getModel().getValue();
        }

        // return diff between from and to time
        static int getDuration() {
            // SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Date before = startTime.getPickedTime();
            Date after = endTime.getPickedTime();
            /*
            * try {
            * before = format.parse(before);
            * after = format.parse(after);
            * } catch (ParseException e) {
            * e.printStackTrace();
            * }
            */
            long diff = after.getTime() - before.getTime();
            // convert diff time to minutes
            int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(diff);

            return minutes;
        }

        // combine date and startTime to 1 variable Date
        static Date getDate() throws ParseException {
            int h = startTime.getPickedTime().getHours();
            int m = startTime.getPickedTime().getMinutes();
            Date temp = getDateTime();
            int month = temp.getMonth();
            int date = temp.getDate();
            int year = temp.getYear() + 1900;
            String string = String.format("%d-%d-%d %d:%d", date, month, year, h, m);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            return sdf.parse(string);
        }
    }

    class LocationField extends JPanel {
        static JTextField locationField = new JTextField(50);
        ImageIcon icon;
        JLabel iconLabel;

        LocationField(Event event) {
            this.setLayout(new FlowLayout());

            try {
                icon = new ImageIcon(getClass().getResource("Images/locationicon.jpg"));
                Image image = icon.getImage();
                Image newing = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
                icon = new ImageIcon(newing);
                iconLabel = new JLabel(icon);
                this.add(iconLabel);
            } catch (Exception e) {
                System.out.println("Image can not be found");
            }

            locationField.setText(event.getLocation());
            locationField.addFocusListener(new FocusListener() {
                String location = "";

                @Override
                public void focusGained(FocusEvent e) {
                    locationField.setText(location);
                }

                @Override
                public void focusLost(FocusEvent e) {
                    location = locationField.getText();
                    if (location.equals("")) {
                        locationField.setText(event.getLocation());
                    }
                }
            });
            this.add(locationField);
        
        }

        static String getLoc() {
            return locationField.getText();
        }
    }

    class Description extends JPanel {
        static JTextField descripField = new JTextField(50);
        ImageIcon icon;
        JLabel iconLabel;

        Description(Event event) {
            this.setLayout(new FlowLayout());

            try {
                icon = new ImageIcon(getClass().getResource("Images/descriptionicon.png"));
                Image image = icon.getImage();
                Image newing = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
                icon = new ImageIcon(newing);
                iconLabel = new JLabel(icon);
                this.add(iconLabel);

            } catch (Exception e) {
                System.out.println("Image can not be found.");
            }

            descripField.setText(event.getDescription());
            descripField.addFocusListener(new FocusListener() {
                String description = "";

                @Override
                public void focusGained(FocusEvent e) {
                    descripField.setText(description);
                }

                @Override
                public void focusLost(FocusEvent e) {
                    description = descripField.getText();
                    if (description.equals("")) {
                        descripField.setText(event.getDescription());
                    }
                }
            });
            this.add(descripField);
        }

        static String getDescription() {
            return descripField.getText();
        }
    }

    class Reminder extends JPanel {
        JLabel reminderLabel = new JLabel("Remind before: ");
        static String[] arr = { "No remind", "1 minute", "3 hour", "3 days", "1 week" };
        static JComboBox<String> reminderComboBox = new JComboBox<String>(arr);

        Reminder(Event event)
        {
            this.add(reminderLabel);
            this.add(reminderComboBox);
        }

        static Date getRemind() throws ParseException {
            // convert date to localdatetime
            LocalDateTime temp = DateTime.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            Date date = new Date();
            switch (reminderComboBox.getSelectedIndex()) {
                case 0:
                    return DateTime.getDate();
                case 1:
                    temp = temp.minusMinutes(1);
                    break;
                case 2:
                    temp = temp.minusHours(3);
                    break;
                case 3:
                    temp = temp.minusDays(3);
                    break;
                case 4:
                    temp = temp.minusDays(7);
                    break;
            }

            // convert localdatetime to date
            date = Date.from(temp.atZone(ZoneId.systemDefault()).toInstant());
            return date;
        }
    }

    // Combobox display time
    static class TimeComboBox extends JPanel {
        JComboBox<Date> cb;
        int index = 0;
        
        TimeComboBox(Event event, String time) {
            setLayout(new GridBagLayout());
            int index = 0;
            Calendar cal = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            String string = String.format("%d-%d-%d %s", cal.getTime().getDate(), cal.getTime().getMonth() + 1, cal.getTime().getYear() + 1900, time);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            try {
                Date sTime = sdf.parse(string);
                cal.setTime(sTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Calendar end = Calendar.getInstance();
            end.set(Calendar.HOUR_OF_DAY, 23);
            end.set(Calendar.MINUTE, 59);
            DefaultComboBoxModel<Date> model = new DefaultComboBoxModel<Date>();
            do {
                model.addElement(calendar.getTime());
                calendar.add(Calendar.MINUTE, 15);
                if (calendar.compareTo(cal) < 0) {
                    calendar.add(Calendar.MINUTE, -15);
                    index = model.getIndexOf(calendar.getTime());
                    calendar.add(Calendar.MINUTE, 15);
                }
            } while (calendar.getTime().before(end.getTime()));


            cb = new JComboBox<Date>(model);
            cb.setRenderer(new DateComboBoxRenderer());
            cb.setSelectedIndex(index + 2);

            add(cb);
        }

        Date getPickedTime() {
            return (Date) cb.getSelectedItem();
        }
    }

    class Priority extends JPanel {
        JLabel priorityLabel = new JLabel("Priority");
        static String[] arr = { "High", "Medium", "Low" };
        static JComboBox<String> priorityComboBox = new JComboBox<String>(arr);

        Priority(Event event) {
            priorityComboBox.setSelectedIndex(event.getPriority());
            this.add(priorityLabel);
            this.add(priorityComboBox);
        }

        static int getPriority() {
            return priorityComboBox.getSelectedIndex();
        }
    }

    class EditBtn extends JPanel
    {
        JButton editBtn = new JButton("Save");
        EditBtn(Event event)
        {
            try {
                editBtn.addActionListener(e -> 
                {
                    if (DateTime.getDuration() <= 0)
                    {
                        JOptionPane.showMessageDialog((JButton) e.getSource(),
                                "Please select end time greater than start time!");
                        return;
                    }
                    else
                    {
                        System.out.println("Duration: " + DateTime.getDuration());
                        System.out.println("Title: " + TitlePanel.getTitle());
                        System.out.println("DateTime: " + DateTime.getDateTime());
                        System.out.println("Location: " + LocationField.getLoc());
                        System.out.println("Priority: " + Priority.getPriority());
                        try {
                            System.out.println("Reminder: " + Reminder.getRemind());
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println("Description: " + Description.getDescription());
                        try {
                            System.out.println("Date: " + DateTime.getDate() + "\n\n");
                        } catch (ParseException e2) {
                            e2.printStackTrace();
                        }
                        try {
                            Event newEvent = new Event(event.getID(), event.getUserID(),
                                TitlePanel.getTitle(),
                                Description.getDescription(),
                                DateTime.getDate(),
                                Reminder.getRemind(),
                                LocationField.getLoc(),
                                DateTime.getDuration(),
                                Priority.getPriority());
                            if (Database.updateEvent(newEvent))
                            {
                                JOptionPane.showMessageDialog((JButton) e.getSource(), "Event updated successfully!");
                                // Mail.sendRemindEmail(user, event); // send email right after event is created
                                /* if (event.getRemind().compareTo(event.getDate()) < 0) {
                                    System.out.println("Remind set");
                                    SchedulerJava.scheduleMail(user, event);
                                } */
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.add(editBtn);
        }
    }

    // Render combobox to display HH:mm
    static class DateComboBoxRenderer extends DefaultListCellRenderer {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            Object item = value;
            if (item instanceof Date) {
                item = dateFormat.format((Date) item);
            }
            return super.getListCellRendererComponent(list, item, index, isSelected,
                    cellHasFocus);
        }
    }

    // Format for JDateTimePicker
    class DateLabelFormatter extends AbstractFormatter {

        private String datePattern = "dd-MM-yyyy";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }
    }
}
