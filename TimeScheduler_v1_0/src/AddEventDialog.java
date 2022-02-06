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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
// import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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
import org.w3c.dom.events.MouseEvent;

public class AddEventDialog extends JDialog {
    AddEventDialog(User user, SwingCalendar calendar) {
        super(SwingUtilities.windowForComponent(calendar));
        this.setTitle("Event");
        this.setSize(800, 500);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.add(new TitlePanel(), BorderLayout.NORTH);
        this.add(new EventMainPanel(), BorderLayout.CENTER);
        this.add(new SetBtn(user, calendar, this), BorderLayout.SOUTH);
        this.setResizable(false);
        // this.setVisible(true);
    }
}

class TitlePanel extends JPanel {
    static JTextField titleField = new JTextField(50);

    TitlePanel() {
        this.setLayout(new FlowLayout());
        JLabel titleLabel = new JLabel("Title");
        titleField.setText("Title");
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

class EventMainPanel extends JPanel {
    GridBagConstraints gbc = new GridBagConstraints();

    EventMainPanel() {
        this.setLayout(new GridBagLayout());

        DateTime datetime = new DateTime();
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(datetime, gbc);

        FriendField friendField = new FriendField();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.4;
        this.add(friendField, gbc);

        LocationField locationField = new LocationField();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        this.add(locationField, gbc);

        Description description = new Description();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        this.add(description, gbc);

        Reminder reminder = new Reminder();
        gbc.gridx = 0;
        gbc.gridy = 5;
        this.add(reminder, gbc);

        Priority priority = new Priority();
        gbc.gridx = 0;
        gbc.gridy = 6;
        this.add(priority, gbc);

    }
}

class DateTime extends JPanel {
    JLabel dateLabel = new JLabel();
    JLabel timeLabel = new JLabel();
    static TestPane startTime = new TestPane();
    static TestPane endTime = new TestPane();
    JLabel iconLabel;
    ImageIcon icon;
    UtilDateModel model = new UtilDateModel();
    // Properties for JDateTimePicker
    Properties p = new Properties();
    // Variable to store return value from jdatepicker
    Date date;
    static JDatePickerImpl datePicker;

    DateTime() {
        this.setLayout(new FlowLayout());
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
        model.setDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
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
        int year = temp.getYear();
        String string = String.format("%d-%d-%d %d:%d", date, month + 1, year + 1900, h, m);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return sdf.parse(string);
    }
}

class FriendField extends JPanel {
    static JTextField friendField = new JTextField(50);
    ImageIcon icon;
    JLabel iconLabel;
    static ArrayList<String> participants = new ArrayList<String>();
    static JList<String> list = new JList<String>();
    private JButton deleteParticipantButton = new JButton("remove");

    // private static JLabel participantsLabel = new JLabel();
    FriendField() {
        this.setLayout(new FlowLayout());
        try {
            icon = new ImageIcon(getClass().getResource("Images/friendsicon.jpg"));
            Image image = icon.getImage();
            Image newing = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newing);
            iconLabel = new JLabel(icon);
            this.add(iconLabel);

        } catch (Exception e) {
            System.out.print("Image can not be found");
        }
        friendField.setText("Participants");
        friendField.addFocusListener(new FocusListener() {
            String friend = "";

            @Override
            public void focusGained(FocusEvent e) {
                friendField.setText(friend);
            }

            @Override
            public void focusLost(FocusEvent e) {
                friend = friendField.getText();
                if (friend.equals("")) {
                    friendField.setText("Participants");
                }
            }
        });
        friendField.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (RegisterValidator.isValidEmail(friendField.getText())) {
                        System.out.println("Participants: " + friendField.getText());
                        if (participants.contains(friendField.getText())) {
                            JOptionPane.showMessageDialog(e.getComponent(), "You have already added this friend");
                        } else {
                            participants.add(friendField.getText());
                            updateParticipants(friendField.getText());
                            list.setListData(participants.toArray(new String[participants.size()]));
                            friendField.setText("");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid email address");
                    }
                }
            }

            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }
        });

        deleteParticipantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (list.getSelectedIndex() != -1) {
                    participants.remove(list.getSelectedIndex());
                    updateParticipants(null);
                    list.setListData(participants.toArray(new String[participants.size()]));
                }
            }
        });

        this.add(friendField);
        this.add(list);
        this.add(deleteParticipantButton);
        deleteParticipantButton.setVisible(false);
    }

    public static ArrayList<String> getParticipants() {
        return participants;
    }

    public void updateParticipants(String participant) {
        System.out.println("Participant label: " + participant);
        JLabel participantLabel = new JLabel(participant);
        list.add(participantLabel);
        if (list.getModel().getSize() >= 0) {
            deleteParticipantButton.setEnabled(true);
            deleteParticipantButton.setVisible(true);
        } else {
            deleteParticipantButton.setEnabled(false);
            deleteParticipantButton.setVisible(false);
        }
    }
}

