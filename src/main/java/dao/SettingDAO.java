package dao;

import config.MariaDbConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class SettingDAO {
    Connection conn = null;

    // get language setting
    public Map<String, String> getLanguage() {
        conn = MariaDbConnection.getConnection();
        String sql = "SELECT * FROM setting";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (!rs.next()) {
                addLanguage();
                rs = st.executeQuery(sql);
            }

            String language = rs.getString("language");
            String region = rs.getString("region");
            Map<String, String> setting = new HashMap<>();
            setting.put("language", language);
            setting.put("region", region);
                return setting;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Tähän vois pistää default languagen (english)
    }

    // set language setting
    public void addLanguage() {
        conn = MariaDbConnection.getConnection();
        String sql = "INSERT setting SET language = ?, region = ?";
        try {
            var ps = conn.prepareStatement(sql);
            ps.setString(1, "en");
            ps.setString(2, "UK");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // update language setting
    public void setLanguage(String language, String region) {
        conn = MariaDbConnection.getConnection();
        String sql = "UPDATE setting SET language = ?, region = ? WHERE setting_id = 1";
        try {
            var ps = conn.prepareStatement(sql);
            ps.setString(1, language);
            ps.setString(2, region);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
