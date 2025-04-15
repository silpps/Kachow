package dao;

import config.MariaDbConnection;
import models.Exam;
import java.sql.*;

/**
 * Data Access Object (DAO) class for managing {@link Exam} objects in the database.
 * Implements {@link IDAO} interface for CRUD (Create, Read, Update, Delete) operations.
 */
public class ExamDAO implements IDAO<Exam> {
    private Connection conn = null;

    /**
     * Retrieves an Exam from the database by its ID.
     *
     * @param id the ID of the exam to retrieve
     * @return the {@link Exam} object with the specified ID, or null if not found
     */
    @Override
    public Exam get(int id) {
        conn = MariaDbConnection.getConnection();
        String sql = "SELECT exam_id, course_id, exam_date, title, description, location FROM exam WHERE exam_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new Exam(
                            rs.getInt("course_id"),
                            rs.getTimestamp("exam_date").toLocalDateTime(),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("location")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Adds a new exam to the database.
     * @param exam the {@link Exam} object to add
     */
    @Override
    public void add(Exam exam) {
        conn = MariaDbConnection.getConnection();
        String sql = "INSERT INTO exam (course_id, exam_date, title, description, location) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, exam.getCourseId());
            st.setTimestamp(2, Timestamp.valueOf(exam.getExamDate()));
            st.setString(3, exam.getTitle());
            st.setString(4, exam.getDescription());
            st.setString(5, exam.getLocation());
            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    exam.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing exam in the database.
     * @param exam the {@link Exam} object to update
     */
    @Override
    public void update(Exam exam) {
        conn = MariaDbConnection.getConnection();
        String sql = "UPDATE exam SET course_id = ?, exam_date = ?, title = ?, description = ?, location = ? WHERE exam_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, exam.getCourseId());
            st.setTimestamp(2, Timestamp.valueOf(exam.getExamDate()));
            st.setString(3, exam.getTitle());
            st.setString(4, exam.getDescription());
            st.setString(5, exam.getLocation());
            st.setInt(6, exam.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes an exam from the database by its ID.
     * @param id the ID of the exam to delete
     */
    @Override
    public void delete(int id) {
        conn = MariaDbConnection.getConnection();
        String sql = "DELETE FROM exam WHERE exam_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}