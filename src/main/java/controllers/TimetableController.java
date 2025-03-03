package controllers;

import dao.TimeTableDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.*;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

//TODO: Järjestele ja siisti koodia
public class TimetableController implements Initializable {

    @FXML
    private VBox mondayColumn, tuesdayColumn, wednesdayColumn, thursdayColumn, fridayColumn, saturdayColumn, sundayColumn;

    @FXML
    private Label mondayDate, tuesdayDate, wednesdayDate, thursdayDate, fridayDate, saturdayDate, sundayDate, currentWeekLabel;

    @FXML
    private Button nextWeekButton, previousWeekButton;

    private TimeTableDAO timeTableDAO;
    private LocalDate startOfWeek;
    private LocalDate endOfWeek;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeTableDAO = new TimeTableDAO();
        LocalDate today = LocalDate.now();
        this.startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        this.endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);

        fetchAndDisplayCurrentWeeksData();
    }

    public void fetchAndDisplayCurrentWeeksData() {
        clearTimetable();


        // Format to dd/MM
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        // Set the dates to the labels
        mondayDate.setText(startOfWeek.format(formatter));
        tuesdayDate.setText(startOfWeek.plusDays(1).format(formatter));
        wednesdayDate.setText(startOfWeek.plusDays(2).format(formatter));
        thursdayDate.setText(startOfWeek.plusDays(3).format(formatter));
        fridayDate.setText(startOfWeek.plusDays(4).format(formatter));
        saturdayDate.setText(startOfWeek.plusDays(5).format(formatter));
        sundayDate.setText(startOfWeek.plusDays(6).format(formatter));

        //TODO: lisää vuosi?
        // Set the current week dates label
        currentWeekLabel.setText(startOfWeek.format(formatter) + " - " + endOfWeek.format(formatter));

        // Fetch current week's data from database
        List<ClassSchedule> classSchedules = timeTableDAO.getClassSchedule(startOfWeek, endOfWeek);
        List<Assignment> assignments = timeTableDAO.getAssignmentSchedule(startOfWeek, endOfWeek);
        List<StudySession> studySessions = timeTableDAO.getStudySessionSchedule(startOfWeek, endOfWeek);
        List<Exam> exams = timeTableDAO.getExamSchedule(startOfWeek, endOfWeek);

        // Combine all tasks into a single list
        List<Object> allTasks = new ArrayList<>();
        allTasks.addAll(classSchedules);
        allTasks.addAll(assignments);
        allTasks.addAll(studySessions);
        allTasks.addAll(exams);

        // Add tasks to the correct day's VBox
        addTasksToDay(allTasks);
    }

    @FXML
    private void showNextWeek() {
        this.startOfWeek = this.startOfWeek.plusDays(7);
        this.endOfWeek = this.endOfWeek.plusDays(7);
        fetchAndDisplayCurrentWeeksData();

    }

    @FXML
    private void showPreviousWeek() {
        this.startOfWeek = this.startOfWeek.minusDays(7);
        this.endOfWeek = this.endOfWeek.minusDays(7);
        fetchAndDisplayCurrentWeeksData();
    }

    private void clearTimetable() {
        mondayColumn.getChildren().clear();
        tuesdayColumn.getChildren().clear();
        wednesdayColumn.getChildren().clear();
        thursdayColumn.getChildren().clear();
        fridayColumn.getChildren().clear();
        saturdayColumn.getChildren().clear();
        sundayColumn.getChildren().clear();
    }

    private <T> void addTasksToDay(List<T> tasks) {
        tasks.sort(Comparator.comparing(task -> {
            return switch (task) {
                case ClassSchedule classSchedule -> classSchedule.getStartTime();
                case StudySession studySession -> studySession.getStartTime();
                case Exam exam -> exam.getExamDate();
                case Assignment assignment -> assignment.getDeadline();
                case null, default -> LocalDateTime.MAX;
            };
        }));

        for (T task : tasks) {
            VBox dayColumn = getDayColumn(getTaskDayOfWeek(task));
            VBox taskBox = createTaskBox(task);
            dayColumn.getChildren().add(taskBox);
        }
    }

    private VBox getDayColumn(int dayOfWeek) {
        return switch (dayOfWeek) {
            case 1 -> mondayColumn;
            case 2 -> tuesdayColumn;
            case 3 -> wednesdayColumn;
            case 4 -> thursdayColumn;
            case 5 -> fridayColumn;
            case 6 -> saturdayColumn;
            case 7 -> sundayColumn;
            default -> throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);
        };
    }


    private VBox createTaskBox(Object task) {
        return switch (task) {
            case ClassSchedule classSchedule -> createClassScheduleBox(classSchedule);
            case Assignment assignment -> createAssignmentBox(assignment);
            case StudySession studySession -> createStudySessionBox(studySession);
            case Exam exam -> createExamBox(exam);
            case null, default -> throw new IllegalArgumentException("Unknown task type");
        };
    }

    private VBox createClassScheduleBox(ClassSchedule classSchedule) {
        VBox taskBox = new VBox();
        taskBox.getStyleClass().add("class-schedule-box");

        Label courseLabel = new Label(classSchedule.getCourseName());
        Label taskLabel = new Label("CLASS: ");
        taskLabel.setStyle("-fx-font-weight: bold;");
        Label timeLabel = new Label(classSchedule.getStartTime().toLocalTime() + " - " + classSchedule.getEndTime().toLocalTime());
        Label locationLabel = new Label("Location: " + classSchedule.getLocation());

        taskBox.getChildren().addAll(courseLabel, taskLabel, timeLabel, locationLabel);
        return taskBox;
    }

    private VBox createAssignmentBox(Assignment assignment) {
        VBox taskBox = new VBox();
        taskBox.getStyleClass().add("assignment-box");

        Label courseLabel = new Label(assignment.getCourseName());
        Label taskLabel = new Label("ASSIGNMENT: ");
        taskLabel.setStyle("-fx-font-weight: bold;");
        Label timeLabel = new Label("Deadline: " + assignment.getDeadline().toLocalTime().toString());
        Label statusLabel = new Label("Status: " + (assignment.getStatus()));

        taskBox.getChildren().addAll(courseLabel, taskLabel, timeLabel, statusLabel);
        return taskBox;
    }

    private VBox createStudySessionBox(StudySession studySession) {
        VBox taskBox = new VBox();
        taskBox.getStyleClass().add("study-session-box");

        Label courseLabel = new Label(studySession.getCourseName());
        Label taskLabel = new Label("STUDY SESSION: ");
        taskLabel.setStyle("-fx-font-weight: bold;");
        Label timeLabel = new Label(studySession.getStartTime().toLocalTime() + " - " + studySession.getEndTime().toLocalTime());

        taskBox.getChildren().addAll(courseLabel, taskLabel, timeLabel);
        return taskBox;
    }

    private VBox createExamBox(Exam exam) {
        VBox taskBox = new VBox();
        taskBox.getStyleClass().add("exam-box");

        Label courseLabel = new Label(exam.getCourseName());
        Label taskLabel = new Label("EXAM: ");
        taskLabel.setStyle("-fx-font-weight: bold;");
        Label timeLabel = new Label(exam.getExamDate().toLocalTime().toString());
        Label locationLabel = new Label("Location: " + exam.getLocation());

        taskBox.getChildren().addAll(courseLabel, taskLabel, timeLabel, locationLabel);
        return taskBox;
    }


    //ei tällä hetkellä käytössä
    private double calculateHeight(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        long minutes = duration.toMinutes();
        return Math.max(30, minutes); // Ensure a minimum height of 30
    }

    private int getTaskDayOfWeek(Object task) {
        return switch (task) {
            case ClassSchedule classSchedule -> classSchedule.getStartTime().getDayOfWeek().getValue();
            case Assignment assignment -> assignment.getDeadline().getDayOfWeek().getValue();
            case StudySession studySession -> studySession.getStartTime().getDayOfWeek().getValue();
            case Exam exam -> exam.getExamDate().getDayOfWeek().getValue();
            case null, default -> throw new IllegalArgumentException("Unknown task type");
        };
    }

    @FXML
    private void addButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addTask.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/timetable.css");
            AddTaskController controller = loader.getController();
            controller.setTimetableController(this);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}