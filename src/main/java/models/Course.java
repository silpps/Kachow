package models;

import java.time.LocalDate;


/**
 * This class represents a course with details such as course ID, name, instructor, start date, and end date.
 * It provides methods to get and set these details.
 */
public class Course {
    private final String courseName;
    private int courseID;
    private String instructor;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Constructor to create a new Course object.
     *
     * @param courseName the name of the course
     * @param instructor the instructor of the course
     * @param startDate  the start date of the course
     * @param endDate    the end date of the course
     */
    public Course(String courseName, String instructor, LocalDate startDate, LocalDate endDate) {
        this.courseName = courseName;
        this.instructor = instructor;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Returns the unique ID of the course.
     *
     * @return the course ID
     */
    public int getCourseID() {
        return courseID;
    }

    /**
     * Sets the unique ID of the course.
     *
     * @param courseID the course ID to set
     */
    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    /**
     * Returns the name of the course.
     *
     * @return the course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Returns the name of the instructor for the course.
     *
     * @return the instructor's name
     */
    public String getInstructor() {
        return instructor;
    }

    /**
     * Sets the name of the instructor for the course.
     *
     * @param instructor the instructor's name to set
     */
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    /**
     * Returns the start date of the course.
     *
     * @return the start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the course.
     *
     * @param startDate the start date to set
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the end date of the course.
     *
     * @return the end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the course.
     *
     * @param endDate the end date to set
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns a string containing the details of the course.
     *
     * @return a string representation of the course details
     */
    public String getDetails() {
        return "Course: " + courseName;
    }
}
