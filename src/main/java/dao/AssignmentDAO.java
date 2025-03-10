package dao;

import config.MariaDbConnection;
import models.Assignment;

import java.sql.*;

public class AssignmentDAO implements IDAO<Assignment> {
    private Connection conn = null;

    // Get an Assignment from the database
    @Override
    public Assignment get(String id) {
        conn = MariaDbConnection.getConnection();
        String sql = "SELECT * FROM assignment WHERE assignment_id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,  Integer.parseInt(id));
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Assignment(
                        rs.getString("course_name"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getTimestamp("deadline").toLocalDateTime(),
                        rs.getString("status")
                );
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
        String sql = "INSERT INTO assignment (course_name, title, description, deadline, status) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, assignment.getCourseName());
            st.setString(2, assignment.getTitle());
            st.setString(3, assignment.getDescription());
            st.setTimestamp(4, Timestamp.valueOf(assignment.getDeadline()));
            st.setString(5, assignment.getStatus());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                assignment.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update an Assignment in the database
    @Override
    public void update(Assignment assignment) {
        conn = MariaDbConnection.getConnection();
        String sql = "UPDATE assignment SET course_name = ?, title = ?, description = ?, deadline = ?, status = ? WHERE assignment_id = ?";

        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, assignment.getCourseName());
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
    public void delete(String id) {
        conn = MariaDbConnection.getConnection();
        String sql = "DELETE FROM assignment WHERE assignment_id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, Integer.parseInt(id));
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}