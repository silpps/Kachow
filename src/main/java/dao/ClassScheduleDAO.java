package dao;

import config.MariaDbConnection;
import models.ClassSchedule;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClassScheduleDAO implements IDAO<ClassSchedule> {
    private Connection conn = null;



    @Override
    public ClassSchedule get(String id) {
        conn = MariaDbConnection.getConnection();
        String sql = "SELECT * FROM class_schedule WHERE id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,  Integer.parseInt(id));
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new ClassSchedule(
                        rs.getString("course_name"),
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

    @Override
    public void add(ClassSchedule classSchedule) {
        conn = MariaDbConnection.getConnection();
        String sql = "INSERT INTO class_schedule (course_name, day_of_week, location, start_time, end_time) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, classSchedule.getCourseName());
            System.out.println(classSchedule.getDayOfWeek());
            st.setString(2, classSchedule.getDayOfWeek());
            st.setString(3, classSchedule.getLocation());
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

    @Override
    public void update(ClassSchedule classSchedule) {
        conn = MariaDbConnection.getConnection();
        String sql = "UPDATE assignment SET (course_name, days_of_week, location, start_time, end_time) VALUES (?, ?, ?, ?, ?) WHERE class_id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, classSchedule.getCourseName());
            st.setString(2, classSchedule.getDayOfWeek());
            st.setString(3, classSchedule.getLocation());
            st.setTimestamp(4, Timestamp.valueOf(classSchedule.getStartTime()));
            st.setTimestamp(5, Timestamp.valueOf(classSchedule.getEndTime()));
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        conn = MariaDbConnection.getConnection();
        String sql = "DELETE FROM class_schedule WHERE class_id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, Integer.parseInt(id));
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
