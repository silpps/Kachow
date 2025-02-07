package models;

import java.util.ArrayList;
import java.util.List;

public class CourseService {
    private static CourseService instance;
    private List<String> courses = new ArrayList<>();

    private CourseService() {}

    public static CourseService getInstance() {
        if (instance == null) {
            instance = new CourseService();
        }
        return instance;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void addCourse(String courseName) {
        courses.add(courseName);
    }

}
