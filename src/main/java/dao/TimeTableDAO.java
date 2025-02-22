package dao;

import config.MariaDbConnection;
import models.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TimeTableDAO {
    Connection conn = null;

    public List<String> getCourseNames() {
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
                Course course = new Course(
                        rs.getString("course_name"),
                        rs.getString("instructor"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate()
                );
                courses.add(course);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public List<ClassSchedule> getClassSchedule(LocalDate startDate, LocalDate endDate) {
        conn = MariaDbConnection.getConnection();
        List<ClassSchedule> classSchedules = new ArrayList<>();
        String sql = "SELECT * FROM class_schedule WHERE start_time >= ? AND end_time<= ? ORDER BY start_time ASC";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setTimestamp(1, Timestamp.valueOf(startDate.atStartOfDay()));
            st.setTimestamp(2, Timestamp.valueOf(endDate.atTime(23, 59, 59)));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ClassSchedule classSchedule = new ClassSchedule(
                        rs.getString("course_name"),
                        rs.getString("location"),
                        rs.getString("description"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime()
                );
                classSchedule.setId(rs.getInt("class_id"));
                classSchedules.add(classSchedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classSchedules;
    }

    public List<Assignment> getAssignmentSchedule(LocalDate startDate, LocalDate endDate) {
        conn = MariaDbConnection.getConnection();
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT * FROM assignment WHERE deadline >= ? AND deadline<= ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setTimestamp(1, Timestamp.valueOf(startDate.atStartOfDay()));
            st.setTimestamp(2, Timestamp.valueOf(endDate.atTime(23, 59, 59)));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Assignment assignment = new Assignment(
                        rs.getString("course_name"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getTimestamp("deadline").toLocalDateTime(),
                        rs.getString("status")
                );
                assignment.setId(rs.getInt("assignment_id"));
                assignments.add(assignment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignments;
    }

    public List<StudySession> getStudySessionSchedule(LocalDate startDate, LocalDate endDate) {
        conn = MariaDbConnection.getConnection();
        List<StudySession> studySessions = new ArrayList<>();
        String sql = "SELECT * FROM study_session WHERE start_time >= ? AND end_time<= ? ORDER BY start_time ASC";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setTimestamp(1, Timestamp.valueOf(startDate.atStartOfDay()));
            st.setTimestamp(2, Timestamp.valueOf(endDate.atTime(23, 59, 59)));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                StudySession studySession = new StudySession(
                        rs.getString("course_name"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime()
                );
                studySession.setId(rs.getInt("session_id"));
                studySessions.add(studySession);
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
                Exam exam = new Exam(
                        rs.getString("course_name"),
                        rs.getTimestamp("exam_date").toLocalDateTime(),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location")
                );
                exam.setId(rs.getInt("exam_id"));
                exams.add(exam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }


}
