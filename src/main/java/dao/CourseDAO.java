package dao;

import models.Course;

import java.util.List;

public class CourseDAO implements IDAO<Course> {

    @Override
    public List<Course> getAll() {
        return List.of();
    }

    @Override
    public Course get(int id) {
        return null;
    }

    @Override
    public void add(Course course) {
        String sql = "INSERT INTO course (course_name, start_date, end_date) VALUES (?, ?, ?)";
    }

    @Override
    public void update(Course course) {

    }

    @Override
    public void delete(Course course) {

    }

}
