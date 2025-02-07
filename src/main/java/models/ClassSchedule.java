package models;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClassSchedule {
    private final String courseName;
    private String daysOfWeek;
    private String  location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int id;

    public ClassSchedule(String courseName, String daysOfWeek, String location, LocalDateTime startTime, LocalDateTime endTime) {
        this.courseName = courseName;
        this.daysOfWeek = daysOfWeek;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public String getLocation() {
        return location;
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

    public String getDetails() {
        return "Course: " + courseName + "\nDays of the Week: " + daysOfWeek + "\nLocation: " + location + "\nStart Time: " + startTime + "\nEnd Time: " + endTime;
    }
}
