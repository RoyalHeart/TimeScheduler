package src;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// Mail class is used to send email to the user
public class Mail {
    private static String verifyCode = "";
    private static String emailVerified = "";
    private static final String username = "notification.tisch@gmail.com";
    private static final String password = "trhwsetebngtbeqx";

    // create a properties object to store host information
    private static Properties createProperty() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // using TLS
        prop.put("mail.smtp.host", "smtp.gmail.com"); // using gmail
        prop.put("mail.smtp.port", "587"); // port for gmail
        return prop;
    }

    private static Session createSession(Properties prop) {
        return Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    public static void main(String[] args) {
        TestParticipants();
    }

    public static void TestParticipants() {
        Database.createConnection();
        User user = Database.getUser("admin", "admin");
        Date date = new Date();
        ArrayList<String> participants = new ArrayList<String>();
        participants.add("huyenma2002@gmail.com");
        participants.add("hoangtam3062002@gmail.com");
        Event event = new Event("0", "Participant Testing", "Participant Testing", participants, date, date, "", 0, 0);
        Mail.sendRemindEmail(user, event);
    }

    public static void TestVerify() {
        Database.createConnection();
        User user = Database.getUser("admin", Hash.hashPassword("adminadmin").toUpperCase());
        Mail.sendVerifyCode(user);
    }

    public static String generateVerifyCode() {
        Random rand = new Random();
        verifyCode = "";
        for (int i = 0; i < 6; i++) {
            verifyCode += rand.nextInt(10);
        }
        return verifyCode;
    }

    public static String sendVerifyCode(User user) {
        Properties prop = createProperty();
        Session session = createSession(prop);
        emailVerified = user.getEmail();
        try {
            verifyCode = generateVerifyCode();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // sent from notification.tisch@gmail.com
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail()));

            message.setSubject("Verify email to join TISCH");
            message.setText("Dear new user" + "\n\n" +
                    "Your verification code is: " + verifyCode + "\n" +
                    "Please enter this code to verify your email address.\n\n" +
                    "Thank you for using TISCH" + "\n\n" +
                    "TISCH Team");
            Transport.send(message);
            System.out.println("Message sent to " + user.getEmail());
            return verifyCode;

        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getVerifyCode() {
        return verifyCode;
    }

    public static void setVerifyCode(String verifyCode) {
        Mail.verifyCode = verifyCode;
    }

    public static String getEmailVerified() {
        return emailVerified;
    }

    public static boolean sendRemindEmail(User user, Event event) {
        Properties prop = createProperty();
        Session session = createSession(prop);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String priority = "";
        if (event.getPriority() == 0) {
            priority = "High";
        } else if (event.getPriority() == 1) {
            priority = "Medium";
        } else {
            priority = "Low";
        }
        // mail to user who created the event
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail()));
            message.setSubject("Reminder from TISCH");
            message.setText("Dear user " + user.getName() + "\n\n" +
                    "You have an event on " + dateFormat.format(event.getDate()) + " at "
                    + timeFormat.format(event.getDate()) + "\n" +
                    "Your event information: \n" +
                    "Title: " + event.getTitle() + "\n" +
                    "Description: " + event.getDescription() + "\n" +
                    "Location: " + event.getLocation() + "\n" +
                    "Priority: " + priority + "\n" +
                    "Duration: " + event.getDuration() + " minutes" + "\n\n" +
                    "TISCH Team");
            Transport.send(message);
            System.out.println("Message sent to " + user.getEmail());
            // return true;
            for (String participant : event.getParticipants()) {
                Properties propParticipant = createProperty();
                Session sessionParticipant = createSession(propParticipant);

                // mail to participants
                try {
                    Message messageParticipant = new MimeMessage(sessionParticipant);
                    messageParticipant.setFrom(new InternetAddress(username));
                    messageParticipant.setRecipients(
                            Message.RecipientType.TO,
                            InternetAddress.parse(participant));
                    // for (String participant : event.getParticipants()) {
                    // messageParticipant.setRecipients(
                    // Message.RecipientType.CC,
                    // InternetAddress.parse(participant));
                    // }
                    messageParticipant.setSubject("Reminder from TISCH");
                    messageParticipant.setText("You have been invited to an event by " + user.getName() +
                            " (email: " + user.getEmail() + ")\n\n" +
                            "You have an event on " + dateFormat.format(event.getDate()) + " at "
                            + timeFormat.format(event.getDate()) + "\n" +
                            "Your event information: \n" +
                            "Title: " + event.getTitle() + "\n" +
                            "Description: " + event.getDescription() + "\n" +
                            "Location: " + event.getLocation() + "\n" +
                            "Priority: " + priority + "\n" +
                            "Duration: " + event.getDuration() + " minutes" + "\n\n" +
                            "TISCH Team");
                    Transport.send(messageParticipant);
                    System.out.println("Message sent to " + participant);
                } catch (MessagingException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void sendEmail(String to, String subject, String body) {

        Properties prop = createProperty();

        Session session = createSession(prop);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // sent from notification.tisch@gmail.com
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("Message sent to " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
