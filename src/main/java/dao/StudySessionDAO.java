package dao;

import config.MariaDbConnection;
import models.StudySession;

import java.sql.*;

public class StudySessionDAO implements IDAO<StudySession> {
    private Connection conn = null;

    // Get a StudySession from the database
    @Override
    public StudySession get(String id) {
        conn = MariaDbConnection.getConnection();
        String sql = "SELECT * FROM study_session WHERE id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,  Integer.parseInt(id));
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new StudySession(
                        rs.getString("course_name"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add a new StudySession to the database
    @Override
    public void add(StudySession session) {
        conn = MariaDbConnection.getConnection();
        String sql = "INSERT INTO study_session (course_name, title, description, start_time, end_time) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, session.getCourseName());
            st.setString(2, session.getTitle());
            st.setString(3, session.getDescription());
            st.setTimestamp(4, Timestamp.valueOf(session.getStartTime()));
            st.setTimestamp(5, Timestamp.valueOf(session.getEndTime()));
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                session.setId(rs.getInt(1));
            }
            System.out.println("Added new study session with id: " + session.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update a StudySession in the database
    @Override
    public void update(StudySession studySession) {
        conn = MariaDbConnection.getConnection();
        String sql = "UPDATE study_session SET course_name = ?, title = ?, description = ?, start_time = ?, end_time = ? WHERE session_id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, studySession.getCourseName());
            st.setString(2, studySession.getTitle());
            st.setString(3, studySession.getDescription());
            st.setTimestamp(4, Timestamp.valueOf(studySession.getStartTime()));
            st.setTimestamp(5, Timestamp.valueOf(studySession.getEndTime()));
            st.setInt(6, studySession.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a StudySession from the database
    @Override
    public void delete(String id) {
        conn = MariaDbConnection.getConnection();
        String sql = "DELETE FROM study_session WHERE session_id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, Integer.parseInt(id));
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
