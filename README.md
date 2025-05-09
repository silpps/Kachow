# Introduction of the project:

This project is a study planner for students of all ages and academic backgrounds. This study planner can help students manage their busy schedule and study more efficiently.

## Features:

This project includes a personal timetable where the user can input their own events. User can input their class schedule and the events can be integrated to the existing course.

Features include:
- User can add, update, and delete events (exams, assignments, class schedules, study sessions)
- Manage courses and associate them with events
- View weekly calendar with interactive navigation
- Multilingual interface (English, Korean, Arabic)
- Persistent local database storage
- Visual confirmation dialogs and tooltips
- Help button with usage instructions
- Supports right-to-left layout for Arabic

## Technologies Used

- Java (JDK 21)
- JavaFX and FXML for GUI
- MySQL for local database
- Maven for build and dependency management
- JUnit for unit testing
- JaCoCo for code coverage
- Jenkins for CI/CD automation
- SonarQube and PMD for static code analysis
- JMeter for performance testing

## ER Diagram

The application is built around a central `Course` entity, which connects to several event types including `Assignment`, `Exam`, `ClassSchedule`, and `StudySession`. Each of these has a one-to-many relationship with a course and includes relevant metadata such as title, time, description, and location.

<img src="doc/UML%20and%20ER%20Diagrams/ER%20Diagram.png" alt="ER Diagram" style="max-width:50%;"/>
<img src="doc/UML%20and%20ER%20Diagrams/Relational%20Schema.png" alt="Relational Schema" style="max-width:50%;"/>


## Implementation Details
  
The application follows the MVC pattern:
- **Models** define data structures like Course and Event subclasses.
- **Controllers** manage user input and application logic.
- **View** handles the GUI using JavaFX and FXML.
- **DAO (Data Access Objects)** handle communication with the database.
- **Config** package provides the MariaDB connection logic.

Events and courses are stored in a MySQL database. Changes are reflected in the GUI through a JavaFX-based interface. The application is structured in packages for maintainability and clarity.

## Installation Guide

**Prerequisites:**
- Java JDK 21
- Git
- Apache Maven
- MySQL or HeidiSQL
- An IDE like IntelliJ IDEA or VS Code (with Maven plugin)

**Steps:**
1. Clone the project: git clone https://github.com/silpps/Kachow.git
2. Import the project in your IDE.
3. Copy the database schema from src/resources from a file named 'database_schema.sql' and run it on HeidiSQL or MariaDB.
4. Run the command 'mvn clean install' in the project terminal.

## Usage Instructions

- **Add Event or Course:** Click "Add", select the event type, enter details, and click "Save". The calendar will update the view.
- **Update/Delete Event or Course:** Click on an item, modify or delete, and confirm the action. The calendar will update the view.
- **Change Language:** Use the language toggle buttons (EN, KR, AR). The selection is saved and applied on the next launch.
- **Help:** Click the question mark button for in-app guidance.

## Troubleshooting

- If there is a problem with JDK installation, such as "Java is not recognized as an internal or external command," ensure that Java is properly added to the system’s `PATH` environment variable. For example, it should be something like:
C:\Program Files\Java\jdk-21\bin
- If there is a problem with Git installation, such as “Git is not recognized as a command”, ensure that Git is properly added to system’s `PATH` environment variable. The path should look something like:
C:\Program Files\Git\cmd
- If there is a problem with Apache Maven installation, such as "Maven is not recognized as an internal or external command", ensure that Maven is properly added to system’s `PATH` environment variable. The path should look something like:
C:\apache-maven-3.8.6\bin
- If there’s a problem while running the application, please restart the application from IDE.


## Link to Trello

- [Trello Board](https://trello.com/w/workspace51787268)
