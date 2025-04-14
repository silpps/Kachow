package dao;

import config.MariaDbConnection;
import models.Assignment;

import java.sql.*;

/**
 * Data Access Object (DAO) class for managing {@link Assignment} objects in the database.
 * Implements {@link IDAO} interface for CRUD (Create, Read, Update, Delete) operations.
 */
public class AssignmentDAO implements IDAO<Assignment> {
    private Connection conn = null;


    /**
     * Retrieves an Assignment from the database by its ID.
     *
     * @param id the ID of the assignment to retrieve
     * @return the {@link Assignment} object with the specified ID, or null if not found
     */
    @Override
    public Assignment get(int id) {
        conn = MariaDbConnection.getConnection();
        String sql = "SELECT assignment_id, course_id, title, description, deadline, status FROM assignment WHERE assignment_id = ?";
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

    /**
     * Adds a new assignment to the database.
     *
     * @param assignment the {@link Assignment} object to add
     */
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

    /**
     * Updates an existing assignment in the database.
     *
     * @param assignment the {@link Assignment} object with updated details
     */
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

    /**
     * Deletes an assignment from the database by its ID.
     *
     * @param id the ID of the assignment to delete
     */
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