package models;

import java.time.LocalDateTime;

/**
 * This class represents an exam for a specific course.
 * It extends the {@link MyEvent} class and contains details about the exam such as title, description, date, and location.
 */
public class Exam extends MyEvent {
    private final int courseId;
    private LocalDateTime examDate;
    private String title;
    private String description;
    private String location;
    private int id;

    /**
     * Constructor to create a new Exam object.
     *
     * @param courseId    the ID of the course associated with this exam
     * @param examDate    the date and time of the exam
     * @param title       the title of the exam
     * @param description the description of the exam
     * @param location    the location of the exam
     */
    public Exam(int courseId, LocalDateTime examDate, String title, String description, String location) {
        this.courseId = courseId;
        this.examDate = examDate;
        this.title = title;
        this.description = description;
        this.location = location;
    }

    /**
     * Returns the ID of the course associated with this exam.
     *
     * @return the course ID
     */
    public int getCourseId() {
        return courseId;
    }

    /**
     * Returns the date and time of the exam.
     *
     * @return the exam date and time
     */
    @Override
    public LocalDateTime getDate() {
        return getExamDate();
    }

    /**
     * Returns the date and time of the exam.
     *
     * @return the exam date and time
     */
    public LocalDateTime getExamDate() {
        return examDate;
    }

    /**
     * Sets the date and time of the exam.
     *
     * @param examDate the exam date and time
     */
    public void setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
    }

    /**
     * Returns the title of the exam.
     *
     * @return the title
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the exam.
     *
     * @param title the exam title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the description of the exam.
     *
     * @return the description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the exam.
     *
     * @param description the exam description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the location of the exam.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the exam.
     *
     * @param location the exam location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns the ID of the exam.
     *
     * @return the exam ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the exam.
     *
     * @param id the exam ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns a string representation of the exam details.
     *
     * @return a string containing the exam details
     */
    public String getDetails() {
        return "Title: " + title + "\nCourse: " + courseId + "\nExam date: " + examDate + "\nDescription: " + description + "\nLocation: " + location;
    }


}
