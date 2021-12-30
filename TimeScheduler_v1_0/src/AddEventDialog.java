package src;

import javax.swing.*;
// import javax.tools.DocumentationTool.Location;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.event.MouseInputAdapter;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import java.awt.*;
import java.text.ParseException;
// import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class AddEventDialog extends JDialog {

    AddEventDialog() 
    {
        this.setTitle("Event");
        this.setSize(800, 500);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.add(new TitlePanel(), BorderLayout.NORTH);
        this.add(new EventMainPanel(), BorderLayout.CENTER);
        JButton btnSetEvent = new JButton("Set");
        this.add(btnSetEvent, BorderLayout.SOUTH);
        this.setResizable(false);
        this.setVisible(true);
    }

    public static void main(String[] args) 
    {
        new AddEventDialog();
    }

}

class TitlePanel extends JPanel 
{
    TitlePanel() 
    {
        this.setLayout(new FlowLayout());
        JTextField titleField = new JTextField(50);
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
        
        Reminder reminder = new Reminder();
        gbc.gridx = 0;
        gbc.gridy = 4;
        this.add(reminder, gbc);
    }
}

class DateTime extends JPanel 
{
    JTextField dateField = new JTextField(10);
    JTextField timeFromField = new JTextField(6);
    JTextField timeToField = new JTextField(6);
    JLabel dateLabel = new JLabel();
    JLabel timeLabel = new JLabel();
    JLabel iconLabel;
    ImageIcon icon;
    UtilDateModel model = new UtilDateModel();
    // Properties for JDateTimePicker
    Properties p = new Properties();

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
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        this.add(datePicker);
        timeLabel.setText("Time");
        this.add(timeLabel);
        this.add(new TestPane());
        this.add(new TestPane());
    }
}

class FriendField extends JPanel 
{
    JTextField friendField = new JTextField(50);
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
}

class LocationField extends JPanel
{
    JTextField locationField = new JTextField(50);
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
}

class Reminder extends JPanel 

    {
    JLabel reminderLabel = new JLabel("Remind before: ");
    String[] arr = {"No remind", "1 minute", "3 hour", "3 days", "1 week"};
    JComboBox<String> reminderComboBox = new JComboBox<String>(arr);

    Reminder() 
    {
        this.add(reminderLabel);
        this.add(reminderComboBox);
    }
}

// Combobox display time
class TestPane extends JPanel {

    public TestPane() {
        setLayout(new GridBagLayout());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        Calendar end = Calendar.getInstance();
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        DefaultComboBoxModel<Date> model = new DefaultComboBoxModel<>();
        do {
            model.addElement(calendar.getTime());
            calendar.add(Calendar.MINUTE, 15);
        } while (calendar.getTime().before(end.getTime()));

        JComboBox<Date> cb = new JComboBox<>(model);
        cb.setRenderer(new DateComboBoxRenderer());

        add(cb);

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



