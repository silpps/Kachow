IF EXISTS DROP DATABASE study_planner;
CREATE DATABASE study_planner;
USE study_planner;


CREATE TABLE course (
                        course_name VARCHAR(100) PRIMARY KEY,
                        start_date DATE,
                        end_date DATE
);

CREATE TABLE study_sessions (
                                session_id INT AUTO_INCREMENT PRIMARY KEY,
                                course_name VARCHAR(100),
                                title VARCHAR(100),
                                description TEXT,
                                start_time DATETIME,
                                end_time DATETIME,
                                FOREIGN KEY (course_name) REFERENCES course(course_name)
);

CREATE TABLE assignments (
                             assignment_id INT AUTO_INCREMENT PRIMARY KEY,
                             course_name VARCHAR(100),
                             title VARCHAR(100),
                             description TEXT,
                             due_date DATETIME,
                             status VARCHAR(20),
                             FOREIGN KEY (course_name) REFERENCES course(course_name)
);

CREATE TABLE exams (
                       exam_id INT AUTO_INCREMENT PRIMARY KEY,
                       course_name VARCHAR(100),
                       exam_date DATETIME,
                       description TEXT,
                       FOREIGN KEY (course_name) REFERENCES course(course_name)
);

CREATE TABLE class_schedule (
                                class_id INT AUTO_INCREMENT PRIMARY KEY,
                                course_name VARCHAR(100),
                                location VARCHAR(15),
                                start_time DATETIME,
                                end_time DATETIME,
                                day_of_week ENUM('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'),
                                FOREIGN KEY (course_name) REFERENCES course(course_name)
);

IF EXISTS DROP USER 'student_test'@'localhost';
CREATE USER 'student_test'@'localhost' IDENTIFIED BY 'schedule';
GRANT SELECT, INSERT, DELETE, UPDATE ON study_planner.* TO 'student_test'@'localhost';

