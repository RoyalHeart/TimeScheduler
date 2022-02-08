package src;

// import oracle.jdbc.proxy.annotation.OnError;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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

/**
 * {@code EditEventDialog} is used to contains the title, time and button to delete and edit event of {@code Event}.
 * <p>
 * This class contains:
 * <p>
 * - An {@code Event}
 * <p>
 * - A dialog of itself
 * <p>
 * - A panel that opens this dialog
 * <p>
 * - A start time of event
 * <p>
 * - A end time of event
 * 
 * @author Sang Doan Tan 1370137
 */
public class EditEventDialog extends JDialog {
    private Event event;
    private JDialog dialog;
    private JPanel panel;
    private String timeStart;
    private String timeEnd;
    private User user;
    private SwingCalendar swingCalendar;

    /**
     * Constructor that creates {@code EditEventDialog} object.
     * 
     * @param event         The event that needed to be edit
     * @param timeStart     The start time of event
     * @param timeEnd       The end time of event
     * @param panel         The panel that opened this dialog
     */
    EditEventDialog(Event event, String timeStart, String timeEnd, JPanel panel, User user, SwingCalendar calendar) {
        super(SwingUtilities.windowForComponent(panel));
        this.event = event;
        this.dialog = this;
        this.panel = panel;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.user = user;
        swingCalendar = calendar;
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

    /**
     * {@code DisplayPanel} is used to contain the title and the time of {@code Event}.
     * <p>
     * This class contains:
     * <p>
     * - A title of {@code Event}
     * <p>
     * - A duration of {@code Event}
     * 
     * @author Sang Doan Tan 1370137 
     */
    class displayPanel extends JPanel {
        JLabel title = new JLabel();
        JLabel time = new JLabel();
        GridBagConstraints gbc = new GridBagConstraints();

        displayPanel() {
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

    /**
     * {@code Btn} is used to contain the edit and delete button of {@code Event}.
     * <p>
     * This class contains:
     * <p>
     * - A edit button
     * <p>
     * - A delete button
     * 
     * @author Sang Doan Tan 1370137
     */
    class Btn extends JPanel {
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");

        Btn() {
            this.setLayout(new FlowLayout());
            this.add(editBtn);
            this.add(delBtn);

            try {
                delBtn.addActionListener(e -> {
                    if (Database.delEvent(event)) {
                        SchedulerJava.unscheduleMail(event);
                        swingCalendar.update();
                        new Thread(new Runnable() {
                            public void run() {
                                // send email when event is deleted
                                Mail.sendDeletedEmail(user, event);
                            }
                        }).start();
                        JOptionPane.showMessageDialog(null, "Event deleted successfully.");
                        dialog.dispose();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                editBtn.addActionListener(e -> {
                    dialog.dispose();
                    new MainPanel(event, panel, timeStart, timeEnd, user);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@code MainPanel} is used to contain all panels that needed to edit for {@code Event}
     * 
     * @author Sang Doan Tan 1370137
     */
    class MainPanel extends JDialog {

        /**
         * Constructor that creates {@code MainPanel} object.
         * 
         * @param event         the event that will be edited
         * @param panel         the panel that opened this dialog
         * @param timeStart     the start time of event
         * @param timeEnd       the end time of event
         * @param user          the user of the event
         */
        MainPanel(Event event, JPanel panel, String timeStart, String timeEnd, User user) {
            super(SwingUtilities.windowForComponent(panel));
            this.setTitle("Event");
            this.setSize(800, 500);
            this.setLayout(new BorderLayout());
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            this.setLocationRelativeTo(null);
            this.add(new TitlePanel(event), BorderLayout.NORTH);
            this.add(new EventMainPanel(event, timeStart, timeEnd), BorderLayout.CENTER);
            this.add(new EditBtn(event, this), BorderLayout.SOUTH);
            this.setResizable(false);
            this.setVisible(true);
        }
    }

    /**
     * {@code TitlePanel} is used to contain the label and the text field of title for editing {@code Event}.
     * <p>
     * This class contains:
     * <p>
     * - A static titleField
     * 
     * @author Sang Doan Tan 1370137
     */
    static class TitlePanel extends JPanel {
        static JTextField titleField = new JTextField(50);

        // Constructor for edit event
        /**
         * Constructor that creates {@code TitlePanel} object for editing {@code Event}.
         * 
         * @param event The event that will be edited.
         */
        TitlePanel(Event event) {
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

    /**
     * {@code EventMainPanel} is used to contain {@link DateTime} panel, {@link FiendField} panel, 
     * {@link LocationField} panel, {@link Description} panel, {@link Reminder} panel and {@link Priority} panel for editing {@code Event}.
     * 
     * @author Sang Doan Tan 1370137
     */
    class EventMainPanel extends JPanel {
        GridBagConstraints gbc = new GridBagConstraints();

        /**
         * Constructor that creates {@code EventMainPanel} object.
         * 
         * @param event         The event that will be edited.
         * @param timeStart     The start time of event.
         * @param timeEnd       The end time of event.
         */
        EventMainPanel(Event event, String timeStart, String timeEnd) {
            this.setLayout(new GridBagLayout());

            DateTime datetime = new DateTime(event, timeStart, timeEnd);
            gbc.gridx = 0;
            gbc.gridy = 1;
            this.add(datetime, gbc);

            FriendField friendField = new FriendField(event);
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 0.5;
            this.add(friendField, gbc);

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

    /**
     * {@code DateTime} is used to contain all labels, text fields that relates to {@code DateTime} and {@code TimeComboBox} for editing event.
     * <p>
     * This class contains:
     * <p>
     * - A date label
     * <p>
     * - A time label
     * <p>
     * - A {@code TimeComboBox} for start time
     * <p>
     * - A {@code TimeComboBox} for end time
     * <p>
     * - A icon label for {@code Calendar}
     * <p>
     * - A image icon 
     * <p>
     * - A static start time of {@code Event}
     * <p>
     * - A static end time of {@code Event}
     * 
     * @author Sang Doan Tan 1370137
     */
    static class DateTime extends JPanel {
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

        /**
         * Constructor that creates {@code DateTime} object for editing {@code Event}.
         * 
         * @param event         The event that will be deleted.
         * @param timeStart     The start time of event.
         * @param timeEnd       The end time of event.
         */
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
            model.setDate(event.getDate().getYear() + 1900, event.getDate().getMonth(), event.getDate().getDate());
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

        /**
         * This method will take the {@code Date} that the user has picked from the {@link Calendar}.
         * 
         * @return  The selected {@code Date} or current {@code Date} if not selected
         */
        static Date getDateTime() {
            // if Date is not selected will take current date
            if ((Date) datePicker.getModel().getValue() == null) {
                return new Date();
            }
            // return selected date
            return (Date) datePicker.getModel().getValue();
        }

        // return diff between from and to time
         /**
         * Calculate the difference between the start time of the {@code Event} and 
         * the end time of the {@code Event} to find duration.
         * 
         * @return  Duration of the {@link Event}
         */
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
         /**
         * Combine selected {@code Date} of the {@code Event} and the start time of the {@code Event} into 1 variable {@code Date}.
         * 
         * @return the {@code Date} and the start time of the {@link Event}
         * @throws ParseException
         */
        static Date getDate() throws ParseException {
            int h = startTime.getPickedTime().getHours();
            int m = startTime.getPickedTime().getMinutes();
            Date temp = getDateTime();
            int month = temp.getMonth() + 1;
            int date = temp.getDate();
            int year = temp.getYear() + 1900;
            String string = String.format("%d-%d-%d %d:%d", date, month, year, h, m);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            return sdf.parse(string);
        }
    }

    static class FriendField extends JPanel {
        static JTextField friendField = new JTextField(50);
        ImageIcon icon;
        JLabel iconLabel;
        static ArrayList<String> participants = new ArrayList<String>();
        static JList<String> list = new JList<String>();
        private JButton deleteParticipantButton = new JButton("remove");

        // private static JLabel participantsLabel = new JLabel();
        FriendField(Event event) {
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
            this.add(friendField);
            friendField.addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (RegisterValidator.isValidEmail(friendField.getText())) {
                            System.out.println("Participant: " + friendField.getText());
                            if (participants.contains(friendField.getText())) {
                                JOptionPane.showMessageDialog(e.getComponent(),
                                        "You have already added this participant");
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
            if (event.getParticipants() != null) {
                participants = event.getParticipants();
                list.setListData(participants.toArray(new String[participants.size()]));
                deleteParticipantButton.setVisible(true);
            } else {
                participants = new ArrayList<String>();
                list.setListData(participants.toArray(new String[participants.size()]));
                deleteParticipantButton.setVisible(false);
            }
            this.add(list);
            this.add(deleteParticipantButton);
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

    /**
     * {@code LocationField} is used to contains icon, label and text field of location of {@code Event} for editing {@code Event}.
     * <p>
     * The class contains:
     * <p>
     * - A static location field
     * <p>
     * - A icon label
     * <p>
     * - A icon
     * 
     * @author Sang Doan Tan 1370137
     */
    static class LocationField extends JPanel {
        static JTextField locationField = new JTextField(50);
        ImageIcon icon;
        JLabel iconLabel;

        /**
         * Constructor that creates {@code LocationField} object.
         * 
         * @param event The event that will be edited.
         */
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

    /**
     * {@code Description} is used to contain icon, label and text field of description of {@code Event} for editing {@code Event}.
     * <p>
     * This class contains:
     * <p>
     * - A static {@code JTextField}
     * <p>
     * - A icon
     * <p>
     * - A icon label
     * 
     * @author Sang Doan Tan 1370137
     */
    static class Description extends JPanel {
        static JTextField descripField = new JTextField(50);
        ImageIcon icon;
        JLabel iconLabel;

        /**
         * Constructor that creates {@code Description} object for editing {@code Event}.
         * 
         * @param event The event that will be edited.
         */
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

    /**
     * {@code Reminder} is used to contains a {@code JComboBox} for user to select remind options for editing {@code Event}.
     * <p>
     * This class contains:
     * <p>
     * - A label
     * <p>
     * - A static {@code String[]} for remind options.
     * <p>
     * - A static {@code JComboBox} to select.
     * 
     * @author Sang Doan Tan 1370137
     */
    static class Reminder extends JPanel {
        JLabel reminderLabel = new JLabel("Remind before: ");
        static String[] arr = { "No remind", "1 minute", "3 hour", "3 days", "1 week" };
        static JComboBox<String> reminderComboBox = new JComboBox<String>(arr);

        Reminder(Event event) {
            this.add(reminderLabel);
            this.add(reminderComboBox);
        }

         /**
         * This method will take the {@code Date} of the {@code Event} 
         * and then minus the selected remind option to find the {@code Date} that need to remind to user.
         * 
         * @return {@code Date} of the remind time.
         */
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
    /**
     * {@code TimeComboBox} is used to contains the {@code JComboBox} to select the time of the {@code Event} for editing {@code Event}.
     * 
     * @author Sang Doan Tan 1370137 
     */
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
            String string = String.format("%d-%d-%d %s", cal.getTime().getDate(), cal.getTime().getMonth() + 1,
                    cal.getTime().getYear() + 1900, time);
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

    /**
     * {@code Priority} is used to contains a {@code JComboBox} for user to select the priority for editing {@code Event}.
     * <p>
     * This class contains:
     * <p>
     * - A label
     * <p>
     * - A static {@code String[]} for priority options.
     * <p>
     * - A static {@code JComboBox} for user to select.
     *
     * @author Sang Doan Tan 1370137
     */
    static class Priority extends JPanel {
        JLabel priorityLabel = new JLabel("Priority");
        static String[] arr = { "High", "Medium", "Low" };
        static JComboBox<String> priorityComboBox = new JComboBox<String>(arr);

        /**
         * Constructor that creates {@code Priority} object for editing {@code Event}.
         * 
         * @param event The event that will be edited.
         */
        Priority(Event event) {
            priorityComboBox.setSelectedIndex(event.getPriority());
            this.add(priorityLabel);
            this.add(priorityComboBox);
        }

        static int getPriority() {
            return priorityComboBox.getSelectedIndex();
        }
    }

    /**
     * {@code EdiBtn} is used to contain the button to save the change of editing {@code Event} to database.
     * 
     * <p>
     * This class contains:
     * <p>
     * - A set button
     * <p>
     * 
     * @author Sang Doan Tan 1370137
     */
    class EditBtn extends JPanel {
        JButton editBtn = new JButton("Save");

         /**
         * Constructor that creates {@code EditBtn} object.
         * 
         * @param event The event that will be edited.
         */
        EditBtn(Event event, MainPanel mainPanel) {
            try {
                editBtn.addActionListener(e -> {
                    if (DateTime.getDuration() <= 0) {
                        JOptionPane.showMessageDialog((JButton) e.getSource(),
                                "Please select end time greater than start time!");
                        return;
                    } else {
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
                            newEvent.setParticipants(FriendField.getParticipants());
                            if (Database.updateEvent(newEvent)) {
                                JOptionPane.showMessageDialog((JButton) e.getSource(), "Event updated successfully!");
                                swingCalendar.cal.add(Calendar.MONTH, +1);
                                swingCalendar.update();
                                swingCalendar.cal.add(Calendar.MONTH, -1);
                                swingCalendar.update();
                                if (event.getRemind().compareTo(event.getDate()) < 0) {
                                    SchedulerJava.unscheduleMail(event);
                                }
                                if (newEvent.getRemind().compareTo(newEvent.getDate()) < 0) {
                                    SchedulerJava.scheduleMail(user, newEvent);
                                }
                                new Thread(new Runnable() {
                                    public void run() {
                                        // send email right after event is edited
                                        Mail.sendEditEmail(user, event, newEvent);
                                    }
                                }).start();
                                mainPanel.dispose();
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
    /**
     * {@code DateComboBoxRenderer} is used to render {@code TimeComboBox} to display ("HH::mm") format.
     * <p>
     * This class contains:
     * <p>
     * - A date format 
     * 
     * @author Sang Doan Tan 1370137
     */
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
    /**
     * {@code DateLabelFormatter} is used to format day in {@code JDateTimePicker}.
     * 
     * @author Sang Doan Tan 1370137
     */
    static class DateLabelFormatter extends AbstractFormatter {

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



