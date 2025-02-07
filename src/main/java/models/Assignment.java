package models;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Assignment {
    private final String courseName;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String status;
    private int id;

    public Assignment(String courseName, String title, String description, LocalDateTime dueDate, String status) {
        this.courseName = courseName;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetails() {
        return "Title: " + title + "\nCourse: " + courseName + "\nDescription: " + description + "\nDue Date: " + dueDate + "\nStatus: " + status;
    }
}
