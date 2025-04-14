package dao;

import config.MariaDbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (!rs.next()) {
                addLanguage();
                try (ResultSet rs2 = st.executeQuery(sql)) {
                    if (rs2.next()) {
                        return getSettingResult(rs2);
                    }
                }
            } else {
                return getSettingResult(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, String> getSettingResult(ResultSet rs) throws SQLException {
        String language = rs.getString("language");
        String region = rs.getString("region");
        Map<String, String> setting = new HashMap<>();
        setting.put("language", language);
        setting.put("region", region);
        return setting;
    }

    // set language setting
    public void addLanguage() {
        conn = MariaDbConnection.getConnection();
        String sql = "INSERT INTO setting (language, region) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, language);
            ps.setString(2, region);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}