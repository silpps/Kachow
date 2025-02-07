package dao;

import config.MariaDbConnection;
import models.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TimeTableDAO {
    Connection conn = null;

    public List<String> getCourse() {
        conn = MariaDbConnection.getConnection();
        List<String> courseNames = new ArrayList<>();
        String sql = "SELECT DISTINCT course_name FROM course";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                courseNames.add(rs.getString("course_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseNames;
    }

    public List<Course> getCourseSchedule(LocalDate startDate, LocalDate endDate) {
        conn = MariaDbConnection.getConnection();
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course WHERE start_date >= ? AND end_date<= ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setDate(1, Date.valueOf(startDate));
            st.setDate(2, Date.valueOf(endDate));

            ResultSet rs = st.executeQuery();
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

    public List<ClassSchedule> getClassSchedule(LocalDate startDate, LocalDate endDate) {
        conn = MariaDbConnection.getConnection();
        List<ClassSchedule> classSchedules = new ArrayList<>();
        String sql = "SELECT * FROM class_schedule WHERE start_time >= ? AND end_time<= ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setTimestamp(1, Timestamp.valueOf(startDate.atStartOfDay()));
            st.setTimestamp(2, Timestamp.valueOf(endDate.atTime(23, 59, 59)));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                classSchedules.add(new ClassSchedule(
                        rs.getString("course_name"),
                        rs.getString("title"),
                        rs.getString("location"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classSchedules;
    }

    public List<Assignment> getAssignmentSchedule(LocalDate startDate, LocalDate endDate) {
        conn = MariaDbConnection.getConnection();
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT * FROM assignment WHERE due_date >= ? AND due_date<= ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setTimestamp(1, Timestamp.valueOf(startDate.atStartOfDay()));
            st.setTimestamp(2, Timestamp.valueOf(endDate.atTime(23, 59, 59)));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                assignments.add(new Assignment(
                        rs.getString("course_name"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getTimestamp("due_date").toLocalDateTime(),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignments;
    }

    public List<StudySession> getStudySessionSchedule(LocalDate startDate, LocalDate endDate) {
        conn = MariaDbConnection.getConnection();
        List<StudySession> studySessions = new ArrayList<>();
        String sql = "SELECT * FROM study_session WHERE start_time >= ? AND end_time<= ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setTimestamp(1, Timestamp.valueOf(startDate.atStartOfDay()));
            st.setTimestamp(2, Timestamp.valueOf(endDate.atTime(23, 59, 59)));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                studySessions.add(new StudySession(
                        rs.getString("course_name"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime(),
                        rs.getDate("date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studySessions;
    }

    public List<Exam> getExamSchedule(LocalDate startDate, LocalDate endDate) {
        conn = MariaDbConnection.getConnection();
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT * FROM exam WHERE exam_date >= ? AND exam_date <= ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setTimestamp(1, Timestamp.valueOf(startDate.atStartOfDay()));
            st.setTimestamp(2, Timestamp.valueOf(endDate.atTime(23, 59, 59)));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                exams.add(new Exam(
                        rs.getString("course_name"),
                        rs.getTimestamp("exam_date").toLocalDateTime(),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }


}
