package models;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class StudySession {
    private final String courseName;
    private String title;
    private String description;
    //Voiskohan nää vaihtaa LocalTimeks ja päivä erikseen LocalDate?
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;


    public StudySession(String courseName, String title, String description, LocalDate date, LocalTime fromTime, LocalTime toTime) {
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDetails() {
        return  "Title: " + title + "\nCourse: " + courseName + "\nDescription: " + description + "\nStart Time: " + startTime + "\nEnd Time: " + endTime;
    }
}
