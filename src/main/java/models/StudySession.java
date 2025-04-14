package models;

import java.time.LocalDateTime;

/**
 * This class represents a study session for a specific course.
 * It extends the {@link MyEvent} class and contains details about the session such as title, description, start time, and end time.
 */
public class StudySession extends MyEvent {
    private final int courseId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int id;

    /**
     * Constructor to create a new StudySession object.
     *
     * @param courseId    the ID of the course associated with this study session
     * @param title       the title of the study session
     * @param description the description of the study session
     * @param startTime   the start time of the study session
     * @param endTime     the end time of the study session
     */
    public StudySession(int courseId, String title, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Returns the ID of the course associated with this study session.
     *
     * @return the course ID
     */
    public int getCourseId() {
        return courseId;
    }

    /**
     * Returns the title of the study session.
     *
     * @return the title
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the study session.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the description of the study session.
     *
     * @return the description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the study session.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the date of the study session, which corresponds to its start time.
     *
     * @return the start time as the date
     */
    @Override
    public LocalDateTime getDate() {
        return getStartTime();
    }

    /**
     * Returns the start time of the study session.
     *
     * @return the start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the study session.
     *
     * @param startTime the start time to set
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Returns the end time of the study session.
     *
     * @return the end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the study session.
     *
     * @param endTime the end time to set
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Returns the unique ID of the study session.
     *
     * @return the session ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of the study session.
     *
     * @param id the session ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns a string containing the details of the study session.
     *
     * @return a string representation of the study session details
     */
    public String getDetails() {
        return "Title: " + title + "\nCourse: " + courseId + "\nDescription: " + description + "\nStart Time: " + startTime + "\nEnd Time: " + endTime;
    }
}