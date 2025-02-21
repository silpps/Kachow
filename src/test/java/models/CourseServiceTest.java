package models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseServiceTest {

    private CourseService courseService;

    @BeforeEach
    void setUp() {
        courseService = CourseService.getInstance();
        courseService.getCourses().clear();
    }

    @Test
    void getInstance() {
        CourseService instance = CourseService.getInstance();
        assertNotNull(instance);
        CourseService instance2 = CourseService.getInstance();
        assertEquals(instance, instance2);
    }

    @Test
    void getCourses() {
        List<String> courses = courseService.getCourses();
        courses.add("Math");
        courses.add("Java");
        assertEquals(courses.size(), 2);

    }

    @Test
    void addCourse() {
        List<String> courses = courseService.getCourses();
        courseService.addCourse("Math");
        courseService.addCourse("Java");
        assertTrue(courses.contains("Math"));
        assertTrue(courses.contains("Java"));
    }
}