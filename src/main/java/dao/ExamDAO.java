package dao;

import config.MariaDbConnection;
import models.Exam;
import java.sql.*;


public class ExamDAO implements IDAO<Exam> {
    private Connection conn = null;


    // Get an Exam from the database
    @Override
    public Exam get(int id) {
        conn = MariaDbConnection.getConnection();
        String sql = "SELECT * FROM exam WHERE id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,  id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Exam(
                        rs.getInt("course_Id"),
                        rs.getTimestamp("exam_date").toLocalDateTime(),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add a new Exam to the database
    @Override
    public void add(Exam exam) {
        conn = MariaDbConnection.getConnection();
        String sql = "INSERT INTO exam (course_id, exam_date, title, description, location) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, exam.getCourseId());
            st.setTimestamp(2, Timestamp.valueOf(exam.getExamDate()));
            st.setString(3, exam.getTitle());
            st.setString(4, exam.getDescription());
            st.setString(5, exam.getLocation());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                exam.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update an Exam in the database
    @Override
    public void update(Exam exam) {
        conn = MariaDbConnection.getConnection();
        String sql = "UPDATE exam SET course_id = ?, exam_date = ?, title = ?, description = ?, location = ? WHERE exam_id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
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

    // Delete an Exam from the database
    @Override
    public void delete(int id) {
        conn = MariaDbConnection.getConnection();
        String sql = "DELETE FROM exam WHERE exam_id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
