package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StudySessionTest {

    private StudySession studySession;

    @BeforeEach
    void setUp() {
        LocalDateTime startTime = LocalDateTime.of(2025, 3, 15, 12,0);
        LocalDateTime endTime = LocalDateTime.of(2025, 3, 15, 14,0);
        studySession = new StudySession("Math", "Matrix", "Study for the test", startTime, endTime);
    }

    @Test
    void getCourseName() {
        assertEquals("Math", studySession.getCourseName());
    }

    @Test
    void getTitle() {
        assertEquals("Matrix", studySession.getTitle());
    }

    @Test
    void getDescription() {
        assertEquals("Study for the test", studySession.getDescription());
    }

    @Test
    void getStartTime() {
        LocalDateTime startTime = LocalDateTime.of(2025, 3, 15, 12,0);
        assertEquals( startTime, studySession.getStartTime());
    }

    @Test
    void getEndTime() {
        LocalDateTime endTime = LocalDateTime.of(2025, 3, 15, 14,0);
        assertEquals( endTime, studySession.getEndTime());
    }

    @Test
    void getId() {
        assertEquals(0, studySession.getId());
    }

    @Test
    void setId() {
        studySession.setId(123);
        assertEquals(123, studySession.getId());
    }

}