package src;

import javax.swing.*;
// import javax.tools.DocumentationTool.Location;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.event.MouseInputAdapter;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

// import oracle.jdbc.proxy.annotation.OnError;

import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
// import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;

public class AddEventDialog extends JDialog {

    AddEventDialog(User user) 
    {
        this.setTitle("Event");
        this.setSize(800, 500);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.add(new TitlePanel(), BorderLayout.NORTH);
        this.add(new EventMainPanel(), BorderLayout.CENTER);
        this.add(new SetBtn(user.getId()), BorderLayout.SOUTH);
        this.setResizable(false);
        this.setVisible(true);
    }

    public static void main(String[] args) 
    {
        //new AddEventDialog(user);
    }

}

class TitlePanel extends JPanel 
{
    static JTextField titleField = new JTextField(50);
    TitlePanel() 
    {
        this.setLayout(new FlowLayout());
        JLabel titleLabel = new JLabel("Title");
        titleField.setText("Title");
        titleField.selectAll();

        // 1 click to select all title
        titleField.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e)
            {
                if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton((java.awt.event.MouseEvent) e)) 
                {
                    titleField.selectAll();
                }
            }
        });
        this.add(titleLabel);
        this.add(titleField);
    }

    static String getTitle() 
    {
        return titleField.getText();
    }
}

class EventMainPanel extends JPanel
{       
    GridBagConstraints gbc = new GridBagConstraints();
    
    EventMainPanel() 
    {
        this.setLayout(new GridBagLayout());

        DateTime datetime = new DateTime();
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(datetime, gbc);

        FriendField friendField = new FriendField();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.weightx = 0.5;
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

class DateTime extends JPanel 
{
    JTextField dateField = new JTextField(10);
    JTextField timeFromField = new JTextField(6);
    JTextField timeToField = new JTextField(6);
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
    static Date date;
    static JDatePickerImpl datePicker;

    DateTime()
    {
        this.setLayout(new FlowLayout());
        try {
            icon = new ImageIcon(getClass().getResource("Images/icondatetime.png"));
            Image image = icon.getImage();
            Image newing = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newing);
            iconLabel = new JLabel(icon);
            this.add("Hello", iconLabel);
        } catch(Exception e) {
            System.out.println("Image cannot be found");
        }
        dateLabel.setText("Date");
        this.add(dateLabel);
        // Implements properties
        p.put("text.month", "Month");
        p.put("text.today", "Today");
        p.put("text.year", "Year");
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

    static Date getDateTime() 
    {
        // if Date is not selected will take current date
        if ((Date)datePicker.getModel().getValue() == null) {return new Date();}
        // return selected date
        return (Date)datePicker.getModel().getValue();
    }

    // return diff between from and to time
    static int getDuration()
    {
         // SimpleDateFormat format = new SimpleDateFormat("HH:mm");  
        Date before = startTime.getPickedTime();
        Date after = endTime.getPickedTime();
        /* try {
            before = format.parse(before);
            after = format.parse(after);
        } catch (ParseException e) {
            e.printStackTrace();
        } */   
        long diff = after.getTime() - before.getTime();
        // convert diff time to minutes
        int minutes = (int)TimeUnit.MILLISECONDS.toMinutes(diff); 

        return minutes;
    }

    // combine date and startTime to 1 variable Date
    static Date getDate() throws ParseException 
    {
        int h = startTime.getPickedTime().getHours();
        int m = startTime.getPickedTime().getMinutes();
        Date temp = getDateTime();
        int month = temp.getMonth();
        int date = temp.getDate();
        int year = temp.getYear();
        String string = String.format("%d-%d-%d %d:%d", date, month+1, year+1900, h, m);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return sdf.parse(string);
    }
}

class FriendField extends JPanel 
{
    static JTextField friendField = new JTextField(50);
    ImageIcon icon;
    JLabel iconLabel;

    FriendField() 
    {
        this.setLayout(new FlowLayout());
        try {
            icon = new ImageIcon(getClass().getResource("Images/friendsicon.jpg"));
            Image image = icon.getImage();
            Image newing = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newing);
            iconLabel = new JLabel(icon);
            this.add(iconLabel);
        } catch(Exception e) {
            System.out.print("Image can not be found");
        }

        this.add(friendField);
    }

    static String getFriend()
    {
        return friendField.getText();
    }
}

class LocationField extends JPanel
{
    static JTextField locationField = new JTextField(50);
    ImageIcon icon;
    JLabel iconLabel;

