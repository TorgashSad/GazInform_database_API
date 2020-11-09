package com.torgashsad.gazinform_api;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.Optional;

/**
 * This class implements an API for interaction with a table "gazinform_users" in a database
 * When an instance of a this class is created, it reads the database and user credentials
 * from system properties (injected through maven settings.xml by surefire plugin),
 * automatically creates a connection to a database and becomes fully functional.
 * Its constructor @throws SQLException if it fails to connect to a database
 * A new MyDAO instance for each thread in a multi-thread environment is highly reccomended
 */
@Log4j2
public class MyDAO {
    @Getter
    private Connection connection;

    public MyDAO() throws SQLException {
        connection = DriverManager.getConnection(System.getProperty("postgreSQL_URL"),
                System.getProperty("postgreSQL_User"),
                System.getProperty("postgreSQL_Password"));
        log.info("Database on URL {} was connected successfully", System.getProperty("postgreSQL_URL"));
    }
    /**
     * Add a new user into the table gazinform_users
     * @param user a not null User class object
     * @return 1 if a user was added successfully, otherwise 0
     */
    public int addUser(@NonNull User user) {
        int rowCount=0;
        String sql = "INSERT INTO gazinform_users(name, surname) "
                + "VALUES(?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getSurname());
            rowCount = pstmt.executeUpdate();
        }
        catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        if (rowCount==1)
            log.info("The user successfully added: {} {}.", user.getName(), user.getSurname());
        return rowCount;
    }
    /**
     * Finds a user by its user name
     * @param name the name of the user
     * @return an Optional of User corresponding to a user found or not
     */
    public Optional<User> findUserByName(String name) {
        String sql = "SELECT * "
                + "FROM gazinform_users "
                + "WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            rs.next();
            return Optional.of(new User(rs.getString(rsmd.getColumnName(1)),
                    rs.getString(rsmd.getColumnName(2))));
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return Optional.empty();
    }
    /**
     * Updates the value of surname column for a user specified by name column value
     * @param name name of a user
     * @param newSurname new surname for a user
     * @return 1 if a surname was updated successfully, otherwise 0
     */
    public int updateSurname(String name, String newSurname) {
        int rowCount=0;
        String sql = "UPDATE gazinform_users "
                + "SET surname = ?"
                + "WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newSurname);
            pstmt.setString(2, name);
            rowCount = pstmt.executeUpdate();
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        if (rowCount==1)
            log.info("The user surname successfully updated: {} {}.", name, newSurname);
        return rowCount;
    }
}
