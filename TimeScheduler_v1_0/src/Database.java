package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.properties.EncryptableProperties;

/**
 * <p>
 * {@code Database} is used to connect to the database and perform CRUD.
 * </p>
 * <p>
 * It is used to store and get the information of the {@link User}s and
 * {@link Event}s.
 * </p>
 * 
 * @author Tam Thai Hoang 1370674
 * @author Huy Truong Quang 1370713 (get user list, check admin, get user,
 *         delete user)
 * @author Sang Doan Tan 1370137 (update user profile, update event)
 */
public class Database {
    /**
     * The {@code String} to add an user to TISCH_USER table.
     */
    final static String addUser = "INSERT INTO TISCH_USER (ID, USERNAME, PASSWORD, USERFULLNAME, USEREMAIL, USERPHONENUMBER) VALUES (?, ?, ?, ?, ?, ?)";

    /**
     * The {@code String} to add an event to EVENT table.
     */
    final static String addEvent = "INSERT INTO EVENT (ID, USERID, EVENTTITLE, EVENTDESCRIPTION, EVENTDATE, EVENTREMIND, EVENTLOCATION, EVENTDURATION, EVENTPRIORITY) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * The {@code String} to update event in EVENT table.
     */
    final static String updateEvent = "UPDATE EVENT SET EVENTTITLE = ?, EVENTDESCRIPTION = ?, EVENTDATE = ?, EVENTREMIND = ?, EVENTLOCATION = ?, EVENTDURATION = ?, EVENTPRIORITY = ? WHERE ID = ? AND USERID = ?";

    /**
     * The {@code String} to add a participant to EVENT_PARTICIPANT table.
     */
    final static String addEventParticipants = "INSERT INTO EVENT_PARTICIPANT (EVENT_ID, USER_ID, PARTICIPANT) VALUES (?, ?, ?)";

    /**
     * The {@code jasyptPassword} to decrypt the {@code database properties}file.
     */
    static String jasyptPassword = getJasyptPassword();

    /**
     * The encrypted {@link Properties} to get database configuration for
     * connection.
     */
    final static Properties databaseProperties = getDatabaseProperties();

    /**
     * The {@code Connection} object to connect to the database.
     */
    static Connection con;

    /**
     * create database {@link Connection} to the Oracle database with the
     * properties from the {@code databaseProperties} object.
     * 
     * @return {@link Connection} to the database
     */
    static Connection createConnection() {
        try {
            // step1 load the driver class
            Class.forName(databaseProperties.getProperty("dbconfig.driver"));

            // step2 create the connection object
            con = DriverManager.getConnection(
                    databaseProperties.getProperty("dbconfig.url"),
                    databaseProperties.getProperty("dbconfig.username"),
                    databaseProperties.getProperty("dbconfig.password"));
            System.out.println("Connected to database");
            return con;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
            return null;
        }
    };

