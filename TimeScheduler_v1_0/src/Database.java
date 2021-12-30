package src;

import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Database {
    final static String addUser = "INSERT INTO TISCH_USER VALUES (?, ?, ?, ?, ?, ?)";
    final static String databaseUsername = "S1_student2_46";
    static String databasePassword = getDatabasePassword();
    static Connection con;

    // create database connection to the Oracle database
    static Connection createConnection() {
        try {
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
        File myObj = new File("ProgramJava/Project/TimeScheduler_v1_0/lib/DatabaseLoginInfo.txt");
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
    public static boolean addTischUser(String username, String password, String name, String phone,
            String email) {
        try {
            // create the connection object
            // Connection con = createConnection();

            // create the prepared statement object
            PreparedStatement ps = con.prepareStatement(addUser);
            ps.setString(1, "");
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, name);
            ps.setString(5, phone);
            ps.setString(6, email);

            // execute query
            ps.execute();
            ps.close();

            // close the connection object
            // con.close();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
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
}
