package dao;

import config.MariaDbConnection;
import models.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO implements IDAO<Course> {
    private static Connection conn = null;

    @Override
    public List<Course> getAll() {
        conn = MariaDbConnection.getConnection();
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                courses.add(new Course(
                        rs.getString("course_name"),
                        rs.getString("instructor"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }


    public Course get(String id) {
        conn = MariaDbConnection.getConnection();
        String sql = "SELECT * FROM course WHERE course_name = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Course(
                        rs.getString("course_name"),
                        rs.getString("instructor"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void add(Course course) {
        String sql = "INSERT INTO courses (course_name, instructor, start_date, end_date) VALUES (?, ?, ?)";
        try (Connection conn = MariaDbConnection.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, course.getCourseName());
            st.setString(2, course.getInstructor());
            st.setDate(2, Date.valueOf(course.getStartDate()));
            st.setDate(3, Date.valueOf(course.getEndDate()));
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Course course) {
        conn = MariaDbConnection.getConnection();
        String sql = "UPDATE course SET (course_name, instructor, start_date, end_date) VALUES (?, ?, ?, ?) WHERE id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, course.getCourseName());
            st.setString(2, course.getInstructor());
            st.setDate(3, Date.valueOf(course.getStartDate()));
            st.setDate(4, Date.valueOf(course.getEndDate()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void delete(String id) {
        String sql = "DELETE FROM course WHERE course_name = ?";
        try (Connection conn = MariaDbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
