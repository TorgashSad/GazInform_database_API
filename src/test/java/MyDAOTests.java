import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MyDAOTests {
    /**
     * Path/name of the configuration file
     */
    private static final String CONFIGURATION_FILE_NAME = "config.properties";
    /**
     * Logger initialization
     */
    private static final Logger LOGGER = LogManager.getLogger(MyDAOTests.class);

    @Test
    public void connectTest() {
        Properties properties = Util.readPropertiesFile(CONFIGURATION_FILE_NAME);
        MyDAO dao = new MyDAO(properties.getProperty("postgreSQL_URL"),
                properties.getProperty("postgreSQL_User"),
                properties.getProperty("postgreSQL_Password"));
        dao.connect();
        assertNotNull(dao.getConnection());

        Properties wrongProperties = mock(Properties.class);
        when(wrongProperties.getProperty("postgreSQL_Password")).thenReturn("wrongpassword");
        dao = new MyDAO(properties.getProperty("postgreSQL_URL"),
                properties.getProperty("postgreSQL_User"),
                wrongProperties.getProperty("postgreSQL_Password"));
        dao.connect();
        assertNull(dao.getConnection());

    }

    @Test
    public void addAndShowUserTest() {
        Properties properties = Util.readPropertiesFile(CONFIGURATION_FILE_NAME);
        MyDAO dao = new MyDAO(properties.getProperty("postgreSQL_URL"),
                properties.getProperty("postgreSQL_User"),
                properties.getProperty("postgreSQL_Password"));
        dao.connect();
        dao.clearTable();
        dao.addUser("Maksimum", "Smolencev");
        Object actual = dao.findUserByName("Maksimum");
        Map<String, String> expected = Map.of("name", "Maksimum", "surname", "Smolencev");
        assertEquals(actual, expected);
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
