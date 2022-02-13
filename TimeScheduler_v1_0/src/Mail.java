package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.properties.EncryptableProperties;

/**
 * {@code Mail} class is used to send emails to user and participants.
 * 
 * @author Tam Thai Hoang
 */
public class Mail {
    private static String verifyCode = "";
    private static String emailVerified = "";
    private static String jasyptPassword = getJasyptPassword();
    private static Properties mailProperties = getMailProperties();
    private static final String USERNAME = "notification.tisch@gmail.com";

    /**
     * Get the jasypt password from a file.
     * A much better way to do this would be to use a web service,
     * or from enviroment variable to get the password
     * 
     * @return the jasypt password as a {@code String}
     */
    private static String getJasyptPassword() {
        File myObj = new File("TimeScheduler_v1_0/lib/DatabaseLoginInfo.txt");
        try {
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                jasyptPassword = myReader.nextLine();
            }
            myReader.close();
            return jasyptPassword;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a the mail configuration as a {@code Properties} file that store host
     * information.
     * The properties password is decrypt with the jasyptPassword.
     * 
     * @return a {@link Properties} object containing host information
     */
    private static Properties getMailProperties() {
        try {
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(jasyptPassword); // could be got from web, env variable...
            encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
            encryptor.setIvGenerator(new RandomIvGenerator());
            Properties properties = new EncryptableProperties(encryptor);
            properties.load(new FileInputStream("TimeScheduler_v1_0/lib/mailConfig.properties"));
            return properties;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mailProperties;
    }

    /**
     * Create a mail session with {@code mailProperties} to send mail.
     * 
     * @return a {@link Session} object
     */
    private static Session createSession() {
        return Session.getInstance(mailProperties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailProperties.getProperty("username"),
                                mailProperties.getProperty("password"));
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

    /**
     * Generate a random 6 number code to verify the user's email
     * 
     * @return the {@code verifyCode} as a {@code String}
     */
    public static String generateVerifyCode() {
        Random rand = new Random();
        verifyCode = "";
        for (int i = 0; i < 6; i++) {
            verifyCode += rand.nextInt(10);
        }
        return verifyCode;
    }

    /**
     * Send a mail to the user to verify the user's email
     * 
     * @param user the {@code User} object
     * @return the {@code verifyCode} as a {@code String}
     */
    public static String sendVerifyCode(User user) {
        Session session = createSession();
        emailVerified = user.getEmail();
        try {
            verifyCode = generateVerifyCode();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME)); // sent from notification.tisch@gmail.com
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

    /**
     * Send a remind email to the {@code user} and all of the participants of the
     * {@code event} to remind them about the event
     * 
     * @param user  the {@link User} that is going to be reminded
     * @param event the {@link Event} that is going to be reminded
     * @return {@code true} if the mail is sent successfully or
     *         {@code false} if the mail is not sent successfully
     */
    public static boolean sendRemindEmail(User user, Event event) {
        Session session = createSession();
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
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail()));
            message.setSubject("Reminder from TISCH");
            String remindString = "";
            if (event.getRemind().equals(event.getDate())) {
                remindString = "No remind";
            } else {
                remindString = "Remind on: " + dateFormat.format(event.getRemind()) + " at: "
                        + timeFormat.format(event.getRemind());
            }
            String participantsString = "";
            if (event.getParticipants() != null) {
                if (event.getParticipants().size() == 0) {
                    participantsString = "No participants";
                } else {
                    participantsString = "Participants: \n";
                    for (int i = 0; i < event.getParticipants().size(); i++) {
                        participantsString += event.getParticipants().get(i) + "\n";
                    }
                }
            }
            String body = "Dear user " + user.getName() + "\n\n" +
                    "You have an event on " + dateFormat.format(event.getDate()) + " at "
                    + timeFormat.format(event.getDate()) + "\n" +
                    "Your event information: \n" +
                    "Title: " + event.getTitle() + "\n" +
                    "Description: " + event.getDescription() + "\n" +
                    "Location: " + event.getLocation() + "\n" +
                    "Priority: " + priority + "\n" +
                    "Duration: " + event.getDuration() + " minutes" + "\n" +
                    remindString + "\n" +
                    participantsString + "\n\n" +
                    "TISCH Team";
            message.setText(body);
            Transport.send(message);
            System.out.println("Message sent to " + user.getEmail());
            Session sessionParticipant = createSession();
            for (String participant : event.getParticipants()) {
                // mail to each participant
                try {
                    Message messageParticipant = new MimeMessage(sessionParticipant);
                    messageParticipant.setFrom(new InternetAddress(USERNAME));
                    messageParticipant.setRecipients(
                            Message.RecipientType.TO,
                            InternetAddress.parse(participant));
                    messageParticipant.setSubject("Reminder from TISCH");
                    String remindStringParticipant = "";
                    if (event.getRemind().equals(event.getDate())) {
                        remindStringParticipant = "No remind";
                    } else {
                        remindStringParticipant = "Remind on: " + dateFormat.format(event.getRemind()) + " at: "
                                + timeFormat.format(event.getRemind());
                    }
                    messageParticipant.setText("You have been invited to an event by " + user.getName() +
                            " (email: " + user.getEmail() + ")\n\n" +
                            "You have an event on " + dateFormat.format(event.getDate()) + " at "
                            + timeFormat.format(event.getDate()) + "\n" +
                            "Your event information: \n" +
                            "Title: " + event.getTitle() + "\n" +
                            "Description: " + event.getDescription() + "\n" +
                            "Location: " + event.getLocation() + "\n" +
                            "Priority: " + priority + "\n" +
                            "Duration: " + event.getDuration() + " minutes" + "\n" +
                            remindStringParticipant + "\n\n" +
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

    /**
     * Send an email to the user and participants about the edited {@link Event}
     *
     * @param user     the {@code user} that edit the event
     * @param event    the {@code event} before editing
     * @param newEvent the {@code event} after editing
     * @return {@code true} if the message is sent successfully
     *         {@code false} if the message is not sent successfully
     */
    public static boolean sendEditEmail(User user, Event event, Event newEvent) {
        Session session = createSession();
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
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail()));
            message.setSubject("Reminder from TISCH");
            String remindString = "";
            if (event.getRemind().equals(event.getDate())) {
                remindString = "No remind";
            } else {
                remindString = "Remind on: " + dateFormat.format(event.getRemind()) + " at: "
                        + timeFormat.format(event.getRemind());
            }
            String participantsString = "";
            if (event.getParticipants() != null) {
                if (event.getParticipants().size() == 0) {
                    participantsString = "No participants";
                } else {
                    participantsString = "Participants: \n";
                    for (int i = 0; i < event.getParticipants().size(); i++) {
                        participantsString += event.getParticipants().get(i) + "\n";
                    }
                }
            } else {
                participantsString = "No participants";
            }
            // old event information
            String body = "Dear user " + user.getName() + "\n\n" +
                    "You have an update to one of your event" + "\n" +
                    "Your old event was on " + dateFormat.format(event.getDate()) + " at "
                    + timeFormat.format(event.getDate()) + "\n" +
                    "Your old event information: \n" +
                    "Title: " + event.getTitle() + "\n" +
                    "Description: " + event.getDescription() + "\n" +
                    "Location: " + event.getLocation() + "\n" +
                    "Priority: " + priority + "\n" +
                    "Duration: " + event.getDuration() + " minutes" + "\n" +
                    remindString + "\n" +
                    participantsString;
            String newRemindString = "";
            if (event.getRemind().equals(event.getDate())) {
                newRemindString = "No remind";
            } else {
                newRemindString = "Remind on: " + dateFormat.format(event.getRemind()) + " at: "
                        + timeFormat.format(event.getRemind());
            }
            String newParticipantsString = "";
            if (event.getParticipants() != null) {
                if (event.getParticipants().size() == 0) {
                    newParticipantsString = "No participants";
                } else {
                    newParticipantsString = "Participants: \n";
                    for (int i = 0; i < event.getParticipants().size(); i++) {
                        newParticipantsString += event.getParticipants().get(i) + "\n";
                    }
                }
            } else {
                newParticipantsString = "No participants";
            }
            // new event information
            body += "\n\n" +
                    "Your new event is on " + dateFormat.format(newEvent.getDate()) + " at "
                    + timeFormat.format(newEvent.getDate()) + "\n" +
                    "Your new event information: \n" +
                    "Title: " + newEvent.getTitle() + "\n" +
                    "Description: " + newEvent.getDescription() + "\n" +
                    "Location: " + newEvent.getLocation() + "\n" +
                    "Priority: " + priority + "\n" +
                    "Duration: " + newEvent.getDuration() + " minutes" + "\n" +
                    newRemindString + "\n" +
                    newParticipantsString + "\n\n" +
                    "TISCH Team";
            message.setText(body);
            Transport.send(message);
            System.out.println("Message sent to " + user.getEmail());
            Session sessionParticipant = createSession();
            if (event.getParticipants() != null) {
                for (String participant : event.getParticipants()) {
                    // mail to each participant
                    try {
                        Message messageParticipant = new MimeMessage(sessionParticipant);
                        messageParticipant.setFrom(new InternetAddress(USERNAME));
                        messageParticipant.setRecipients(
                                Message.RecipientType.TO,
                                InternetAddress.parse(participant));
                        messageParticipant.setSubject("Reminder from TISCH");

                        String remindStringParticipant = "";
                        if (event.getRemind().equals(event.getDate())) {
                            remindStringParticipant = "No remind";
                        } else {
                            remindStringParticipant = "Remind on: " + dateFormat.format(event.getRemind()) + " at: "
                                    + timeFormat.format(event.getRemind());
                        }
                        String bodyParticipant = "";
                        bodyParticipant += "Your event invited by " + user.getName() +
                                " (email: " + user.getEmail() + ") has an update\n\n" +
                                "Your old is event on " + dateFormat.format(event.getDate()) + " at "
                                + timeFormat.format(event.getDate()) + "\n" +
                                "Your old event information: \n" +
                                "Title: " + event.getTitle() + "\n" +
                                "Description: " + event.getDescription() + "\n" +
                                "Location: " + event.getLocation() + "\n" +
                                "Priority: " + priority + "\n" +
                                "Duration: " + event.getDuration() + " minutes" + "\n" +
                                remindStringParticipant;

                        String newRemindStringParticipant = "";
                        if (event.getRemind().equals(event.getDate())) {
                            newRemindStringParticipant = "No remind";
                        } else {
                            newRemindStringParticipant = "Remind on: " + dateFormat.format(event.getRemind()) + " at: "
                                    + timeFormat.format(event.getRemind());
                        }
                        bodyParticipant += "\n\nYour new event is on " + dateFormat.format(newEvent.getDate()) + " at "
                                + timeFormat.format(newEvent.getDate()) + "\n" +
                                "Your new event information: \n" +
                                "Title: " + newEvent.getTitle() + "\n" +
                                "Description: " + newEvent.getDescription() + "\n" +
                                "Location: " + newEvent.getLocation() + "\n" +
                                "Priority: " + priority + "\n" +
                                "Duration: " + newEvent.getDuration() + " minutes" + "\n" +
                                newRemindStringParticipant + "\n\n" +
                                "TISCH Team";

                        messageParticipant.setText(bodyParticipant);
                        Transport.send(messageParticipant);
                        System.out.println("Message sent to " + participant);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            } else {
                System.out.println("No participants");
            }
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Send an email to the user and participants about the deleted {@link Event}
     *
     * @param user  the {@code user} that delete the event
     * @param event the {@code event} that was deleted
     * @return {@code true} if the message is sent successfully
     *         {@code false} if the message is not sent successfully
     */
    public static boolean sendDeletedEmail(User user, Event event) {
        Session session = createSession();
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
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail()));
            message.setSubject("Reminder from TISCH");
            String remindString = "";
            if (event.getRemind().equals(event.getDate())) {
                remindString = "No remind";
            } else {
                remindString = "Remind on: " + dateFormat.format(event.getRemind()) + " at: "
                        + timeFormat.format(event.getRemind());
            }
            String participantsString = "";
            if (event.getParticipants() != null) {
                if (event.getParticipants().size() == 0) {
                    participantsString = "No participants";
                } else {
                    participantsString = "Participants: \n";
                    for (int i = 0; i < event.getParticipants().size(); i++) {
                        participantsString += event.getParticipants().get(i) + "\n";
                    }
                }
            } else {
                participantsString = "No participants";
            }
            String body = "Dear user " + user.getName() + "\n\n" +
                    "You have deleted an event on " + dateFormat.format(event.getDate()) + " at "
                    + timeFormat.format(event.getDate()) + "\n" +
                    "Your deleted event information: \n" +
                    "Title: " + event.getTitle() + "\n" +
                    "Description: " + event.getDescription() + "\n" +
                    "Location: " + event.getLocation() + "\n" +
                    "Priority: " + priority + "\n" +
                    "Duration: " + event.getDuration() + " minutes" + "\n" +
                    remindString + "\n" +
                    participantsString + "\n\n" +
                    "TISCH Team";
            message.setText(body);
            Transport.send(message);
            System.out.println("Message sent to " + user.getEmail());
            Session sessionParticipant = createSession();
            if (event.getParticipants() != null) {
                for (String participant : event.getParticipants()) {
                    // mail to each participant
                    try {
                        Message messageParticipant = new MimeMessage(sessionParticipant);
                        messageParticipant.setFrom(new InternetAddress(USERNAME));
                        messageParticipant.setRecipients(
                                Message.RecipientType.TO,
                                InternetAddress.parse(participant));
                        messageParticipant.setSubject("Reminder from TISCH");
                        String remindStringParticipant = "";
                        if (event.getRemind().equals(event.getDate())) {
                            remindStringParticipant = "No remind";
                        } else {
                            remindStringParticipant = "Remind on: " + dateFormat.format(event.getRemind()) + " at: "
                                    + timeFormat.format(event.getRemind());
                        }
                        messageParticipant.setText("Your event invited by " + user.getName() +
                                " (email: " + user.getEmail() + ") has been deleted\n\n" +
                                "Your deleted event was on " + dateFormat.format(event.getDate()) + " at "
                                + timeFormat.format(event.getDate()) + "\n" +
                                "Your deleted event information: \n" +
                                "Title: " + event.getTitle() + "\n" +
                                "Description: " + event.getDescription() + "\n" +
                                "Location: " + event.getLocation() + "\n" +
                                "Priority: " + priority + "\n" +
                                "Duration: " + event.getDuration() + " minutes" + "\n" +
                                remindStringParticipant + "\n\n" +
                                "TISCH Team");
                        Transport.send(messageParticipant);
                        System.out.println("Message sent to " + participant);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                return true;
            }
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
