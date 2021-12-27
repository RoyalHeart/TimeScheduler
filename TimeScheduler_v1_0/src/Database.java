package src;

import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Database {
    final static String addUser = "INSERT INTO TISCH_USER VALUES (?, ?, ?, ?, ?, ?)";
    final static String databaseUsername = "S1_student2_46";
    static String databasePassword = "Password";

    public static boolean addTischUser(String username, String password, String name, String phone,
            String email) {

        // load the password from file
        try {
            File myObj = new File("ProgramJava/Project/TimeScheduler_v1_0/lib/DatabaseLoginInfo.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                databasePassword = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            // step1 load the driver class
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // step2 create the connection object
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@db1.fb2.frankfurt-university.de:1521:info01", databaseUsername,
                    databasePassword);

            // step3 create the prepared statement object
            PreparedStatement ps = con.prepareStatement(addUser);
            ps.setString(1, "");
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, name);
            ps.setString(5, phone);
            ps.setString(6, email);

            // step4 execute query
            ps.execute();
            ps.close();

            // step5 close the connection object
            con.close();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    static boolean existUser(String username, String hashPassword) {
        System.out.println(username + ' ' + hashPassword);
        try {
            System.out.println(username + ' ' + hashPassword);
            // step1 load the driver class
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // step2 create the connection object
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@db1.fb2.frankfurt-university.de:1521:info01", databaseUsername, "T@m3062002");

            // step3 create the statement object
            Statement stmt = con.createStatement();

            // step4 execute query
            ResultSet rs = stmt.executeQuery("SELECT * FROM TISCH_USER WHERE USERNAME = '" + username + "'");

            rs.next();
            System.out.println(rs.getString(2) + " " + rs.getString(3));
            // step 5 process the result set
            if (rs.getString(3).equals(hashPassword.toUpperCase())) {
                System.out.println(rs.getString(2) + " " + rs.getString(3));
                con.close();
                return true;
            } else {
                System.out.println(rs.getString(2) + " " + rs.getString(3));
                con.close();
                return false;
            }

            // step 6 close the connection object

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    static boolean existAdmin(String username, String hashPassword) {
        try {
            // step1 load the driver class
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // step2 create the connection object
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@db1.fb2.frankfurt-university.de:1521:info01", databaseUsername, "T@m3062002");

            // step3 create the statement object
            Statement stmt = con.createStatement();

            // step4 execute query
            ResultSet rs = stmt.executeQuery("SELECT * FROM TISCH_USER WHERE USERNAME = '" + username + "'");

            // step5 process the result set
            if (rs.next()) {
                if (rs.getString(3).equals(hashPassword)) {
                    return true;
                } else {
                    return false;
                }
            }

            // step6 close the connection object
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }
}
