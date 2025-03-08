package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ExamTest {

    private Exam exam;

    @BeforeEach
    void setUp() {
        LocalDateTime examDate = LocalDateTime.of(2025, 2, 20, 10, 0);
        exam = new Exam("TestCourseName", examDate, "Test Exam", "Test Description", "Test Location");
    }

    @Test
    void getCourseName() {
        assertEquals("TestCourseName", exam.getCourseName());

    }

    @Test
    void getExamDate() {
        LocalDateTime examDate = LocalDateTime.of(2025, 2, 20, 10, 0);
        assertEquals(examDate, exam.getExamDate());
    }

    @Test
    void getTitle() {
        assertEquals("Test Exam", exam.getTitle());
    }

    @Test
    void getDescription() {
        assertEquals("Test Description", exam.getDescription());
    }

    @Test
    void getLocation() {
        assertEquals("Test Location", exam.getLocation());
    }

    @Test
    void getId() {
        assertEquals(0, exam.getId());
    }

    @Test
    void setId() {
        exam.setId(123);
        assertEquals(123, exam.getId());
    }

    @Test
    void setExamDate() {
        LocalDateTime examDate = LocalDateTime.of(2025, 2, 20, 10, 0);
        exam.setExamDate(examDate);
        assertEquals(examDate, exam.getExamDate());
    }

    @Test
    void setTitle() {
        exam.setTitle("New Test Exam");
        assertEquals("New Test Exam", exam.getTitle());
    }

    @Test
    void setDescription() {
        exam.setDescription("New Test Description");
        assertEquals("New Test Description", exam.getDescription());
    }

    @Test
    void setLocation() {
        exam.setLocation("New Test Location");
        assertEquals("New Test Location", exam.getLocation());
    }

    @Test
    void getDetails() {
        assertEquals("Title: Test Exam\nCourse: TestCourseName\nExam date: 2025-02-20T10:00\nDescription: Test Description\nLocation: Test Location", exam.getDetails());
    }

}