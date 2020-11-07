import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.*;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
/**
 * These tests are for the case when there is no DB available
 */
@RunWith(MockitoJUnitRunner.class)
public class MyDAOMockTests {

    @Mock
    private Connection c;

    @Mock
    private PreparedStatement pstmt;

    @Mock
    private ResultSet rs;

    @Mock
    private ResultSetMetaData rsmd;

    private User user;

    @Before
    public void setUp() throws Exception {
        when(c.prepareStatement(any(String.class))).thenReturn(pstmt);

        user = new User("John", "Dorian");

        when(pstmt.executeQuery()).thenReturn(rs);
        when(pstmt.executeUpdate()).thenReturn(1);
        when(rs.getMetaData()).thenReturn(rsmd);
        when(rsmd.getColumnName(1)).thenReturn("name");
        when(rsmd.getColumnName(2)).thenReturn("surname");
        when(rs.getString("name")).thenReturn(user.getName());
        when(rs.getString("surname")).thenReturn(user.getSurname());
    }

    @InjectMocks
    private MyDAO testDAO;

    /**
     * Null User is forbidden to add to gazinform_users table
     */
    @Test(expected = java.lang.AssertionError.class)
    public void nullAddUserThrowsException() {
        testDAO.addUser(null);
    }

    /**
     * Adds a user to gazinform_users table and retrieves it back
     * from the table to check if the insert was successful
     */
    @Test
    public void addAndShowUserTest() {
        int result = testDAO.addUser(user);
        assertEquals(1, result);

        Optional<User> actual1 = testDAO.findUserByName(user.getName());
        assertEquals(user.getName(), actual1.get().getName());
        assertEquals(user.getSurname(), actual1.get().getSurname());
    }

    /**
     * Adds a user to gazinform_users table, updates its surname and retrieves it back
     * from the table to check if insert and update were successful
     */
    @Test
    public void addUpdateAndShowUserTest() throws SQLException {
        int result = testDAO.addUser(user);
        assertEquals(1, result);
        result = testDAO.updateSurname(user.getName(), "Nairod");
        verify(pstmt).setString(1,"Nairod");
        verify(pstmt).setString(2,user.getName());
        assertEquals(1, result);
    }

}
