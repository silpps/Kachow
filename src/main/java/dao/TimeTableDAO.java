package dao;

import config.MariaDbConnection;
import models.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object (DAO) class for managing timetable-related data in the database.
 * Provides methods to retrieve course names, class schedules, assignments, study sessions, and exams.
 */
public class TimeTableDAO {
    Connection conn = null;

    /**
     * Retrieves a map of course IDs and names from the database.
     *
     * @return a map where the key is the course ID and the value is the course name
     */
    public Map<Integer, String> getCourses() {
        conn = MariaDbConnection.getConnection();
        Map<Integer, String> courses = new HashMap<>();
        String sql = "SELECT course_id, course_name FROM course";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                courses.put(rs.getInt("course_id"), rs.getString("course_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    /**
     * Retrieves a list of courses from the database within the specified date range.
     *
     * @param startDate the start date of the course
     * @param endDate   the end date of the course
     * @return a list of {@link Course} objects within the specified date range
     */
    public List<Course> getCourses(LocalDate startDate, LocalDate endDate) {
        conn = MariaDbConnection.getConnection();
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT course_id, course_name, instructor, start_date, end_date FROM course WHERE start_date >= ? AND end_date <= ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setDate(1, Date.valueOf(startDate));
            st.setDate(2, Date.valueOf(endDate));
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course(
                            rs.getString("course_name"),
                            rs.getString("instructor"),
                            rs.getDate("start_date").toLocalDate(),
                            rs.getDate("end_date").toLocalDate()
                    );
                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    /**
     * Retrieves a list of class schedules from the database within the specified date range.
     *
     * @param startDate the start date of the class schedule
     * @param endDate   the end date of the class schedule
     * @return a list of {@link ClassSchedule} objects within the specified date range
     */
    public List<ClassSchedule> getClassSchedule(LocalDate startDate, LocalDate endDate) {
        conn = MariaDbConnection.getConnection();
        List<ClassSchedule> classSchedules = new ArrayList<>();
        String sql = "SELECT class_id, course_id, location, description, start_time, end_time FROM class_schedule WHERE start_time >= ? AND end_time <= ? ORDER BY start_time ASC";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setTimestamp(1, Timestamp.valueOf(startDate.atStartOfDay()));
            st.setTimestamp(2, Timestamp.valueOf(endDate.atTime(23, 59, 59)));
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    ClassSchedule classSchedule = new ClassSchedule(
                            rs.getInt("course_id"),
                            rs.getString("location"),
                            rs.getString("description"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time").toLocalDateTime()
                    );
                    classSchedule.setId(rs.getInt("class_id"));
                    classSchedules.add(classSchedule);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classSchedules;
    }

    /**
     * Retrieves a list of assignments from the database within the specified date range.
     *
     * @param startDate the start date of the assignment
     * @param endDate   the end date of the assignment
     * @return a list of {@link Assignment} objects within the specified date range
     */
    public List<Assignment> getAssignmentSchedule(LocalDate startDate, LocalDate endDate) {
        conn = MariaDbConnection.getConnection();
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT assignment_id, course_id, title, description, deadline, status FROM assignment WHERE deadline >= ? AND deadline <= ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setTimestamp(1, Timestamp.valueOf(startDate.atStartOfDay()));
            st.setTimestamp(2, Timestamp.valueOf(endDate.atTime(23, 59, 59)));
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Assignment assignment = new Assignment(
                            rs.getInt("course_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getTimestamp("deadline").toLocalDateTime(),
                            rs.getString("status")
                    );
                    assignment.setId(rs.getInt("assignment_id"));
                    assignments.add(assignment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignments;
    }

    /**
     * Retrieves a list of study sessions from the database within the specified date range.
     *
     * @param startDate the start date of the study session
     * @param endDate   the end date of the study session
     * @return a list of {@link StudySession} objects within the specified date range
     */
    public List<StudySession> getStudySessionSchedule(LocalDate startDate, LocalDate endDate) {
        conn = MariaDbConnection.getConnection();
        List<StudySession> studySessions = new ArrayList<>();
        String sql = "SELECT session_id, course_id, title, description, start_time, end_time FROM study_session WHERE start_time >= ? AND end_time <= ? ORDER BY start_time ASC";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setTimestamp(1, Timestamp.valueOf(startDate.atStartOfDay()));
            st.setTimestamp(2, Timestamp.valueOf(endDate.atTime(23, 59, 59)));
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    StudySession studySession = new StudySession(
                            rs.getInt("course_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time").toLocalDateTime()
                    );
                    studySession.setId(rs.getInt("session_id"));
                    studySessions.add(studySession);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studySessions;
    }

    /**
     * Retrieves a list of exams from the database within the specified date range.
     *
     * @param startDate the start date of the exam
     * @param endDate   the end date of the exam
     * @return a list of {@link Exam} objects within the specified date range
     */
    public List<Exam> getExamSchedule(LocalDate startDate, LocalDate endDate) {
        conn = MariaDbConnection.getConnection();
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT exam_id, course_id, exam_date, title, description, location FROM exam WHERE exam_date >= ? AND exam_date <= ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setTimestamp(1, Timestamp.valueOf(startDate.atStartOfDay()));
            st.setTimestamp(2, Timestamp.valueOf(endDate.atTime(23, 59, 59)));
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Exam exam = new Exam(
                            rs.getInt("course_id"),
                            rs.getTimestamp("exam_date").toLocalDateTime(),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("location")
                    );
                    exam.setId(rs.getInt("exam_id"));
                    exams.add(exam);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }
}