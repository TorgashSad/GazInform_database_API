import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * This class implements an API for interaction with a table "gazinform_users" in a database
 * Initially, an instance of a this class must be created with database and user credentials and method
 * connect() executed. Then, other methods are ready for use.
 */
public class MyDAO {
    /**
     * Logger initialization
     */
    private static final Logger LOGGER = LogManager.getLogger(MyDAO.class);
    private final String url;
    private final String user;
    private final String password;

    private Connection connection;

    public MyDAO(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Connect to the PostgreSQL database
     */
    public void connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            LOGGER.info("Connected to database successfully.");
        } catch (SQLException e) {
            LOGGER.error("Error: connection to database wasn't established", e);
            System.out.println(e.getMessage());
        }
        connection = conn;
    }
    /**
     * Clears the whole table gazinform_users
     */
    public void clearTable() {
        String SQL = "TRUNCATE gazinform_users";
        try (PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    /**
     * Add a new user into the table gazinform_users
     * @param user a User class object
     */
    public void addUser(User user) {
        String SQL = "INSERT INTO gazinform_users(name, surname) "
                + "VALUES(?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getSurname());
            pstmt.executeUpdate();
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    /**
     * Finds a user by its user name
     * @param name the name of the user
     * @return a LinkedHashMap, where keys a column names and values correspond to a user found
     */
    public User findUserByName(String name) {
        String SQL = "SELECT * "
                + "FROM gazinform_users "
                + "WHERE name = ?";
        User user = null;
        try (PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            rs.next();
            user = new User(rs.getString(rsmd.getColumnName(1)), rs.getString(rsmd.getColumnName(2)));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return user;
    }
    /**
     * Updates the value of surname column for a user specified by name column value
     * @param name name of a user
     * @param new_surname new surname for a user
     */
    public void updateSurname(String name, String new_surname) {
        String SQL = "UPDATE gazinform_users "
                + "SET surname = ?"
                + "WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.setString(1, new_surname);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            LOGGER.info("The user surname successfully updated: " + name + " " + new_surname);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Returns the whole table "gazinform_users" as a ResultSet
     */
    public ResultSet getTable() {
        String SQL = "SELECT * FROM gazinform_users";
        ResultSet rs=null;
        try (Statement stmt = connection.createStatement()) {
            rs = stmt.executeQuery(SQL);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return rs;
    }
}
