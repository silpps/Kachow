package models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudySession {
    private final String courseName;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;


    public StudySession(String courseName, String title, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.courseName = courseName;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public String getDetails() {
        return  "Title: " + title + "\nCourse: " + courseName + "\nDescription: " + description + "\nStart Time: " + startTime + "\nEnd Time: " + endTime;
    }
}
