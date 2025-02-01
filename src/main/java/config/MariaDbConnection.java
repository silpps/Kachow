package config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Luokka, joka luo yhteyden tietokantaan.
 *
 * @author Veera Ruotsalainen
 */
public class MariaDbConnection {
    private static Connection conn = null;

    /**
     * konstruktori joka hakee yhteyden tietokantaan
     * @return palauttaa yhteyden tietokantaan.
     */
    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(
                        "jdbc:mariadb://localhost:3306/study_palanner?user=student_test&password=schedule");
            } catch (SQLException e) {
                System.out.println("Connection failed");
            }
        }
        return conn;
    }

}