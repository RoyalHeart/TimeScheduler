package src;

import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Properties;

public class Database {
    final static String addUser = "INSERT INTO TISCH_USER VALUES (?, ?, ?, ?, ?, ?)";
    final static String addEvent = "INSERT INTO EVENT VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    final static String databaseUsername = "S1_student2_46";
    static String databasePassword = getDatabasePassword();
    static Connection con;

    // create database connection to the Oracle database
    static Connection createConnection() {
        try {
            Properties connectionProps = new Properties();
            // step1 load the driver class
            // Class<?> driverClass = Class.forName("oracle.jdbc.driver.OracleDriver");
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // step2 create the connection object
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@db1.fb2.frankfurt-university.de:1521:info01",
                    databaseUsername,
                    databasePassword);
            return con;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
            return null;
        }
    };

    // read password from file
    private static String getDatabasePassword() {
        File myObj = new File("TimeScheduler_v1_0/lib/DatabaseLoginInfo.txt");
        try {
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                databasePassword = myReader.nextLine();
            }
            myReader.close();
            return databasePassword;
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

    // check if user exists in database
    // check for login
    static boolean existUser(String username, String hashPassword) {
        System.out.println(username + ' ' + hashPassword);
        try {
            System.out.println(username + ' ' + hashPassword);
            // create statement object
            // con = createConnection();
            Statement stmt = con.createStatement();

            // create a query
            ResultSet rs = stmt.executeQuery("SELECT * FROM TISCH_USER WHERE USERNAME = '" + username + "'");

            // execute query
            rs.next();
            // System.out.println(rs.getString(2) + " " + rs.getString(3));
            // step 5 process the result set
            // if user exists
            if (rs.getString(3).equals(hashPassword.toUpperCase())) {
                // System.out.println(rs.getString(2) + " " + rs.getString(3));
                // con.close();
                return true;
            } else { // if user doesn't exist
                // System.out.println(rs.getString(2) + " " + rs.getString(3));
                // con.close();
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

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
                if (rs.getString(3).equals(hashPassword) && rs.getString(1).equals("0")) {
                    return true;
                } else {
                    return false;
                }
            }

            // close the connection object
            // con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
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
                String userName = rs.getString(3);
                String name = rs.getString(4);
                String email = rs.getString(5);
                String phone = rs.getString(6);

                if (rs.getString(3).equals(hashPassword)) {
                    return new User(userID, userName, name, email, phone);
                } else {
                    return null;
                }
            }

            // close the connection object
            // con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
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
            if (rs.next() && rs.getString(1).equals("0")) {
                User user = new User(rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
                // con.close();
                return user;
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
            System.out.println("event Date" + eventDate);
            java.sql.Date eventStartTime = new java.sql.Date(event.getStartTime().getTime());
            System.out.println("eventStartTime" + eventStartTime);
            PreparedStatement ps = con.prepareStatement(addEvent);
            ps.setString(1, "");
            ps.setString(2, event.getUserID());
            ps.setString(3, event.getTitle());
            ps.setString(4, event.getDescription());
            ps.setDate(5, eventDate);
            ps.setDate(6, eventStartTime);
            ps.setString(7, event.getLocation());
            ps.setInt(8, event.getDuration());
            ps.setInt(9, event.getPriority());
            ps.setInt(10, event.getRemind());

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
                    .executeQuery("SELECT * FROM EVENT WHERE USERID = '" + user.getId() + "'" + " ORDER BY EventDate");

            // process the result set
            while (rs.next()) {
                String id = rs.getString(1);
                String userid = rs.getString(2);
                String title = rs.getString(3);
                String description = rs.getString(4);
                Date Date = rs.getDate(5);
                Date startTime = rs.getDate(6);
                String location = rs.getString(7);
                int duration = rs.getInt(8);
                int priority = rs.getInt(9);
                int remind = rs.getInt(10);

                Event event = new Event(userid, title, description, Date, startTime, location, duration, priority,
                        remind);
                events.add(event);
            }
            return events;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
