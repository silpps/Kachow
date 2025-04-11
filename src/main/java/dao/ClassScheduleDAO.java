package dao;

import config.MariaDbConnection;
import models.ClassSchedule;

import java.sql.*;

public class ClassScheduleDAO implements IDAO<ClassSchedule> {
    private Connection conn = null;


    // Get a ClassSchedule from the database
    @Override
    public ClassSchedule get(int id) {
        conn = MariaDbConnection.getConnection();
        String sql = "SELECT * FROM class_schedule WHERE id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,  id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new ClassSchedule(
                        rs.getInt("course_id"),
                        rs.getString("days_of_week"),
                        rs.getString("location"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add a new ClassSchedule to the database
    @Override
    public void add(ClassSchedule classSchedule) {
        conn = MariaDbConnection.getConnection();
        String sql = "INSERT INTO class_schedule (course_id, location, description, start_time, end_time) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, classSchedule.getCourseId());
            st.setString(2, classSchedule.getLocation());
            st.setString(3, classSchedule.getDescription());
            st.setTimestamp(4, Timestamp.valueOf(classSchedule.getStartTime()));
            st.setTimestamp(5, Timestamp.valueOf(classSchedule.getEndTime()));
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                classSchedule.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update a ClassSchedule in the database
    @Override
    public void update(ClassSchedule classSchedule) {
        conn = MariaDbConnection.getConnection();
        String sql = "UPDATE class_schedule SET location = ?, description = ?, start_time = ?, end_time = ? WHERE class_id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, classSchedule.getLocation());
            st.setString(2, classSchedule.getDescription());
            st.setTimestamp(3, Timestamp.valueOf(classSchedule.getStartTime()));
            st.setTimestamp(4, Timestamp.valueOf(classSchedule.getEndTime()));
            st.setString(5, String.valueOf(classSchedule.getId()));
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a ClassSchedule from the database
    @Override
    public void delete(int id) {
        conn = MariaDbConnection.getConnection();
        String sql = "DELETE FROM class_schedule WHERE class_id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
