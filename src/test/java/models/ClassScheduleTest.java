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
        classSchedule = new ClassSchedule("Maths", "Building A", "Math class", startTime, endTime);
    }

    @Test
    void getCourseName() {
        assertEquals("Maths", classSchedule.getCourseName());

    }


    @Test
    void getLocation() {
        assertEquals("Building A", classSchedule.getLocation());
    }

    @Test
    void getDescription() {
        assertEquals("Math class", classSchedule.getDescription());
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

    @Test
    void setLocation() {
        classSchedule.setLocation("Building B");
        assertEquals("Building B", classSchedule.getLocation());
    }

    @Test
    void setDescription() {
        classSchedule.setDescription("Math class for beginners");
        assertEquals("Math class for beginners", classSchedule.getDescription());
    }

    @Test
    void setStartTime() {
        LocalDateTime startTime = LocalDateTime.of(2025, 2, 20, 9, 0);
        classSchedule.setStartTime(startTime);
        assertEquals(startTime, classSchedule.getStartTime());
    }

    @Test
    void setEndTime() {
        LocalDateTime endTime = LocalDateTime.of(2025, 2, 20, 11, 0);
        classSchedule.setEndTime(endTime);
        assertEquals(endTime, classSchedule.getEndTime());
    }

    @Test
    void getDetails(){
        String details = "Course: Maths\nLocation: Building A\nDescription: Math class\nStart time: 2025-02-20T10:00\nEnd time: 2025-02-20T12:00";
        assertEquals(details, classSchedule.getDetails());
    }

}