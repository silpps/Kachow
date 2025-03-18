package models;
import java.time.LocalDateTime;

//TODO: Pitäiskö tähän kanssa lisätä jonkinlainen description? se vois vaik sisältää tietyn tunnin aiheet tms
public class ClassSchedule {
    private final int courseId;
    private String  location;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int id;

    public ClassSchedule(int courseId, String location,String description , LocalDateTime startTime, LocalDateTime endTime) {
        this.courseId = courseId;

        this.location = location;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getCourseId() {
        return courseId;
    }


    public String getLocation() {
        return location;
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

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }



    public String getDetails() {
        return "Course: " + courseId + "\nLocation: " + location + "\nDescription: " + description + "\nStart time: " + startTime + "\nEnd time: " + endTime;
    }

    public void setCourseName(String newTitle) {

    }

}
