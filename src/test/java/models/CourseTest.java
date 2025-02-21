package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {
    private Course course;

    @BeforeEach
    void setup() {
        LocalDateTime startDate = LocalDateTime.of(2025, 2, 10, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 4, 10, 10, 0);
        course = new Course("Java", "John Doe", startDate.toLocalDate(), endDate.toLocalDate());
    }

    @Test
    void getCourseName() {
        assertEquals("Java", course.getCourseName());
    }

    @Test
    void getInstructor() {
        assertEquals("John Doe", course.getInstructor());
    }

    @Test
    void getStartDate() {
        assertEquals("2025-02-10", course.getStartDate().toString());
    }

    @Test
    void getEndDate() {
        assertEquals("2025-04-10", course.getEndDate().toString());
    }

}