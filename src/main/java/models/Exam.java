package models;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Exam {
    private final String courseName;
    private LocalDateTime examDate;
    private String title;
    private String description;
    private String location;
    private int id;

    public Exam(String courseName, LocalDateTime examDate, String title, String description, String location) {
        this.courseName = courseName;
        this.examDate = examDate;
        this.title = title;
        this.description = description;
        this.location = location;
    }

    public String getCourseName() {
        return courseName;
    }

    public LocalDateTime getExamDate() {
        return examDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetails() {
        return "Title: " + title + "\nCourse: " + courseName + "\nExam date: " + examDate + "\nDescription: " + description + "\nLocation: " + location;
    }
}
