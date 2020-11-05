import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * These tests are for the case when an actual DB is available for connection
 */
public class MyDAOTests {

    private final MyDAO dao=new MyDAO();

    public MyDAOTests() throws SQLException {
    }

    /**
     * Adds a user to gazinform_users table and retrieves it back
     * from the table to check if the insert was successful
     */
    @Test
    public void addAndShowUserTest() {
        clearTable(dao.getConnection());
        User expected = new User("Maksim", "Smolencev");
        dao.addUser(expected);
        Optional<User> actual1 = dao.findUserByName("Maksim");
        assertEquals(expected, actual1.get());

        Optional<User> actual2 = dao.findUserByName("NotMaksim");
        assertFalse(actual2.isPresent());
    }

    /**
     * Adds a user to gazinform_users table, updates its surname and retrieves it back
     * from the table to check if insert and update were successful
     */
    @Test
    public void updateSurnameAndShowUserTest() {
        clearTable(dao.getConnection());
        User initial = new User("Alina", "Kvochkina");
        dao.addUser(initial);
        dao.updateSurname("Alina", "Kasparova");
        Optional<User> actual = dao.findUserByName("Alina");
        User expected = new User("Alina", "Kasparova");
        assertEquals(expected, actual.get());
    }
    /**
     * Clears the whole table gazinform_users
     */
    private static void clearTable(Connection connection) {
        String SQL = "TRUNCATE gazinform_users";
        try (PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
