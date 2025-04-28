package models;
import java.time.LocalDateTime;

/**
 * This class represents an assignment for a specific course.
 * It extends the {@link MyEvent} class and contains details about the assignment such as title, description, deadline, and status.
 */
public class Assignment extends MyEvent {
    private final int courseId;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private String status;
    private int id;

    /**
     * Constructor to create a new Assignment object.
     *
     * @param courseId    the ID of the course associated with this assignment
     * @param title       the title of the assignment
     * @param description the description of the assignment
     * @param deadline    the deadline for the assignment
     * @param status      the status of the assignment (e.g., "completed", "pending")
     */
    public Assignment(int courseId, String title, String description, LocalDateTime deadline, String status) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
    }

    /**
     * Returns the ID of the course associated with this assignment.
     *
     * @return the course ID
     */
    public int getCourseId() {
        return courseId;
    }

    /**
     * Returns the title of the assignment.
     *
     * @return the title
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Returns the description of the assignment.
     *
     * @return the description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Returns the date of the deadline.
     *
     * @return the deadline
     */
    @Override
    public LocalDateTime getDate() {
        return getDeadline();
    }

    /**
     * Returns the deadline of the assignment.
     *
     * @return the deadline
     */
    public LocalDateTime getDeadline() {
        return deadline;
    }

    @Override
    public LocalDateTime getEndTime() {
        return getDeadline();
    }

    /**
     * Returns the status of the assignment.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns the unique ID of the assignment.
     *
     * @return the assignment ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of the assignment.
     *
     * @param id the assignment ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the title of the assignment.
     *
     * @param title the assignment title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the description of the assignment.
     *
     * @param description the assignment description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the deadline of the assignment.
     *
     * @param deadline the assignment deadline
     */
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    /**
     * Sets the status of the assignment.
     *
     * @param status the assignment status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns a string representation of the assignment details.
     *
     * @return a string containing the assignment details
     */
    public String getDetails() {
        return "Title: " + title + "\nCourse: " + courseId + "\nDescription: " + description + "\nDeadline: " + deadline + "\nStatus: " + status;
    }
}
