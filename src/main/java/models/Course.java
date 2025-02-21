package models;

import java.time.LocalDate;


//TODO: Mieti miten tätä luokkaa vois hyödyntää kalenterinäkymässä. esim filtteri joka näyttää vain tietyn kurssin tapahtumat
public class Course {
    private final String courseName;
    private String instructor;
    private LocalDate startDate;
    private LocalDate endDate;

    public Course(String courseName, String instructor, LocalDate startDate, LocalDate endDate) {
        this.courseName = courseName;
        this.instructor = instructor;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getInstructor() {
        return instructor;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDetails() {
        return "Course: " + courseName;
    }
}
