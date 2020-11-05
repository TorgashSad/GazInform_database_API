import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.sql.*;
import java.util.Optional;
import java.util.Properties;

/**
 * This class implements an API for interaction with a table "gazinform_users" in a database
 * When an instance of a this class is created, it reads the database and user credentials from a
 * configuration file, automatically creates a connection to a database and becomes fully functional.
 * Its constructor @throws SQLException if it fails to connect to a database
 */
public class MyDAO {
    /**
     * Logger initialization
     */
    private static final Logger LOGGER = LogManager.getLogger(MyDAO.class);
    /**
     * Path/name of the configuration file
     */
    private static final String CONFIGURATION_FILE_NAME = "config.properties";

    @Getter
    private Connection connection;

    public MyDAO() throws SQLException {
        Properties properties = Util.readPropertiesFile(CONFIGURATION_FILE_NAME);
        connection = DriverManager.getConnection(properties.getProperty("postgreSQL_URL"),
                properties.getProperty("postgreSQL_User"),
                properties.getProperty("postgreSQL_Password"));
    }
    /**
     * Add a new user into the table gazinform_users
     * @param user a not null User class object
     * @return 1 if a user was added successfully, otherwise 0
     */
    public int addUser(User user) {
        Assert.assertNotNull(user);
        int rowCount=0;
        String SQL = "INSERT INTO gazinform_users(name, surname) "
                + "VALUES(?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getSurname());
            rowCount = pstmt.executeUpdate();
        }
        catch (SQLException ex) {
            LOGGER.error(ex.getMessage());
        }
        return rowCount;
    }
    /**
     * Finds a user by its user name
     * @param name the name of the user
     * @return an Optional of User corresponding to a user found or not
     */
    public Optional<User> findUserByName(String name) {
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
            LOGGER.error(ex.getMessage());
        }
        return Optional.ofNullable(user);
    }
    /**
     * Updates the value of surname column for a user specified by name column value
     * @param name name of a user
     * @param new_surname new surname for a user
     * @return 1 if a surname was updated successfully, otherwise 0
     */
    public int updateSurname(String name, String new_surname) {
        int rowCount=0;
        String SQL = "UPDATE gazinform_users "
                + "SET surname = ?"
                + "WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.setString(1, new_surname);
            pstmt.setString(2, name);
            rowCount = pstmt.executeUpdate();
            LOGGER.info("The user surname successfully updated: " + name + " " + new_surname);
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage());
        }
        return rowCount;
    }
}
