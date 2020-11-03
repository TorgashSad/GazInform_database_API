import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class MyDAO {
    /**
     * Logger initialization
     */
    private static final Logger LOGGER = LogManager.getLogger(MyDAO.class);
    private String url;
    private String user;
    private String password;

    private Connection connection;

    public MyDAO(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void updateSurname(String name, String new_surname) {
        String SQL = "UPDATE gazinform_users "
                + "SET surname = ?"
                + "WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.setString(1, new_surname);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            System.out.println("The user surname successfully updated: " + name + " " + new_surname);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void findUserByName(String name) {
        String SQL = "SELECT name, surname "
                + "FROM gazinform_users "
                + "WHERE name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("The user found is: " + rs.getString("name") + "\t"
                        + rs.getString("surname"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void clearTable() {
        String SQL = "TRUNCATE gazinform_users";
        try (PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void addUser(String name, String surname) {
        String SQL = "INSERT INTO gazinform_users(name, surname) "
                + "VALUES(?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.setString(1, name);
            pstmt.setString(2, surname);
            pstmt.executeUpdate();
        }
        catch (SQLException ex) {
        System.out.println(ex.getMessage());
        }
    }

    public void showTable() {
        String SQL = "SELECT name, surname FROM gazinform_users";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            // display actor information
            System.out.println("The full table is: \n");
            while (rs.next()) {
                System.out.println(rs.getString("name") + "\t"
                        + rs.getString("surname"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public void connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            LOGGER.trace("Connected to database successfully.");
        } catch (SQLException e) {
            LOGGER.error("Error: connection to database wasn't established", e);
            System.out.println(e.getMessage());
        }
        connection = conn;
    }

}
