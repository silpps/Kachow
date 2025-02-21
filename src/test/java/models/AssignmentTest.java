package models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentTest {

    private Assignment assignment;

    @BeforeEach
     void setUp() {
        LocalDateTime dueDate = LocalDateTime.of(2025, 2, 15, 0, 0);
        assignment = new Assignment("Maths", "Matrix", "Do the matrix assignment", dueDate, "Not Started");
    }

    @Test
    void getCourseName() {
        assertEquals("Maths", assignment.getCourseName());
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
    void getDueDate() {
        LocalDateTime dueDate = LocalDateTime.of(2025, 2, 15, 0, 0);
        assertEquals(dueDate, assignment.getDueDate());
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
}