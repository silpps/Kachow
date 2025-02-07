package models;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudySession {
    private final String courseName;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int id;


    public StudySession(String courseName, String title, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.courseName = courseName;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        //tarviskohan lisätä päivä?
        this.date = date;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDetails() {
        return  "Title: " + title + "\nCourse: " + courseName + "\nDescription: " + description + "\nStart Time: " + startTime + "\nEnd Time: " + endTime;
    }
}
