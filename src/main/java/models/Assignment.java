package models;
import java.time.LocalDateTime;

//TODO: Lisää deadline aika
public class Assignment extends MyEvent {
    private String courseName;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private String status;
    private int id;
//
    public Assignment(String courseName, String title, String description, LocalDateTime deadline, String status) {
        this.courseName = courseName;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
    }

    public String getCourseName() {
        return courseName;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public LocalDateTime getDate() {
        return getDeadline();
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

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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
        return "Title: " + title + "\nCourse: " + courseName + "\nDescription: " + description + "\nDeadline: " + deadline + "\nStatus: " + status;
    }
}
