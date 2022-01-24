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

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.properties.EncryptableProperties;

public class Database {
    final static String addUser = "INSERT INTO TISCH_USER (ID, USERNAME, PASSWORD, USERFULLNAME, USEREMAIL, USERPHONENUMBER) VALUES (?, ?, ?, ?, ?, ?)";
    final static String addEvent = "INSERT INTO EVENT (ID, USERID, EVENTTITLE, EVENTDESCRIPTION, EVENTDATE, EVENTREMIND, EVENTLOCATION, EVENTDURATION, EVENTPRIORITY) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    static String jasyptPassword = getJasyptPassword();
    final static Properties databaseProperties = getDatabaseProperties();
    static Connection con;

    // create database connection to the Oracle database
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

    // read password from file
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

    // add user to database
    // hash password is hash(password + username) with SHA-256
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

    // check if username exists in database
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

    // check if user exists in database
    // check for login
    static boolean existUser(String username, String hashPassword) {
        try {
            // create statement object
            // con = createConnection();
            Statement stmt = con.createStatement();

            // create a query
            ResultSet rs = stmt.executeQuery("SELECT * FROM TISCH_USER WHERE USERNAME = '" + username + "'");

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

    // check if the user is an admin or not
    static boolean existAdmin(String username, String hashPassword) {
        try {
            // create the connection object
            // Connection con = createConnection();

            // create the statement object
            Statement stmt = con.createStatement();

            // execute query
            ResultSet rs = stmt.executeQuery("SELECT * FROM TISCH_USER WHERE USERNAME = '" + username + "'");

            // process the result set
            if (rs.next()) {
                String userID = rs.getString(1);
                if (rs.getString("PASSWORD").equals(hashPassword)) {
                    rs = stmt.executeQuery("SELECT * FROM ADMINISTRATOR WHERE ID = '" + rs.getString(1) + "'");
                    if (userID.equals(rs.getString(1))) {
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

    static User getUser(String username, String hashPassword) {
        try {
            // create the connection object
            // Connection con = createConnection();

            // create the statement object
            Statement stmt = con.createStatement();

            // execute query
            ResultSet rs = stmt.executeQuery("SELECT * FROM TISCH_USER WHERE USERNAME = '" + username + "'");

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

    static User getAdmin(String username, String hashPassword) {
        try {

            // create the connection object
            // Connection con = createConnection();

            // create the statement object
            Statement stmt = con.createStatement();

            // execute query
            ResultSet rs = stmt.executeQuery("SELECT * FROM TISCH_USER WHERE USERNAME = '" + username + "'");

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

    // add event of a user to database
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

    // get all events from a user from database
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
}
