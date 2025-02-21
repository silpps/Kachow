package models;
import java.time.LocalDate;
import java.time.LocalDateTime;

//TODO: Lisää deadline aika
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

    public void setCourseName(String courseName) {
        this.title = courseName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetails() {
        return "Title: " + title + "\nCourse: " + courseName + "\nDescription: " + description + "\nDue Date: " + dueDate + "\nStatus: " + status;
    }
}
