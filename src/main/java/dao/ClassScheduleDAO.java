package dao;

import config.MariaDbConnection;
import models.ClassSchedule;

import java.sql.*;

/**
 * Data Access Object (DAO) class for managing {@link ClassSchedule} objects in the database.
 * Implements {@link IDAO} interface for CRUD (Create, Read, Update, Delete) operations.
 */
public class ClassScheduleDAO implements IDAO<ClassSchedule> {
    private Connection conn = null;


    /**
     * Retrieves a ClassSchedule from the database by its ID.
     * @param id the ID of the class schedule to retrieve
     * @return the {@link ClassSchedule} object with the specified ID, or null if not found
     */
    @Override
    public ClassSchedule get(int id) {
        conn = MariaDbConnection.getConnection();
        String sql = "SELECT id, course_id, days_of_week, location, start_time, end_time FROM class_schedule WHERE class_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new ClassSchedule(
                            rs.getInt("course_id"),
                            rs.getString("days_of_week"),
                            rs.getString("location"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Adds a new class schedule to the database.
     * @param classSchedule the {@link ClassSchedule} object to add
     */
    @Override
    public void add(ClassSchedule classSchedule) {
        conn = MariaDbConnection.getConnection();
        String sql = "INSERT INTO class_schedule (course_id, location, description, start_time, end_time) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, classSchedule.getCourseId());
            st.setString(2, classSchedule.getLocation());
            st.setString(3, classSchedule.getDescription());
            st.setTimestamp(4, Timestamp.valueOf(classSchedule.getStartTime()));
            st.setTimestamp(5, Timestamp.valueOf(classSchedule.getEndTime()));
            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    classSchedule.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing class schedule in the database.
     * @param classSchedule the {@link ClassSchedule} object to update
     */
    @Override
    public void update(ClassSchedule classSchedule) {
        conn = MariaDbConnection.getConnection();
        String sql = "UPDATE class_schedule SET location = ?, description = ?, start_time = ?, end_time = ? WHERE class_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, classSchedule.getLocation());
            st.setString(2, classSchedule.getDescription());
            st.setTimestamp(3, Timestamp.valueOf(classSchedule.getStartTime()));
            st.setTimestamp(4, Timestamp.valueOf(classSchedule.getEndTime()));
            st.setInt(5, classSchedule.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a class schedule from the database by its ID.
     * @param id the ID of the class schedule to delete
     */
    @Override
    public void delete(int id) {
        conn = MariaDbConnection.getConnection();
        String sql = "DELETE FROM class_schedule WHERE class_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}