package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ClassScheduleTest {

    private ClassSchedule classSchedule;

    @BeforeEach
    void setUp() {
        LocalDateTime startTime = LocalDateTime.of(2025, 2, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 2, 20, 12, 0);
        classSchedule = new ClassSchedule("Maths", "Monday", "Building A", startTime, endTime);
    }

    @Test
    void getCourseName() {
        assertEquals("Maths", classSchedule.getCourseName());

    }

    @Test
    void getDayOfWeek() {
        assertEquals("Monday", classSchedule.getDayOfWeek());
    }

    @Test
    void getLocation() {
        assertEquals("Building A", classSchedule.getLocation());
    }

    @Test
    void getStartTime() {
        LocalDateTime startTime = LocalDateTime.of(2025, 2, 20, 10, 0);
        assertEquals(startTime, classSchedule.getStartTime());
    }

    @Test
    void getEndTime() {
        LocalDateTime endTime = LocalDateTime.of(2025, 2, 20, 12, 0);
        assertEquals(endTime, classSchedule.getEndTime());
    }

    @Test
    void getId() {
        assertEquals(0, classSchedule.getId());
    }

    @Test
    void setId() {
        classSchedule.setId(123);
        assertEquals(123, classSchedule.getId());
    }

}