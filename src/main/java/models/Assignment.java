package models;
import java.time.LocalDateTime;

//TODO: Lisää deadline aika
public class Assignment {
    private final int courseId;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private String status;
    private int id;

    public Assignment(int courseId, String title, String description, LocalDateTime deadline, String status) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetails() {
        return "Title: " + title + "\nCourse: " + courseId + "\nDescription: " + description + "\nDeadline: " + deadline + "\nStatus: " + status;
    }
}