    /**
     * This method is used to close the {@link Connection} to the database.
     * 
     * @return {@code true} if the {@link Connection} is closed successfully,
     *         {@code false} otherwise
     */
    static boolean closeConnection() {
        try {
            System.out.println("Closing connection to database");
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get the {@code database properties} from the properties file.
     * The properties file is encrypted using jasypt
     * 
     * @return {@code Properties} of the database
     */
    private static Properties getDatabaseProperties() {
        try {
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(jasyptPassword); // could be got from web, env variable...
            encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
            encryptor.setIvGenerator(new RandomIvGenerator());
            Properties properties = new EncryptableProperties(encryptor);
            properties.load(new FileInputStream("TimeScheduler_v1_0/lib/databaseconfig.properties"));
            return properties;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseProperties;
    }

    /**
     * Get the jasypt password from the file.
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
     * Add user to database and return {@code true} if the user is added successfully
     * 
     * @param username     username of the user
     * @param hashPassword hash(password+username) of the user
     * @param name         full name of the user
     * @param email        email of the user
     * @param phone        phone number of the user
     * @return true if the user is added successfully,
     *         false otherwise
     */
    public static boolean addUser(String username, String hashPassword, String name,
            String email, String phone) {
        try {
            // create the connection object
            // Connection con = createConnection();

            // create the prepared statement object
            PreparedStatement ps = con.prepareStatement(addUser);
            ps.setString(1, "");
            ps.setString(2, username);
            ps.setString(3, hashPassword);
            ps.setString(4, name);
            ps.setString(5, email);
            ps.setString(6, phone);

            // execute query
            ps.execute();
            ps.close();

            // close the connection object
            // con.close();
            return true; // user successfully added
        } catch (Exception e) {
            System.out.println(e);
            return false; // failed to add user
        }
    }

    /**
     * Check if username exists in database and return true if it does
     * 
     * @param username username to check
     * @return true if username exists in database
     *         , false otherwise
     */
    static boolean existUsername(String username) {
        try {
            // create the connection object
            // Connection con = createConnection();

            // create the prepared statement object
            PreparedStatement ps = con.prepareStatement("SELECT * FROM TISCH_USER WHERE USERNAME = ?");
            ps.setString(1, username);

            // execute query
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ps.close();
                // con.close();
                return true; // username exists
            } else {
                ps.close();
                // con.close();
                return false; // username does not exist
            }
        } catch (Exception e) {
            System.out.println(e);
            return false; // failed to check username
        }
    }

    /**
     * <p>
     * Check if the user exists in the database and return true if it does
     * </p>
     * <p>
     * Using for {@link LoginScreen} to check if the user exists in the database
     * </p>
     * 
     * @param username username of the user
     * @param password plain password of the user
     * @return true if user is exist in the database,
     *         false if user is not exist in the database
     */
    static boolean existUser(String username, String password) {
        try {
            // create statement object
            // con = createConnection();
            Statement stmt = con.createStatement();

            // create a query
            ResultSet rs = stmt.executeQuery("SELECT * FROM TISCH_USER WHERE USERNAME = '" + username + "'");
            String hashPassword = Hash.hashPassword(password + username).toUpperCase();
            // execute query
            if (rs.next() && rs.getString("PASSWORD").equals(hashPassword)) {
                return true; // user exists
            } else {
                return false; // user does not exist
            }

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * <p>
     * Check if the user is admin or not
     * </p>
     * <p>
     * Using for login purpose
     * </p>
     * 
     * @param username username of the user
     * @param password plain password of the user
     * @return true if user is admin,
     *         false if user is not admin
     */
    static boolean existAdmin(String username, String password) {
        try {
            // create the connection object
            // Connection con = createConnection();

            // create the statement object
            Statement stmt = con.createStatement();

            // execute query
            ResultSet rs = stmt.executeQuery("SELECT * FROM TISCH_USER WHERE USERNAME = '" + username + "'");
            String hashPassword = Hash.hashPassword(password + username).toUpperCase();
            // process the result set
            if (rs.next()) {
                if (rs.getString("PASSWORD").equals(hashPassword)) {
                    rs = stmt.executeQuery("SELECT 1 FROM ADMINISTRATOR WHERE ID = '" + rs.getString("ID") + "'");
                    if (rs.next()) {
                        return true; // admin exists
                    } else {
                        return false; // user is not an admin
                    }
                } else {
                    return false; // password is wrong for this user
                }
            } else {
                return false; // user does not exist
            }

            // close the connection object
            // con.close();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * Get the {@code User} object from the database
     * 
     * @param username the username of the user
     * @param password the plain password of the user
     * @return the user object from the database,
     *         or null if user does not exist
     */
    static User getUser(String username, String password) {
        try {
            // create the connection object
            // Connection con = createConnection();

            // create the statement object
            Statement stmt = con.createStatement();

            // execute query
            ResultSet rs = stmt.executeQuery("SELECT * FROM TISCH_USER WHERE USERNAME = '" + username + "'");
            String hashPassword = Hash.hashPassword(password + username).toUpperCase();
            // process the result set
            if (rs.next()) {
                String userID = rs.getString(1);
                String name = rs.getString(4);
                String email = rs.getString(5);
                String phone = rs.getString(6);

                if (rs.getString(3).equals(hashPassword)) {
                    return new User(userID, username, name, email, phone);
                } else {
                    return null; // password is wrong for this user
                }
            } else {
                return null; // username does not exist
            }
            // close the connection object
            // con.close();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Get the admin as a {@code User} object from the database
     * 
     * @param username username of the admin
     * @param password the plain password of the admin
     * @return admin as a {@code User} object,
     *         or null if user does not exist
     */
    static User getAdmin(String username, String password) {
        try {

            // create the connection object
            // Connection con = createConnection();

            // create the statement object
            Statement stmt = con.createStatement();

            // execute query
            ResultSet rs = stmt.executeQuery("SELECT * FROM TISCH_USER WHERE USERNAME = '" + username + "'");
            String hashPassword = Hash.hashPassword(password + username).toUpperCase();
            // process the result set
            if (rs.next()) {
                if (rs.getString(3).equals(hashPassword)) {
                    String userID = rs.getString(1);
                    String name = rs.getString(4);
                    String email = rs.getString(5);
                    String phone = rs.getString(6);
                    rs = stmt.executeQuery("SELECT * FROM ADMINISTRATOR WHERE ID = '" + rs.getString(1) + "'");
                    if (rs.next()) {
                        System.out.println("Admin ID: " + userID);
                        return new User(userID, username, name, email, phone);
                    } else {
                        return null;
                    }
                }
            }

            // close the connection object
            // con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Add {@code Event} of a user to the database
     * 
     * @param event the event to be added
     * @return true if event is added successfully
     *         false if event is not added successfully
     */
    static boolean addEvent(Event event) {
        try {
            // create the statement object
            java.sql.Date eventDate = new java.sql.Date(event.getDate().getTime());
            java.sql.Date eventRemind = new java.sql.Date(event.getRemind().getTime());
            System.out.println("event Date" + eventDate);
            PreparedStatement ps = con.prepareStatement(addEvent);
            ps.setString(1, "");
            ps.setString(2, event.getUserID());
            ps.setString(3, event.getTitle());
            ps.setString(4, event.getDescription());
            ps.setDate(5, eventDate);
            ps.setDate(6, eventRemind);
            ps.setString(7, event.getLocation());
            ps.setInt(8, event.getDuration());
            ps.setInt(9, event.getPriority());
            // execute query
            ps.execute();
            ps.close();

            Statement stmt = con.createStatement();
            ResultSet rs = stmt
                    .executeQuery("SELECT EVENT_SEQUENCE.CURRVAL FROM DUAL");
            if (rs.next()) {
                System.out.println("Result set: " + rs.getString(1));
            }
            event.setID(rs.getString(1));
            PreparedStatement ps2 = con.prepareStatement(addEventParticipants);
            for (String participant : event.getParticipants()) {
                ps2.setString(1, event.getID());
                ps2.setString(2, event.getUserID());
                ps2.setString(3, participant);
                ps2.execute();
            }
            ps2.close();
            // close the connection object
            // con.close();
            return true; // user successfully added
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all events from a user from database
     * 
     * @param user the {@code User} to get the events from
     * @return an array list of all events from a user
     */
    static ArrayList<Event> getEvents(User user) {
        ArrayList<Event> events = new ArrayList<>();
        try {
            // create the statement object
            Statement stmt = con.createStatement();

            // execute query
            ResultSet rs = stmt
                    .executeQuery("SELECT * FROM EVENT WHERE USERID = '" + user.getId() + "'" + " ORDER BY EventDate");

            // process the result set
            while (rs.next()) {
                String id = rs.getString(1);
                String userid = rs.getString(2);
                String title = rs.getString(3);
                String description = rs.getString(4);
                Date date = rs.getDate(5);
                Date remind = rs.getDate(6);
                String location = rs.getString(7);
                int duration = rs.getInt(8);
                int priority = rs.getInt(9);
                Event event = new Event(id, userid, title, description, date, remind, location, duration, priority);
                // event = getEventParticipants(event); // very poor performance
                events.add(event);
            }
            rs.close();
            stmt.close();
            events = getEventsParticipants(events); // better performance than above
            return events;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all participants of events and return an array list of events with
     * participants
     * 
     * @param events the {@code Event}s to get the participants from
     * @return an array list of all events with participants
     */
    static ArrayList<Event> getEventsParticipants(ArrayList<Event> events) {
        try {
            // create the statement object
            Statement stmt = con.createStatement();
            // execute query
            ResultSet rs = stmt
                    .executeQuery("SELECT * FROM EVENT_PARTICIPANT");

            // process the result set

            Map<String, ArrayList<String>> map = new HashMap<>();
            // add all participants to the map
            while (rs.next()) {
                String eventID = rs.getString(1);
                String participant = rs.getString(3);
                if (map.containsKey(eventID)) {
                    map.get(eventID).add(participant);
                } else {
                    ArrayList<String> participants = new ArrayList<>();
                    participants.add(participant);
                    map.put(eventID, participants);
                }
            }
            for (Event event : events) {
                event.setParticipants(map.get(event.getID()));
            }
            stmt.close();
            rs.close();
            return events;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Update name of a {@code User} in the database
     * 
     * @param name the new name of the user
     * @param user the user to update name
     * @return true if name is updated successfully,
     *         false if name is not updated successfully
     */
    static boolean updateName(String name, User user) {
        // Create the statement object, statement object allows you to sends SQL
        // statement to database
        try {
            Statement stmt = con.createStatement();
            // execute query
            if (stmt.executeUpdate(
                    "UPDATE TISCH_USER SET USERFULLNAME = '" + name + "' WHERE id = " + user.getId()) == 0) {
                System.out.println("Can not update database");
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }

    /**
     * Update email of a {@code User} in the database
     * 
     * @param email the new email of user
     * @param user  the user to update email
     * @return true if email is updated successfully,
     *         false if email is not updated successfully
     */
    static boolean updateEmail(String email, User user) {
        try {
            Statement stmt = con.createStatement();
            if (stmt.executeUpdate(
                    "UPDATE TISCH_USER SET USEREMAIL = '" + email + "' WHERE id = " + user.getId()) == 0) {
                return false;
            } else {
                System.out.println("Email is updated");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Update phone of a {@code User} in the database
     *
     * @param phone the new phone of user
     * @param user  the user to update phone
     * @return true if phone is updated successfully,
     *         false if phone is not updated succesfully
     */
    static boolean updatePhone(String phone, User user) {
        try {
            Statement stmt = con.createStatement();
            if (stmt.executeUpdate(
                    "UPDATE TISCH_USER SET USERPHONENUMBER = '" + phone + "' WHERE id = " + user.getId()) == 0) {
                return false;
            } else {
                System.out.println("Phone is updated.");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Update pasword of a {@code User} in the database
     * 
     * @param password the new password of user
     * @param user     the user to update
     * @return true if password is updated succesfully,
     *         false if password is not updated succesffuly
     */
    static boolean updatePassword(String password, User user) {
        try {
            Statement stmt = con.createStatement();
            if (stmt.executeUpdate(
                    "UPDATE TISCH_USER SET PASSWORD = '"
                            + Hash.hashPassword(password + user.getUsername()).toUpperCase() + "' WHERE id = "
                            + user.getId()) == 0) {
                return false;
            } else {
                System.out.println("Password is updated.");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete {@code Event} of a {@code User} in the database
     * 
     * @param event the event that needed to delete
     * @return true if delete event succsessfully,
     *         false if does not delete event successfully
     */
    static boolean delEvent(Event event) {
        try {
            Statement stmt = con.createStatement();
            if (stmt.executeUpdate(
                    "DELETE EVENT WHERE id = " + event.getID() + " AND userId = " + event.getUserID()) == 0) {
                if (stmt.executeUpdate("DELETE FROM EVENT_PARTICIPANT WHERE eventId = " + event.getID()) == 0) {
                    return false; // delete event_participant failed
                } else {
                    return true;
                }
            } else {
                System.out.println("Delete event successfully.");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update {@code Event} of a {@code User} in the database
     * 
     * @param event the event that will be updated
     * @return true if updated event successfully,
     *         false if can not update event
     */
    static boolean updateEvent(Event event) {
        try {
            // create the statement object
            java.sql.Date eventDate = new java.sql.Date(event.getDate().getTime());
            java.sql.Date eventRemind = new java.sql.Date(event.getRemind().getTime());
            PreparedStatement ps = con.prepareStatement(updateEvent);
            ps.setString(1, event.getTitle());
            ps.setString(2, event.getDescription());
            ps.setDate(3, eventDate);
            ps.setDate(4, eventRemind);
            ps.setString(5, event.getLocation());
            ps.setInt(6, event.getDuration());
            ps.setInt(7, event.getPriority());
            ps.setString(8, event.getID());
            ps.setString(9, event.getUserID());

            ps.execute();
            ps.close();

            Statement stmt = con.createStatement();
            if (event.getParticipants() != null) {
                if (event.getParticipants().size() > 0) {
                    // delete participants of event
                    if (stmt.executeUpdate(
                            "DELETE FROM EVENT_PARTICIPANT WHERE EVENT_ID = " + event.getID() + " AND USER_ID = "
                                    + event.getUserID()) == 0) {
                        System.out.println("Can not delete participants of event");
                        return false;
                    } else {
                        System.out.println("Delete participants of event successfully.");
                    }
                } else {
                    System.out.println("No participants of event.");
                }
            } else {
                System.out.println("No participants object.");
            }

            // add participants of event
            PreparedStatement ps2 = con.prepareStatement(addEventParticipants);
            for (String participant : event.getParticipants()) {
                ps2.setString(1, event.getID());
                ps2.setString(2, event.getUserID());
                ps2.setString(3, participant);
                ps2.execute();
            }
            ps2.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Get all user to make a list of all users from the database
     * 
     * @return a list of all users in the database
     */
    static Vector<Vector> getUserList() {
        try {
            Statement stm = con.createStatement();
            String sql = "SELECT ID, USERNAME, USERFULLNAME, USEREMAIL FROM TISCH_USER ORDER BY ID ASC";
            ResultSet rs = stm.executeQuery(sql);

            Vector<Vector> data = new Vector<Vector>();
            while (rs.next()) {
                Vector<Object> row = new Vector<Object>(4);

                for (int i = 1; i <= 4; i++) { // rs start from 1
                    row.addElement(rs.getObject(i));
                }

                data.addElement(row);
            }

            stm.close();
            rs.close();

            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if the input userID is the ID of an Administrator
     * 
     * @param userID
     * @return true if the input userID is the ID of an Administrator
     *         false in other cases
     */
    static boolean isAdminID(String userID) {
        try {
            Statement stm = con.createStatement();
            String sql = "SELECT 1 FROM ADMINISTRATOR WHERE ID = " + userID;
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next())
                return true;
            else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get the {@code User} object from the database
     * 
     * @param userID the ID of the user
     * @return the user object from the database,
     *         or null if user does not exist
     */
    static User getUser(String userID) {
        try {
            Statement stm = con.createStatement();
            String sql = "SELECT * FROM TISCH_USER WHERE ID = " + userID;
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                String username = rs.getString(2);
                String name = rs.getString(4);
                String email = rs.getString(5);
                String phone = rs.getString(6);

                return new User(userID, username, name, email, phone);
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * delete user by ID
     * 
     * @param userID
     * @return true if it can delete the user successfully
     *         false if it can not
     */
    static boolean deleteUser(String userID) {
        try {
            Statement stm = con.createStatement();
            String sql = "DELETE FROM TISCH_USER WHERE ID = " + userID;
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next())
                return true;
            else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
