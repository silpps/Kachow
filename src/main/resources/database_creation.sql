DROP DATABASE IF EXISTS study_planner;
CREATE DATABASE study_planner;
USE study_planner;


CREATE TABLE course
(
    course_name VARCHAR(50) PRIMARY KEY,
    instructor  VARCHAR(50) NOT NULL ,
    start_date  DATE NOT NULL ,
    end_date    DATE NOT NULL ,
);

CREATE TABLE study_session
(
    session_id  INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(50) NOT NULL,
    title       VARCHAR(50) NOT NULL,
    description VARCHAR(100),
    start_time  DATETIME NOT NULL,
    end_time    DATETIME NOT NULL,
    FOREIGN KEY (course_name) REFERENCES course (course_name)
);

CREATE TABLE assignment
(
    assignment_id INT AUTO_INCREMENT PRIMARY KEY,
    course_name   VARCHAR(50) NOT NULL,
    title         VARCHAR(50) NOT NULL,
    description   VARCHAR(100),
    deadline      DATETIME NOT NULL,
    status        VARCHAR(20) NOT NULL,
    FOREIGN KEY (course_name) REFERENCES course (course_name)
);

CREATE TABLE exam
(
    exam_id     INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(50) NOT NULL,
    exam_date   DATETIME NOT NULL,
    title       VARCHAR(50) NOT NULL,
    description VARCHAR(100),
    location    VARCHAR(15) NOT NULL,
    FOREIGN KEY (course_name) REFERENCES course (course_name)
);

CREATE TABLE class_schedule
(
    class_id    INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(50) NOT NULL,
    location    VARCHAR(15) NOT NULL,
    description VARCHAR(100),
    start_time  DATETIME NOT NULL,
    end_time    DATETIME NOT NULL,
    FOREIGN KEY (course_name) REFERENCES course (course_name)
);

DROP USER IF EXISTS 'student_test'@'localhost';
CREATE USER 'student_test'@'localhost' IDENTIFIED BY 'schedule';
GRANT SELECT, INSERT, DELETE, UPDATE ON study_planner.* TO 'student_test'@'localhost';

