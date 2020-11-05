import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.*;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MyDAOMockTests {

    @Mock
    private Connection c;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
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
        when(rs.getMetaData()).thenReturn(rsmd);
        when(rsmd.getColumnName(1)).thenReturn("name");
        when(rsmd.getColumnName(2)).thenReturn("surname");
        when(rs.getString("name")).thenReturn(user.getName());
        when(rs.getString("surname")).thenReturn(user.getSurname());
    }

    @InjectMocks
    private MyDAO testDAO;

    @Test(expected = java.lang.AssertionError.class)
    public void nullAddUserThrowsException() {
        testDAO.addUser(null);
    }

    @Test
    public void addAndShowUserTest() {
        testDAO.addUser(user);
        Optional<User> actual1 = testDAO.findUserByName(user.getName());
        assertEquals(user.getName(), actual1.get().getName());
        assertEquals(user.getSurname(), actual1.get().getSurname());
    }

    @Test
    public void addUpdateAndShowUserTest() throws SQLException {
        testDAO.addUser(user);
        testDAO.updateSurname(user.getName(), "Nairod");
        verify(pstmt).setString(1,"Nairod");
        verify(pstmt).setString(2,user.getName());
        verify(pstmt, times(2)).executeUpdate();
        //Optional<User> actual1 = testDAO.findUserByName(user.getName());
        //assertEquals(user.getSurname(), actual1.get().getSurname());
    }

}
