package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentTest {

    private Assignment assignment;

    @BeforeEach
     void setUp() {
        LocalDateTime deadline = LocalDateTime.of(2025, 2, 15, 0, 0);
        assignment = new Assignment(1, "Matrix", "Do the matrix assignment", deadline, "Not Started");
    }

    @Test
    void getCourseName() {
        assertEquals(1, assignment.getCourseId());
    }

    @Test
    void getTitle() {
        assertEquals("Matrix", assignment.getTitle());
    }

    @Test
    void getDescription() {
        assertEquals("Do the matrix assignment", assignment.getDescription());
    }

    @Test
    void getDeadline() {
        LocalDateTime deadline = LocalDateTime.of(2025, 2, 15, 0, 0);
        assertEquals(deadline, assignment.getDeadline());
    }

    @Test
    void getStatus() {
        assertEquals("Not Started", assignment.getStatus());
    }

    @Test
    void getId() {
        assertEquals(0, assignment.getId());
    }

    @Test
    void setId() {
        assignment.setId(123);
        assertEquals(123, assignment.getId());
    }

    @Test
    void setTitle() {
        assignment.setTitle("Arrays");
        assertEquals("Arrays", assignment.getTitle());
    }

    @Test
    void setDescription() {
        assignment.setDescription("Do the arrays assignment");
        assertEquals("Do the arrays assignment", assignment.getDescription());
    }

    @Test
    void setDeadline() {
        LocalDateTime deadline = LocalDateTime.of(2025, 2, 15, 0, 0);
        assignment.setDeadline(deadline);
        assertEquals(deadline, assignment.getDeadline());
    }

    @Test
    void setStatus() {
        assignment.setStatus("Ongoing");
        assertEquals("Ongoing", assignment.getStatus());
    }

    @Test
    void getDetails(){
        assertEquals("Title: Matrix\nCourse: 1\nDescription: Do the matrix assignment\nDeadline: 2025-02-15T00:00\nStatus: Not Started", assignment.getDetails());
    }
}