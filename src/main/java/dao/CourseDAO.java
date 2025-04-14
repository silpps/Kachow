package dao;

import config.MariaDbConnection;
import models.Course;

import java.sql.*;

public class CourseDAO implements IDAO<Course> {
    private Connection conn = null;

    // Get a Course from the database
    @Override
    public Course get(int id) {
        conn = MariaDbConnection.getConnection();
        String sql = "SELECT * FROM course WHERE course_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Course course = new Course(
                            rs.getString("course_name"),
                            rs.getString("instructor"),
                            rs.getDate("start_date").toLocalDate(),
                            rs.getDate("end_date").toLocalDate()
                    );
                    course.setCourseID(rs.getInt("course_id"));
                    return course;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add a new Course to the database
    @Override
    public void add(Course course) {
        String sql = "INSERT INTO course (course_name, instructor, start_date, end_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = MariaDbConnection.getConnection();
             PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, course.getCourseName());
            st.setString(2, course.getInstructor());
            st.setDate(3, Date.valueOf(course.getStartDate()));
            st.setDate(4, Date.valueOf(course.getEndDate()));
            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    course.setCourseID(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update a Course in the database
    @Override
    public void update(Course course) {
        conn = MariaDbConnection.getConnection();
        String sql = "UPDATE course SET course_name = ?, instructor = ?, start_date = ?, end_date = ? WHERE course_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, course.getCourseName());
            st.setString(2, course.getInstructor());
            st.setDate(3, Date.valueOf(course.getStartDate()));
            st.setDate(4, Date.valueOf(course.getEndDate()));
            st.setInt(5, course.getCourseID());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a Course from the database
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM course WHERE course_id = ?";
        try (Connection conn = MariaDbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}