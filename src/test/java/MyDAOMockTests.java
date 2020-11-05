import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MyDAOMockTests {

    @Mock
    private Connection c;

    @Mock
    private PreparedStatement pstmt;

    @Mock
    private Statement stmt;

    @Mock
    private ResultSet rs;

    @Mock
    private ResultSetMetaData rsmd;

    private User user;

    @Before
    public void setUp() throws Exception {
        when(c.prepareStatement(any(String.class))).thenReturn(pstmt);
        //when(c.createStatement()).thenReturn(stmt);

        user = new User("John", "Dorian");

        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.getMetaData()).thenReturn(rsmd);
        when(rsmd.getColumnName(1)).thenReturn("name");
        when(rsmd.getColumnName(2)).thenReturn("surname");
        when(rs.getString("name")).thenReturn(user.getName());
        when(rs.getString("surname")).thenReturn(user.getSurname());
    }

    @InjectMocks
    private final MyDAO testDAO=new MyDAO("","","");

    @Test(expected = java.lang.AssertionError.class)
    public void nullAddUserThrowsException() {
        testDAO.addUser(null);
    }

    @Test
    public void addAndShowUserTest() {
        testDAO.addUser(user);
        User actual1 = testDAO.findUserByName(user.getName());
        assertEquals(user.getName(), actual1.getName());
        assertEquals(user.getSurname(), actual1.getSurname());
    }

//    @Test
//    public void addUpdateAndShowUserTest() {
//        testDAO.addUser(user);
//        testDAO.updateSurname();
//        User actual1 = testDAO.findUserByName(user.getName());
//        assertEquals(user.getName(), actual1.getName());
//        assertEquals(user.getSurname(), actual1.getSurname());
//    }

}