    LocationField() 
    {
        this.setLayout(new FlowLayout());
        try {
            icon = new ImageIcon(getClass().getResource("Images/locationicon.jpg"));
            Image image = icon.getImage();
            Image newing = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newing);
            iconLabel = new JLabel(icon);
            this.add(iconLabel);
        } catch(Exception e) {
            System.out.println("Image can not be found");
        }

        this.add(locationField);
    }

    static String getLoc()
    {
        return locationField.getText();
    }
}

class Reminder extends JPanel 

    {
    JLabel reminderLabel = new JLabel("Remind before: ");
    static String[] arr = {"No remind", "1 minute", "3 hour", "3 days", "1 week"};
    static JComboBox<String> reminderComboBox = new JComboBox<String>(arr);

    Reminder() 
    {
        this.add(reminderLabel);
        this.add(reminderComboBox);
    }

    static int getRemind() 
    {
        return reminderComboBox.getSelectedIndex();
    }
}

class Priority extends JPanel 
{
    JLabel priorityLabel = new JLabel("Priority");
    static String[] arr = {"High", "Medium", "Low"};
    static JComboBox<String> priorityComboBox = new JComboBox<String>(arr);
    Priority() 
    {
        this.add(priorityLabel);
        this.add(priorityComboBox);
    }

    static int getPriority()
    {
        return priorityComboBox.getSelectedIndex();
    }
}

class Description extends JPanel
{
    static JTextField descripField = new JTextField(50);
    ImageIcon icon;
    JLabel iconLabel;
    Description() 
    {
        this.setLayout(new FlowLayout());
        try {
            icon = new ImageIcon(getClass().getResource("Images/descriptionicon.png"));
            Image image = icon.getImage();
            Image newing = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newing);
            iconLabel = new JLabel(icon);
            this.add(iconLabel);
        } catch(Exception e) {
            System.out.println("Image can not be found.");
        }
        this.add(descripField);
    }

    static String getDescription() 
    {
        return descripField.getText();
    }
}

class SetBtn extends JPanel
{
    JButton setBtn = new JButton("Set");
    String userID;
    SetBtn(String userID) 
    {
        this.add(setBtn);
        this.userID = userID;
        setBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // setEvent(TitlePanel.getTitle(), DateTime.getDateTime(), LocationField.getLoc(), DateTime.getDuration(), Priority.getPriority(), Reminder.getRemind());
                System.out.println(TitlePanel.getTitle());
                System.out.println(DateTime.getDateTime());
                System.out.println(LocationField.getLoc());
                System.out.println(DateTime.getDuration());
                System.out.println(Priority.getPriority());
                System.out.println(Reminder.getRemind());
                System.out.println(Description.getDescription());
                try {
                    System.out.println(DateTime.getDate());
                } catch (ParseException e2) {
                    e2.printStackTrace();
                }
            }

        });
    }
}

// Combobox display time
class TestPane extends JPanel {
    JComboBox<Date> cb;

    public TestPane() {
        setLayout(new GridBagLayout());
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
        } while (calendar.getTime().before(end.getTime()));

        cb = new JComboBox<Date>(model);
        cb.setRenderer(new DateComboBoxRenderer());

        add(cb);
    }

    Date getPickedTime()
    {
        return (Date)cb.getSelectedItem();
    }
}
        
/*class DateFormattedListCellRenderer extends DefaultListCellRenderer {

    private DateFormat format;

    public DateFormattedListCellRenderer(DateFormat format) {
        this.format = format;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof Date) {
            value = format.format((Date) value);
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}*/

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










