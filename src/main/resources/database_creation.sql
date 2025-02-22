DROP DATABASE IF EXISTS study_planner;
CREATE DATABASE study_planner;
USE study_planner;


CREATE TABLE course
(
    course_name VARCHAR(50) PRIMARY KEY,
    instructor  VARCHAR(50),
    start_date  DATE,
    end_date    DATE
);

CREATE TABLE study_session
(
    session_id  INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(50),
    title       VARCHAR(50),
    description VARCHAR(100),
    start_time  DATETIME,
    end_time    DATETIME,
    FOREIGN KEY (course_name) REFERENCES course (course_name)
);

CREATE TABLE assignment
(
    assignment_id INT AUTO_INCREMENT PRIMARY KEY,
    course_name   VARCHAR(50),
    title         VARCHAR(50),
    description   VARCHAR(100),
    deadline      DATETIME,
    status        VARCHAR(20),
    FOREIGN KEY (course_name) REFERENCES course (course_name)
);

CREATE TABLE exam
(
    exam_id     INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(50),
    exam_date   DATETIME,
    title       VARCHAR(50),
    description VARCHAR(100),
    location    VARCHAR(15),
    FOREIGN KEY (course_name) REFERENCES course (course_name)
);

CREATE TABLE class_schedule
(
    class_id    INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(50),
    location    VARCHAR(15),
    description VARCHAR(100),
    start_time  DATETIME,
    end_time    DATETIME,
    day_of_week VARCHAR(10),
    FOREIGN KEY (course_name) REFERENCES course (course_name)
);

DROP USER IF EXISTS 'student_test'@'localhost';
CREATE USER 'student_test'@'localhost' IDENTIFIED BY 'schedule';
GRANT SELECT, INSERT, DELETE, UPDATE ON study_planner.* TO 'student_test'@'localhost';

