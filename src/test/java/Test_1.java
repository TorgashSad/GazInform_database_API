import java.util.Properties;

public class Test_1 {
    /**
     * Path/name of the configuration file
     */
    private static final String CONFIGURATION_FILE_NAME = "config.properties";

    public static void main(String[] args) {
        Properties properties = Util.readPropertiesFile(CONFIGURATION_FILE_NAME);
        MyDAO dao = new MyDAO(properties.getProperty("postgreSQL_URL"),
                properties.getProperty("postgreSQL_User"),
                properties.getProperty("postgreSQL_Password"));
        dao.connect();
        dao.clearTable();
        dao.addUser("Aleksei", "Antoniuk");
        dao.addUser("Alina", "Kvochkina");
        dao.addUser("Petr", "Sidorov");
        dao.addUser("Maksim", "Smolencev");
        dao.findUserByName("Petr");
        dao.updateSurname("Petr", "Pushkin");
        dao.showTable();


        //System.out.println(dao.exampleFunction());
    }
}
