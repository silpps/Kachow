package dao;

import config.MariaDbConnection;
import models.Assignment;

import java.sql.*;

public class AssignmentDAO implements IDAO<Assignment> {
    private Connection conn = null;

    // Get an Assignment from the database
    @Override
    public Assignment get(int id) {
        conn = MariaDbConnection.getConnection();
        String sql = "SELECT * FROM assignment WHERE assignment_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new Assignment(
                            rs.getInt("course_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getTimestamp("deadline").toLocalDateTime(),
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add a new Assignment to the database
    @Override
    public void add(Assignment assignment) {
        conn = MariaDbConnection.getConnection();
        String sql = "INSERT INTO assignment (course_id, title, description, deadline, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, assignment.getCourseId());
            st.setString(2, assignment.getTitle());
            st.setString(3, assignment.getDescription());
            st.setTimestamp(4, Timestamp.valueOf(assignment.getDeadline()));
            st.setString(5, assignment.getStatus());
            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    assignment.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update an Assignment in the database
    @Override
    public void update(Assignment assignment) {
        conn = MariaDbConnection.getConnection();
        String sql = "UPDATE assignment SET course_id = ?, title = ?, description = ?, deadline = ?, status = ? WHERE assignment_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, assignment.getCourseId());
            st.setString(2, assignment.getTitle());
            st.setString(3, assignment.getDescription());
            st.setTimestamp(4, Timestamp.valueOf(assignment.getDeadline()));
            st.setString(5, assignment.getStatus());
            st.setInt(6, assignment.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete an Assignment from the database
    @Override
    public void delete(int id) {
        conn = MariaDbConnection.getConnection();
        String sql = "DELETE FROM assignment WHERE assignment_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}