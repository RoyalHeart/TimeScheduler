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
import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.properties.EncryptableProperties;

/**
 * <p>
 * {@code Database} is used to connect to the database and perform CRUD.
 * <p>
 * <p>
 * It is used to store the information of the {@link User}s, administrators and
 * {@link Event}s.
 * </p>
 * 
 * @author Tam Thai Hoang 1370674
 * @author Huy Truong Quang (get list of user)
 * @author Sang Doan Tan (update user profile)
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
     * The {@code jasyptPassword} to decrypt the {@code database properties}file.
     */
    static String jasyptPassword = getJasyptPassword();
    
    /**
     * The encrypted {@link Properties} to get database configuration for connection.
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
     * add user to database and return if the user is added successfully
     * 
     * @param username     username of the user
     * @param hashPassword hash(password+username) of the user
     * @param name         full name of the user
     * @param email        email of the user
     * @param phone        phone number of the user
     * @return true if the user is added successfully,
     *         false otherwise
     * 
     * 
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
     * <p>
     * <p>
     * Using for login purpose
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
     * <p>
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
     * @param username     username of the admin
     * @param hashPassword the hashed password
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
                    .executeQuery("SELECT * FROM EVENT WHERE USERID = '" + user.getId() + "'"
                            + " ORDER BY EventDate");

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
                events.add(event);
            }
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
        // create the statement object, statement object allows you to sends SQL
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
    
    static boolean updateEmail(String email, User user)
    {
        try
        {
            Statement stmt = con.createStatement();
            if (stmt.executeUpdate("UPDATE TISCH_USER SET USEREMAIL = '" + email + "' WHERE id = " + user.getId()) == 0)
            {
                return false;
            }
            else {
                System.out.println("Email is updated");
                return true;}
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    static boolean updatePhone(String phone, User user)
    {
        try {
            Statement stmt = con.createStatement();
            if (stmt.executeUpdate("UPDATE TISCH_USER SET USERPHONENUMBER = '" + phone + "' WHERE id = " + user.getId()) == 0)
            {
                return false;
            }
            else 
            {
                System.out.println("Phone is updated.");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    static boolean updatePassword(String password, User user)
    {
        try {
            Statement stmt = con.createStatement();
            if (stmt.executeUpdate(
                    "UPDATE TISCH_USER SET PASSWORD = '" + Hash.hashPassword(password + user.getUsername()).toUpperCase() + "' WHERE id = " + user.getId()) == 0)
            {
                return false;
            }
            else 
            {
                System.out.println("Password is updated.");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get all user to make a list of all users from the database
     * 
     * @return a list of all users in the database
     */
    static Vector<Vector> getUserList() {
        try {
            Statement stm = con.createStatement();
            String sql = "SELECT ID, USERNAME, USERFULLNAME, USEREMAIL FROM TISCH_USER";
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
}
