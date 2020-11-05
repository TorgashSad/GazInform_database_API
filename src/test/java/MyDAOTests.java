import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    /**
     * Checks a proper behavior of MyDAO.connect() method with correct and incorrect DB credentials
     */
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
    /**
     * Adds a user to gazinform_users table and retrieves it back
     * from the table to check if insert was successful
     */
    @Test
    public void addAndShowUserTest() {
        dao.connect();
        dao.clearTable();
        User expected = new User("Maksim", "Smolencev");
        dao.addUser(expected);
        Object actual1 = dao.findUserByName("Maksim");
        assertEquals(expected, actual1);

        Object actual2 = dao.findUserByName("NotMaksim");
        assertNotEquals(expected, actual2);
    }

    /**
     * Adds a user to gazinform_users table, updates its surname and retrieves it back
     * from the table to check if insert and update were successful
     */
    @Test
    public void updateSurnameAndShowUserTest() {
        dao.connect();
        dao.clearTable();
        User initial = new User("Alina", "Kvochkina");
        dao.addUser(initial);
        dao.updateSurname("Alina", "Kasparova");
        Object actual = dao.findUserByName("Alina");
        User expected = new User("Alina", "Kasparova");
        assertEquals(expected, actual);
    }
    /**
     * Returns a table gazinform_users and checks if it is of ResultSet class and not null
     */
    @Test
    public void getTableTest() {
        dao.connect();
        dao.clearTable();
        Object actual = dao.getTable();
        assertThat(actual, instanceOf(ResultSet.class));
        assertNotNull(actual);
    }

    //Supplementary method, may be used or completely removed later
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
