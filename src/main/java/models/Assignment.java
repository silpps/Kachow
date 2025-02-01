package models;
import java.time.LocalDate;

public class Assignment {
    private final String courseName;
    private String title;
    private String description;
    private LocalDate dueDate;
    private String status;

    public Assignment(String courseName, String title, String description, LocalDate dueDate, String status) {
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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public String getDetails() {
        return "Title: " + title + "\nCourse: " + courseName + "\nDescription: " + description + "\nDue Date: " + dueDate + "\nStatus: " + status;
    }
}