class LocationField extends JPanel {
    static JTextField locationField = new JTextField(50);
    ImageIcon icon;
    JLabel iconLabel;

    LocationField() {
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
        locationField.setText("Location");
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
                    locationField.setText("Location");
                }
            }
        });
        this.add(locationField);
    }

    static String getLoc() {
        if (locationField.getText().equals("Location")) {
            return "";
        } else {
            return locationField.getText();
        }
    }
}

class Reminder extends JPanel {
    JLabel reminderLabel = new JLabel("Remind before: ");
    static String[] arr = { "No remind", "1 minute", "3 hour", "3 days", "1 week" };
    static JComboBox<String> reminderComboBox = new JComboBox<String>(arr);

    Reminder() {
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

class Priority extends JPanel {
    JLabel priorityLabel = new JLabel("Priority");
    static String[] arr = { "High", "Medium", "Low" };
    static JComboBox<String> priorityComboBox = new JComboBox<String>(arr);

    Priority() {
        this.add(priorityLabel);
        this.add(priorityComboBox);
    }

    static int getPriority() {
        return priorityComboBox.getSelectedIndex();
    }
}

class Description extends JPanel {
    static JTextField descripField = new JTextField(50);
    ImageIcon icon;
    JLabel iconLabel;

    Description() {
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
        descripField.setText("Description");
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
                    descripField.setText("Description");
                }
            }
        });
        this.add(descripField);
    }

    static String getDescription() {
        if (descripField.getText().equals("Description")) {
            return "";
        } else {
            return descripField.getText();
        }
    }
}

class SetBtn extends JPanel {
    JButton setBtn = new JButton("Set");

    SetBtn(User user, SwingCalendar calendar, AddEventDialog dialog) {
        this.add(setBtn);
        setBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (DateTime.getDuration() <= 0) {
                    JOptionPane.showMessageDialog((JButton) e.getSource(),
                            "Please select end time greater than start time!");
                    return;
                } else {
                    System.out.println("Duration: " + DateTime.getDuration());
                }
                // setEvent(TitlePanel.getTitle(), DateTime.getDateTime(),
                // LocationField.getLoc(), DateTime.getDuration(), Priority.getPriority(),
                // Reminder.getRemind());
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
                    Event event = new Event(user.getId(),
                            TitlePanel.getTitle(),
                            Description.getDescription(),
                            FriendField.getParticipants(),
                            DateTime.getDate(),
                            Reminder.getRemind(),
                            LocationField.getLoc(),
                            DateTime.getDuration(),
                            Priority.getPriority());
                    if (Database.addEvent(event)) {
                        JOptionPane.showMessageDialog((JButton) e.getSource(), "Event added successfully!");
                        calendar.cal.add(Calendar.MONTH, +1);
                        calendar.update();
                        calendar.cal.add(Calendar.MONTH, -1);
                        calendar.update();
                        new Thread(new Runnable() {
                            public void run() {
                                Mail.sendRemindEmail(user, event); // send email right after event is created
                            }
                        }).start();
                        if (event.getRemind().compareTo(event.getDate()) < 0) {
                            System.out.println("Remind set");
                            SchedulerJava.scheduleMail(user, event);
                        }
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog((JButton) e.getSource(), "Fail to add event");
                    }
                } catch (Exception e1) {
                    System.out.println("Parse Error");
                }
            }

        });
    }
}

// Combobox display time
class TestPane extends JPanel {
    JComboBox<Date> cb;
    int index = 0;

    public TestPane() {
        setLayout(new GridBagLayout());
        Calendar cal = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

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
        cb.setSelectedIndex(index + 1);

        add(cb);
    }

    Date getPickedTime() {
        return (Date) cb.getSelectedItem();
    }
}

// Render combobox to display HH:mm
class DateComboBoxRenderer extends DefaultListCellRenderer {
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
