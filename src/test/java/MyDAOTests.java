import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.JVM)
public class MyDAOTests {
    /**
     * Path/name of the configuration file
     */
    private static final String CONFIGURATION_FILE_NAME = "config.properties";
    /**
     * Logger initialization
     */
    private static final Logger LOGGER = LogManager.getLogger(MyDAOTests.class);

    private static final Properties properties = Util.readPropertiesFile(CONFIGURATION_FILE_NAME);

    private static final MyDAO dao = new MyDAO(properties.getProperty("postgreSQL_URL"),
            properties.getProperty("postgreSQL_User"),
            properties.getProperty("postgreSQL_Password"));

    @Test
    public void connectTest() {
        //Correct credentials
        MyDAO localDao = new MyDAO(properties.getProperty("postgreSQL_URL"),
                properties.getProperty("postgreSQL_User"),
                properties.getProperty("postgreSQL_Password"));
        localDao.connect();
        assertNotNull(localDao.getConnection());

        Properties wrongProperties = mock(Properties.class);
        when(wrongProperties.getProperty("postgreSQL_Password")).thenReturn("wrongpassword");
        //INCORRECT credentials
        localDao = new MyDAO(properties.getProperty("postgreSQL_URL"),
                properties.getProperty("postgreSQL_User"),
                wrongProperties.getProperty("postgreSQL_Password"));
        localDao.connect();
        assertNull(localDao.getConnection());
    }

    @Test
    public void addAndShowUserTest() {
        dao.connect();
        dao.clearTable();
        User expected = new User("Maksim", "Smolencev");
        dao.addUser(expected);
        Object actual1 = dao.findUserByName("Maksim");
        assertEquals(actual1, expected);

        Object actual2 = dao.findUserByName("NotMaksim");
        assertNotEquals(actual2, expected);
    }

    @Test
    public void updateSurnameAndShowUserTest() {
        dao.connect();
        dao.clearTable();
        User initial = new User("Alina", "Kvochkina");
        dao.addUser(initial);
        dao.updateSurname("Alina", "Kasparova");
        Object actual = dao.findUserByName("Alina");
        User expected = new User("Alina", "Kasparova");
        assertEquals(actual, expected);
    }

    @Test
    public void getTableTest() {
        dao.connect();
        dao.clearTable();
        Object actual = dao.getTable();
        assertThat(actual, instanceOf(ResultSet.class));
        assertNotNull(actual);
    }

    public void printRS(ResultSet rs) {
        LOGGER.info("The full table is:");
        try {
        ResultSetMetaData rsmd = rs.getMetaData();
            for (int i=1; i<=rsmd.getColumnCount();i++)
                System.out.print(rsmd.getColumnName(i) + "\t");
            System.out.print("\n");
            while (rs.next()) {
                System.out.println(rs.getString("name") + "\t"
                        + rs.getString("surname"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
