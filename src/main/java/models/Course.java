package models;

import java.time.LocalDate;

public class Course {
    private final String courseName;
    private LocalDate startDate;
    private LocalDate endDate;

    public Course(String courseName, LocalDate startDate, LocalDate endDate) {
        this.courseName = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDetails() {
        return "Course: " + courseName;
    }
}
