DROP DATABASE IF EXISTS study_planner;
CREATE DATABASE study_planner
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;
USE study_planner;

CREATE TABLE course
(
    course_id   INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    instructor  VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    start_date  DATE NOT NULL,
    end_date    DATE NOT NULL
);

CREATE TABLE study_session
(
    session_id  INT AUTO_INCREMENT PRIMARY KEY,
    course_id   INT NOT NULL,
    title       VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    description VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    start_time  DATETIME NOT NULL,
    end_time    DATETIME NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course (course_id) ON DELETE CASCADE
);

CREATE TABLE assignment
(
    assignment_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id     INT NOT NULL,
    title         VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    description   VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    deadline      DATETIME NOT NULL,
    status        VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course (course_id) ON DELETE CASCADE
);

CREATE TABLE exam
(
    exam_id     INT AUTO_INCREMENT PRIMARY KEY,
    course_id   INT NOT NULL,
    exam_date   DATETIME NOT NULL,
    title       VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    description VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    location    VARCHAR(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    FOREIGN KEY (course_id) REFERENCES course (course_id) ON DELETE CASCADE
);

CREATE TABLE class_schedule
(
    class_id    INT AUTO_INCREMENT PRIMARY KEY,
    course_id   INT NOT NULL,
    location    VARCHAR(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    description VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    start_time  DATETIME NOT NULL,
    end_time    DATETIME NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course (course_id) ON DELETE CASCADE
);

DROP USER IF EXISTS 'student_test'@'localhost';
CREATE USER 'student_test'@'localhost' IDENTIFIED BY 'schedule';
GRANT SELECT, INSERT, DELETE, UPDATE ON study_planner.* TO 'student_test'@'localhost';
