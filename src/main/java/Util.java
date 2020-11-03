import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Util {
    /**
     * Logger initialization
     */
    private static final Logger LOGGER = LogManager.getLogger(Util.class);
    /**
     * This method reads the configuration file and return a Properties object
     * @param CONFIGURATION_FILE_NAME Name/Path to the configuration file
     * @return Properties object
     */
    static Properties readPropertiesFile(String CONFIGURATION_FILE_NAME) {
        FileInputStream fis = null;
        Properties prop = null;
        try {
            fis = new FileInputStream(CONFIGURATION_FILE_NAME);
            prop = new Properties();
            prop.load(fis);
            LOGGER.info("Properties file was successfully read.");
        } catch(Exception e) {
            LOGGER.error("Error: properties file not found.", e);
        } finally {
            try {
                assert fis != null;
                fis.close();
            }
            catch (IOException e){
                LOGGER.error("Error: properties file wasn't closed.", e);
            }
        }
        return prop;
    }
}
