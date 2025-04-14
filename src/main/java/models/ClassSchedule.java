package models;

import java.time.LocalDateTime;

/**
 * Represents a class schedule for a specific course.
 * This class extends the {@link MyEvent} class and provides details
 * about the class schedule, including its location, description, start time, and end time.
 */
public class ClassSchedule extends MyEvent {
    private final int courseId;
    private String location;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int id;

    /**
     * Constructs a new {@code ClassSchedule} with the specified details.
     *
     * @param courseId    the ID of the course associated with this class schedule
     * @param location    the location of the class
     * @param description the description of the class
     * @param startTime   the start time of the class
     * @param endTime     the end time of the class
     */
    public ClassSchedule(int courseId, String location, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.courseId = courseId;
        this.location = location;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Returns the ID of the course associated with this class schedule.
     *
     * @return the course ID
     */
    public int getCourseId() {
        return courseId;
    }

    /**
     * Returns the title of the class schedule.
     * Currently, this method returns an empty string.
     *
     * @return the title of the class schedule
     */
    @Override
    public String getTitle() {
        return "";
    }

    /**
     * Returns the location of the class.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the class.
     *
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns the description of the class.
     *
     * @return the description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the class.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the start time of the class.
     *
     * @return the start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the class.
     *
     * @param startTime the start time to set
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Returns the end time of the class.
     *
     * @return the end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the class.
     *
     * @param endTime the end time to set
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Returns the unique ID of the class schedule.
     *
     * @return the schedule ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of the class schedule.
     *
     * @param id the schedule ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the date of the class, which corresponds to its start time.
     *
     * @return the start time as the date
     */
    @Override
    public LocalDateTime getDate() {
        return getStartTime();
    }

    /**
     * Returns a string containing the details of the class schedule.
     *
     * @return a string representation of the class schedule details
     */
    public String getDetails() {
        return "Course: " + courseId + "\nLocation: " + location + "\nDescription: " + description + "\nStart time: " + startTime + "\nEnd time: " + endTime;
    }
}
