package src;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import src.database.Database;

/**
 * This class is used to get {@link Event} from the {@link Database}
 * and change the events to corresponding datatype.
 * 
 * @author Tam Thai Hoang
 */
public class LoadEvents {
    // private static DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static DateFormat weekFormat = new SimpleDateFormat("ww");
    private static DateFormat monthFormat = new SimpleDateFormat("MM");
    private static DateFormat yearFormat = new SimpleDateFormat("yyyy");
    private static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    /**
     * Get the month data from the database and return it as a list of MonthCell
     * objects to show to the {@link SwingCalendar}.
     * 
     * @param data An empty {@link ArrayList} of {@link MonthCell} objects to be
     *             filled with the data
     *             from the {@link Database}
     * @param cal  The {@link Calendar} object to get the month data from
     * @param user The {@link User} object to get the events from
     * @return The list of MonthCell objects to show to the {@link SwingCalendar}
     */
    public static ArrayList<List<MonthCell>> updateMonthDataEvent(ArrayList<List<MonthCell>> data, Calendar cal,
            User user) {
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int startDay = cal.get(Calendar.DAY_OF_WEEK);
        int numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
        int day = 1;
        data = new ArrayList<List<MonthCell>>();
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
        for (Event event : events) {
            int eventYear = Integer.valueOf(yearFormat.format(event.getDate()));
            int eventMonth = Integer.valueOf(monthFormat.format(event.getDate()));
            if (eventYear == year && eventMonth == month + 1) {
                Calendar calendar = new GregorianCalendar();
                // System.out.println("Title: " + event.getTitle());
                // System.out.println("Date: " + dateFormat.format(event.getDate()));
                // System.out.println("Start time: " + timeFormat.format(event.getDate()));
                // System.out.println("Duration: " + event.getDuration());
                // System.out.println("Priority: " + event.getPriority());
                calendar.setTime(event.getDate());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                int week = calendar.get(Calendar.WEEK_OF_MONTH);
                // System.out.println("Date of Month: " + calendar.get(Calendar.DAY_OF_WEEK));
                // System.out.println("Week: " + calendar.get(Calendar.WEEK_OF_MONTH) + "\n");
                data.get(week - 1).get(dayOfWeek - 1).addEvent(event);
            }
        }
        return data;
    };

    /**
     * Get the week data from the database and return it as a list of WeekCell
     * objects to show to the {@link SwingCalendar}.
     * 
     * @param weekData The empty list of WeekCell objects to be filled with the data
     *                 from the {@link Database}
     * @param cal      The {@link Calendar} object to get the month data from
     * @param user     The {@link User} object to get the events from
     * @return The list of WeekCell objects to show to the SwingCalendar
     */
    public static ArrayList<WeekCell> updateWeekDataEvent(ArrayList<WeekCell> weekData, Calendar cal, User user) {
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        System.out.println("Load Date: " + cal.getTime());
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        boolean isWeekOverflow = false;
        System.out.println("Month: " + month + " Year: " + year + " Week: " + week);
        if (cal.get(Calendar.WEEK_OF_MONTH) == cal.getActualMaximum(Calendar.WEEK_OF_MONTH)) {
            isWeekOverflow = true;
            System.out.println("Week is overflow");
        }
        weekData = new ArrayList<WeekCell>();
        for (int i = 0; i < 7; i++) {
            WeekCell weekCell = new WeekCell();
            weekData.add(weekCell);
        }
        ArrayList<Event> events = Database.getEvents(user);
        if (!isWeekOverflow) {
            for (Event event : events) {
                int eventYear = Integer.valueOf(yearFormat.format(event.getDate()));
                int eventMonth = Integer.valueOf(monthFormat.format(event.getDate()));
                int eventWeek = Integer.valueOf(weekFormat.format(event.getDate()));
                if (eventYear == year && eventMonth == month + 1 && eventWeek == week) {
                    Calendar calendar = new GregorianCalendar();
                    // System.out.println("Title: " + event.getTitle());
                    // System.out.println("Date: " + dateFormat.format(event.getDate()));
                    // System.out.println("Start time: " + timeFormat.format(event.getDate()));
                    // System.out.println("Duration: " + event.getDuration());
                    // System.out.println("Priority: " + event.getPriority());
                    calendar.setTime(event.getDate());
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    // System.out.println("Date of Month: " + calendar.get(Calendar.DAY_OF_WEEK));
                    // System.out.println("Week: " + calendar.get(Calendar.WEEK_OF_MONTH) + "\n");
                    weekData.get(dayOfWeek - 1).date = dateFormat.format(event.getDate());
                    weekData.get(dayOfWeek - 1).addEvent(event);
                }
            }
            return weekData;
        } else {
            Calendar cal2 = new GregorianCalendar();
            cal2.setTime(cal.getTime());
            cal2.add(Calendar.DATE, +6);
            int month2 = cal2.get(Calendar.MONTH);
            int year2 = cal2.get(Calendar.YEAR);
            int week2 = cal2.get(Calendar.WEEK_OF_YEAR);
            for (Event event : events) {
                int eventYear = Integer.valueOf(yearFormat.format(event.getDate()));
                int eventMonth = Integer.valueOf(monthFormat.format(event.getDate()));
                int eventWeek = Integer.valueOf(weekFormat.format(event.getDate()));
                if (eventYear == year && eventMonth == month + 1 && eventWeek == week
                        || (eventYear == year2 && eventMonth == month2 + 1 && eventWeek == week)
                        || (eventYear == year2 && eventMonth == month2 + 1 && eventWeek == week2)) {
                    Calendar calendar = new GregorianCalendar();
                    // System.out.println("Title: " + event.getTitle());
                    // System.out.println("Date: " + dateFormat.format(event.getDate()));
                    // System.out.println("Start time: " + timeFormat.format(event.getDate()));
                    // System.out.println("Duration: " + event.getDuration());
                    // System.out.println("Priority: " + event.getPriority());
                    calendar.setTime(event.getDate());
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    // System.out.println("Date of Month: " + calendar.get(Calendar.DAY_OF_WEEK));
                    // System.out.println("Week: " + calendar.get(Calendar.WEEK_OF_MONTH) + "\n");
                    weekData.get(dayOfWeek - 1).date = dateFormat.format(event.getDate());
                    weekData.get(dayOfWeek - 1).addEvent(event);
                }
            }
            return weekData;
        }
    }

