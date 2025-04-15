package dao;

import config.MariaDbConnection;
import models.StudySession;

import java.sql.*;

/**
 * Data Access Object (DAO) class for managing {@link StudySession} objects in the database.
 * Implements {@link IDAO} interface for CRUD (Create, Read, Update, Delete) operations.
 */
public class StudySessionDAO implements IDAO<StudySession> {
    private Connection conn = null;

    /**
     * Retrieves a StudySession from the database by its ID.
     *
     * @param id the ID of the study session to retrieve
     * @return the {@link StudySession} object with the specified ID, or null if not found
     */
    @Override
    public StudySession get(int id) {
        conn = MariaDbConnection.getConnection();
        String sql = "SELECT session_id, course_id, title, description, start_time, end_time FROM study_session WHERE session_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new StudySession(
                            rs.getInt("course_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Adds a new study session to the database.
     *
     * @param session the {@link StudySession} object to add
     */
    @Override
    public void add(StudySession session) {
        conn = MariaDbConnection.getConnection();
        String sql = "INSERT INTO study_session (course_id, title, description, start_time, end_time) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, session.getCourseId());
            st.setString(2, session.getTitle());
            st.setString(3, session.getDescription());
            st.setTimestamp(4, Timestamp.valueOf(session.getStartTime()));
            st.setTimestamp(5, Timestamp.valueOf(session.getEndTime()));
            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    session.setId(rs.getInt(1));
                }
            }
            System.out.println("Added new study session with id: " + session.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing study session in the database.
     *
     * @param studySession the {@link StudySession} object to update
     */
    @Override
    public void update(StudySession studySession) {
        conn = MariaDbConnection.getConnection();
        String sql = "UPDATE study_session SET course_id = ?, title = ?, description = ?, start_time = ?, end_time = ? WHERE session_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, studySession.getCourseId());
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

    /**
     * Deletes a study session from the database by its ID.
     *
     * @param id the ID of the study session to delete
     */
    @Override
    public void delete(int id) {
        conn = MariaDbConnection.getConnection();
        String sql = "DELETE FROM study_session WHERE session_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}