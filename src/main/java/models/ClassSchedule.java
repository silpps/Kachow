package models;
import java.time.LocalDate;
import java.time.LocalDateTime;

//TODO: Pitäiskö tähän kanssa lisätä jonkinlainen description? se vois vaik sisältää tietyn tunnin aiheet tms
public class ClassSchedule {
    private final String courseName;
    private String dayOfWeek;
    private String  location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int id;

    public ClassSchedule(String courseName, String dayOfWeek, String location, LocalDateTime startTime, LocalDateTime endTime) {
        this.courseName = courseName;
        this.dayOfWeek = dayOfWeek;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
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

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }



    public String getDetails() {
        return "Course: " + courseName + "\nDays of the Week: " + dayOfWeek + "\nLocation: " + location + "\nStart Time: " + startTime + "\nEnd Time: " + endTime;
    }

    public void setCourseName(String newTitle) {

    }
}