    /**
     * Get the month data from the database and return it as a list of
     * {@link Event}s
     * to print to {@link PDF}.
     * 
     * @param user The {@link User} to get the events from
     * @param cal  The {@link Calendar} to get which month will be printed from
     * @return The list of {@link Event}s to print to PDF
     */
    public static ArrayList<Event> getEventsOfMonth(User user, Calendar cal) {
        ArrayList<Event> monthEvents = new ArrayList<Event>();
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        ArrayList<Event> events = Database.getEvents(user);
        for (Event event : events) {
            int eventYear = Integer.valueOf(yearFormat.format(event.getDate()));
            int eventMonth = Integer.valueOf(monthFormat.format(event.getDate()));
            if (eventYear == year && eventMonth == month + 1) {
                // System.out.println("Title: " + event.getTitle());
                // System.out.println("Date: " + dateFormat.format(event.getDate()));
                // System.out.println("Day of month: " + calendar.get(Calendar.DAY_OF_WEEK));
                // System.out.println("Description: " + event.getDescription());
                // System.out.println("Start time: " + timeFormat.format(event.getDate()));
                // System.out.println("Duration: " + event.getDuration());
                // System.out.println("Priority: " + event.getPriority());
                monthEvents.add(event);
            }
        }
        return monthEvents;
    }

    /**
     * Get the week data from the database and return it as a list of {@link Event}s
     * to print to {@link PDF}.
     * 
     * @param user The {@link User} to get the events from
     * @param cal  The {@link Calendar} to get which week will be printed from
     * @return The list of {@link Event}s to print to PDF
     */
    public static ArrayList<Event> getEventsOfWeek(User user, Calendar cal) {
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        System.out.println("Date: " + cal.getTime());
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        boolean isWeekOverflow = false;
        if (cal.get(Calendar.WEEK_OF_MONTH) == cal.getActualMaximum(Calendar.WEEK_OF_MONTH)) {
            isWeekOverflow = true;
            System.out.println("Week is overflow");
        }
        ArrayList<Event> weekEvents = new ArrayList<Event>();
        ArrayList<Event> events = Database.getEvents(user);
        if (!isWeekOverflow) {
            for (Event event : events) {
                int eventYear = Integer.valueOf(yearFormat.format(event.getDate()));
                int eventMonth = Integer.valueOf(monthFormat.format(event.getDate()));
                int eventWeek = Integer.valueOf(weekFormat.format(event.getDate()));
                if (eventYear == year && eventMonth == month + 1 && eventWeek == week) {
                    // System.out.println("Title: " + event.getTitle());
                    // System.out.println("Date: " + dateFormat.format(event.getDate()));
                    // System.out.println("Description: " + event.getDescription());
                    // System.out.println("Start time: " + timeFormat.format(event.getDate()));
                    // System.out.println("Duration: " + event.getDuration());
                    // System.out.println("Priority: " + event.getPriority());
                    weekEvents.add(event);
                }
            }
            return weekEvents;
        } else {
            Calendar cal2 = new GregorianCalendar();
            cal2.setTime(cal.getTime());
            cal2.add(Calendar.DATE, +6);
            int month2 = cal2.get(Calendar.MONTH);
            int year2 = cal2.get(Calendar.YEAR);
            int week2 = cal2.get(Calendar.WEEK_OF_MONTH);
            for (Event event : events) {
                int eventYear = Integer.valueOf(yearFormat.format(event.getDate()));
                int eventMonth = Integer.valueOf(monthFormat.format(event.getDate()));
                int eventWeek = Integer.valueOf(weekFormat.format(event.getDate()));
                if (eventYear == year && eventMonth == month + 1 && eventWeek == week
                        || (eventYear == year2 && eventMonth == month2 + 1 && eventWeek == week)
                        || (eventYear == year2 && eventMonth == month2 + 1 && eventWeek == week2)) {
                    // System.out.println("Title: " + event.getTitle());
                    // System.out.println("Date: " + dateFormat.format(event.getDate()));
                    // System.out.println("Description: " + event.getDescription());
                    // System.out.println("Start time: " + timeFormat.format(event.getDate()));
                    // System.out.println("Duration: " + event.getDuration());
                    // System.out.println("Priority: " + event.getPriority());
                    weekEvents.add(event);
                }
            }
            return weekEvents;
        }
    }

    /**
     * Get the {@link Event}s that will be reminded in the future.
     * 
     * @param user The {@link User} to get the events from
     * @return The list of {@link Event}s that will be reminded
     */
    public static ArrayList<Event> getRemindEvents(User user) {
        Calendar cal = new GregorianCalendar();
        Calendar remindCalendar = new GregorianCalendar();
        ArrayList<Event> remindEvents = new ArrayList<Event>();
        ArrayList<Event> events = Database.getEvents(user);
        for (Event event : events) {
            remindCalendar.setTime(event.getRemind());
            if (cal.compareTo(remindCalendar) < 0) {
                remindEvents.add(event);
            }
        }
        return remindEvents;
    }
}
