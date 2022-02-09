package src;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

// import com.itextpdf.awt.geom.Shape;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * PDF class to export the monthly and weekly events information to PDF.
 * 
 * @author Tam Thai Hoang
 */
public class PDF {
    private static Document document;
    static String filePath = "";
    private static String fileName = "WeeklyReport.pdf";
    private static final String TISCH_IMAGE_STRING_PATH = "lib/TimeSchedulerIcon.png";

    /**
     * Create a PDF file with the TISCH logo image and some welcoming words.
     * 
     * @return the {@link Document} object
     */
    public static Document createDocumentForm(User user) {
        try {
            document.open();
            System.out.println("Document Opened");
            float width = document.getPageSize().getWidth();
            float height = document.getPageSize().getHeight();
            Path imagePath = Paths.get(ClassLoader.getSystemResource(TISCH_IMAGE_STRING_PATH).toURI());
            Image img = Image.getInstance(imagePath.toString());
            img.scaleAbsolute(70, 70);
            float imgWidth = img.getScaledWidth();
            float imgHeight = img.getScaledHeight();
            img.setAbsolutePosition(width - imgWidth - 10, height - imgHeight - 10);
            document.add(img);
            System.out.println("Image added");
            document.add(new Phrase("Dear User " + user.getName() + ",\n\n"));
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create a PDF file with monthly events.
     * 
     * @param user     the {@link User} object to get the events from
     * @param calendar the {@link Calendar} object to get the month from
     * @return {@code true} if the file is created successfully, {@code false}
     *         otherwise
     */
    public static boolean exportMonthlyEvents(User user, Calendar calendar) {
        try {
            Calendar cal = calendar;
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            ArrayList<Event> events = new ArrayList<Event>();
            events = LoadEvents.getEventsOfMonth(user, cal);
            int year = cal.get(Calendar.YEAR);
            String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
            fileName = "MonthlyReport_" + monthName + "_" + year + ".pdf";
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath + fileName));
            System.out.println("PDF created at: " + filePath + fileName);
            document = createDocumentForm(user);
            document.add(new Phrase("This is your monthly events report in " + monthName + " " + year + ":\n"));
            if (events.size() == 0) {
                document.add(new Phrase("You have no events for this month.\n"));
                document.close();
                System.out.println("Document Closed");
                return true;
            } else {
                if (events.size() > 1) {
                    document.add(new Phrase(
                            "You have " + events.size() + " events in " + monthName + " " + year + ":\n"));
                } else {
                    document.add(new Phrase(
                            "You have 1 event in " + monthName + " " + year + ":\n"));
                }
                for (Event event : events) {
                    String startTime = dateFormat.format(event.getDate());
                    String endTime = dateFormat.format(event.getDate().getTime() + event.getDuration() * 60 * 1000);
                    String remind;
                    if (event.getRemind() != null) {
                        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        String remindTime = timeFormat.format(event.getRemind());
                        String remindDate = dateFormat.format(event.getRemind());
                        remind = "    Remind at:  " + remindTime + " on " + remindDate + ".";
                    } else {
                        remind = "    No remind.";
                    }
                    int priorityInt = event.getPriority();
                    String priority = "";
                    if (priorityInt == 0) {
                        priority = "High";
                    } else if (priorityInt == 1) {
                        priority = "Medium";
                    } else if (priorityInt == 2) {
                        priority = "Low";
                    }
                    document.add(new Phrase("Title: " + event.getTitle() + "\n" +
                            "    Description: " + event.getDescription() + "\n" +
                            "    Date:           " + dateFormat.format(event.getDate()) + "\n" +
                            "    Time:          " + startTime + " - " + endTime + "\n" +
                            "    Location:     " + event.getLocation() + "\n" +
                            "    Priority:       " + priority + "\n" +
                            remind + "\n" +
                            "    Participant: "));
                    if (event.getParticipants() == null) {
                        document.add(new Phrase("You are the only participant.\n\n"));
                    } else {
                        for (String participant : event.getParticipants()) {
                            document.add(new Phrase("\n           " + participant));
                        }
                        document.add(new Phrase("\n\n"));
                    }
                }
                document.close();
                System.out.println("Document Closed");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Create a PDF file with weekly events.
     * 
     * @param user     the {@link User} to get the events from
     * @param calendar the {@link Calendar} to get the week from
     * @return {@code true} if the file is created successfully, {@code false}
     *         otherwise
     */
    public static boolean exportWeeklyEvents(User user, Calendar calendar) {
        try {
            System.out.println("Exporting weekly events..." + calendar.get(Calendar.YEAR) + " "
                    + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DAY_OF_MONTH));
            Calendar cal = calendar;
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            ArrayList<Event> events = new ArrayList<Event>();
            events = LoadEvents.getEventsOfWeek(user, cal);
            for (Event e : events) {
                System.out.println(e.getTitle());
            }
            int weekOfMonth = cal.get(Calendar.WEEK_OF_MONTH);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            cal.add(Calendar.DATE, -dayOfWeek + 1);
            String startDate = dateFormat.format(cal.getTime());
            cal.add(Calendar.DATE, +7);
            String endDate = dateFormat.format(cal.getTime());

            fileName = "WeeklyReport_" + startDate + ".pdf";
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath + fileName));
            System.out.println("PDF created at: " + filePath + fileName);
            document = createDocumentForm(user);
            document.add(new Phrase("This is your weekly events report " +
                    "(from " + startDate + " to " + endDate + "):\n"));
            if (events.size() == 0) {
                document.add(new Phrase("You have no events for this week.\n"));
                document.close();
                System.out.println("Document Closed");
                return true;
            } else {
                DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);

                if (events.size() > 1) {
                    document.add(new Phrase(
                            "You have " + events.size() + " events for week " + weekOfMonth + " of " + month + ":\n"));
                } else {
                    document.add(new Phrase(
                            "You have 1 event for week " + weekOfMonth + " of " + month + ":\n"));
                }
                for (Event event : events) {
                    String startTime = timeFormat.format(event.getDate());
                    String endTime = timeFormat.format(event.getDate().getTime() + event.getDuration() * 60 * 1000);
                    String remind;
                    if (event.getRemind() != null) {
                        String remindTime = timeFormat.format(event.getRemind());
                        String remindDate = dateFormat.format(event.getRemind());
                        remind = "    Remind at:  " + remindTime + " on " + remindDate + ".";
                    } else {
                        remind = "    No remind.";
                    }
                    int priorityInt = event.getPriority();
                    String priority = "";
                    if (priorityInt == 0) {
                        priority = "High";
                    } else if (priorityInt == 1) {
                        priority = "Medium";
                    } else if (priorityInt == 2) {
                        priority = "Low";
                    }
                    document.add(new Phrase("Title: " + event.getTitle() + "\n" +
                            "    Description: " + event.getDescription() + "\n" +
                            "    Date:           " + dateFormat.format(event.getDate()) + "\n" +
                            "    Time:          " + startTime + " - " + endTime + "\n" +
                            "    Location:     " + event.getLocation() + "\n" +
                            "    Priority:       " + priority + "\n" +
                            remind + "\n" +
                            "    Participant: "));
                    if (event.getParticipants() == null) {
                        document.add(new Phrase("You are the only participant."));
                    } else {
                        for (String participant : event.getParticipants()) {
                            document.add(new Phrase("\n               " + participant));
                        }
                        document.add(new Phrase("\n\n"));
                    }
                }
                document.close();
                System.out.println("Document Closed");

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
