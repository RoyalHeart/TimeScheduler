package src;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.*;

public class ShowEvents {
    public static ArrayList<List<MonthCell>> updateDataEvent(ArrayList<List<MonthCell>> returnData, Calendar cal,
            User user) {
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int month = cal.get(Calendar.MONTH);
        System.out.println(month);
        int year = cal.get(Calendar.YEAR);
        System.out.println(year);
        int startDay = cal.get(Calendar.DAY_OF_WEEK);
        int numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
        int day = 1;
        ArrayList<List<MonthCell>> data = new ArrayList<List<MonthCell>>();
        for (int i = 0; i < weeks; i++) {
            List<MonthCell> list = new ArrayList<MonthCell>();
            for (int j = 0; j < 7; j++) {
                if (i == 0 && j < startDay - 1) {
                    list.add(new MonthCell());
                } else if (day > numberOfDays) {
                    list.add(new MonthCell());
                } else {
                    list.add(new MonthCell(String.valueOf(day)));
                    day++;
                }
            }
            data.add(list);
        }
        ArrayList<Event> events = Database.getEvents(user);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        DateFormat yearFormat = new SimpleDateFormat("yyyy");
        DateFormat monthFormat = new SimpleDateFormat("MM");
        DateFormat dayFormat = new SimpleDateFormat("dd");
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for (Event event : events) {
            int eventYear = Integer.valueOf(yearFormat.format(event.getDate()));
            int eventMonth = Integer.valueOf(monthFormat.format(event.getDate()));
            if (eventYear == year && eventMonth == month + 1) {
                Calendar calendar = new GregorianCalendar();
                System.out.println("Title: " + event.getTitle());
                System.out.println("Date: " + dateFormat.format(event.getDate()));
                System.out.println("Start time: " + timeFormat.format(event.getDate()));
                System.out.println("Duration: " + event.getDuration());
                System.out.println("Priority: " + event.getPriority());
                calendar.setTime(event.getDate());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                int week = calendar.get(Calendar.WEEK_OF_MONTH);
                System.out.println("Date of Month: " + calendar.get(Calendar.DAY_OF_WEEK));
                System.out.println("Week: " + calendar.get(Calendar.WEEK_OF_MONTH) + "\n");
                data.get(week - 1).get(dayOfWeek - 1).addEvent(event);
            }
        }
        return data;
    };

    public static void main(String[] args) {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int month = cal.get(Calendar.MONTH);
        System.out.println(month);
        int year = cal.get(Calendar.YEAR);
        System.out.println(year);
        int startDay = cal.get(Calendar.DAY_OF_WEEK);
        int numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
        int day = 1;
        ArrayList<List<MonthCell>> data = new ArrayList<List<MonthCell>>();
        for (int i = 0; i < weeks; i++) {
            List<MonthCell> list = new ArrayList<MonthCell>();
            for (int j = 0; j < 7; j++) {
                if (i == 0 && j < startDay - 1) {
                    list.add(new MonthCell());
                } else if (day > numberOfDays) {
                    list.add(new MonthCell());
                } else {
                    list.add(new MonthCell(String.valueOf(day)));
                    day++;
                }
            }
            data.add(list);
        }
        Database.createConnection();
        User user = Database.getUser("admin", "D82494F05D6917BA02F7AAA29689CCB444BB73F20380876CB05D1F37537B7892");
        ArrayList<Event> events = Database.getEvents(user);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        DateFormat yearFormat = new SimpleDateFormat("yyyy");
        DateFormat monthFormat = new SimpleDateFormat("MM");
        DateFormat dayFormat = new SimpleDateFormat("dd");
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for (Event event : events) {
            int eventYear = Integer.valueOf(yearFormat.format(event.getDate()));
            int eventMonth = Integer.valueOf(monthFormat.format(event.getDate()));
            if (eventYear == year && eventMonth == month + 1) {
                Calendar calendar = new GregorianCalendar();
                System.out.println("Title: " + event.getTitle());
                System.out.println("Date: " + dateFormat.format(event.getDate()));
                System.out.println("Start time: " + timeFormat.format(event.getDate()));
                System.out.println("Duration: " + event.getDuration());
                System.out.println("Priority: " + event.getPriority());
                calendar.setTime(event.getDate());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                int week = calendar.get(Calendar.WEEK_OF_MONTH);
                System.out.println("Date of Month: " + calendar.get(Calendar.DAY_OF_WEEK));
                System.out.println("Week: " + calendar.get(Calendar.WEEK_OF_MONTH) + "\n");
                data.get(week - 1).get(dayOfWeek - 1).addEvent(event);
            }
        }
        JFrame frame = new JFrame("Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        for (int i = 0; i < weeks; i++) {
            for (int j = 0; j < 7; j++) {
                for (int t = 0; t < data.get(i).get(j).getEvents().size(); t++) {
                    System.out.println(data.get(i).get(j).getEvents().get(t).getDate());
                    System.out.println(data.get(i).get(j).getEvents().get(t).getTitle());
                }
            }
            System.out.println();
        }
        MyClassTableModel model = new MyClassTableModel(data);
        JTable table = new JTable(model);
        table.setRowHeight(100);
        table.setDefaultRenderer(MonthCell.class, new MyClassCellRenderer());
        table.setDefaultEditor(MonthCell.class, new MyClassCellEditor());
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);
    }
}
