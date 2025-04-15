package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to manage the list of courses.
 */
public class CourseService {
    private static CourseService instance;
    private final List<String> courses = new ArrayList<>();

    private CourseService() {}

    /**
     * @return the singleton instance of CourseService
     */
    public static CourseService getInstance() {
        if (instance == null) {
            instance = new CourseService();
        }
        return instance;
    }

    /**
     * @return the list of courses
     */
    public List<String> getCourses() {
        return courses;
    }

    /**
     * Adds a new course to the list of courses.
     *
     * @param courseName the name of the course to add
     */
    public void addCourse(String courseName) {
        courses.add(courseName);
    }

}
