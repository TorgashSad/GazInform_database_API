import com.torgashsad.gazinform_api.MyDAO;
import com.torgashsad.gazinform_api.User;
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

    private final MyDAO testDAO=new MyDAO();

    public MyDAOTests() throws SQLException {
    }

    /**
     * Adds a user to gazinform_users table and retrieves it back
     * from the table to check if the insert was successful
     */
    @Test
    public void addAndShowUserTest() {
        clearTable(testDAO.getConnection());
        User expected = new User("Maksim", "Smolencev");
        int result = testDAO.addUser(expected);
        assertEquals(1, result);

        Optional<User> actual1 = testDAO.findUserByName("Maksim");
        assertEquals(expected, actual1.get());

        Optional<User> actual2 = testDAO.findUserByName("NotMaksim");
        assertFalse(actual2.isPresent());
    }

    /**
     * Adds a user to gazinform_users table, updates its surname and retrieves it back
     * from the table to check if insert and update were successful
     */
    @Test
    public void updateSurnameAndShowUserTest() {
        clearTable(testDAO.getConnection());
        User initial = new User("Alina", "Kvochkina");
        int result = testDAO.addUser(initial);
        assertEquals(1, result);

        result = testDAO.updateSurname("Alina", "Kasparova");
        assertEquals(1, result);

        Optional<User> actual = testDAO.findUserByName("Alina");
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
