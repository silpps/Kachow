package config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A class that creates a connection to the database.
 *
 * @author Veera Ruotsalainen
 */
public final class MariaDbConnection {
    private static Connection conn = null;

    /**
     * Private constructor to prevent instantiation of the class.
     */
    private MariaDbConnection() {
    }

    /**
     * A method that retrieves a connection to the database.
     * @return returns the connection to the database.
     */
    public static synchronized Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(
                        DBConfig.getUrl()+"?"+"user="+DBConfig.getUsername()+"&password="+DBConfig.getPassword());
                System.out.println("Connection successful");
            } catch (SQLException e) {
                System.out.println("Connection failed");
                e.printStackTrace();
            }
        }
        return conn;
    }

}