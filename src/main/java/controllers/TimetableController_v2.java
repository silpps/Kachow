package controllers;

import dao.TimeTableDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class TimetableController_v2 implements Initializable {

    @FXML
    public TableView<Timetable_v2> timetable;

    @FXML
    private TableColumn<Timetable_v2, String> timeColumn;

    @FXML
    private TableColumn<Timetable_v2, String> mondayColumn;

    @FXML
    private TableColumn<Timetable_v2, String> tuesdayColumn;

    @FXML
    private TableColumn<Timetable_v2, String> wednesdayColumn;

    @FXML
    private TableColumn<Timetable_v2, String> thursdayColumn;

    @FXML
    private TableColumn<Timetable_v2, String> fridayColumn;

    @FXML
    private TableColumn<Timetable_v2, String> saturdayColumn;

    @FXML
    private TableColumn<Timetable_v2, String> sundayColumn;

    @FXML
    private Button addButton;

    private TimeTableDAO timeTableDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize DAO
        timeTableDAO = new TimeTableDAO();

        // Set up TableView columns
        setupTableViewColumns();

        // Fill the timetable with time slots
        ObservableList<Timetable_v2> timeSlots = createTimeSlots();
        timetable.setItems(timeSlots);

        // Fetch and display the current week's data
        fetchAndDisplayCurrentWeekData(timeSlots);
        timetable.refresh();

    }

    private void setupTableViewColumns() {
        // Bind columns to the TimetableTime properties
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        mondayColumn.setCellValueFactory(new PropertyValueFactory<>("monday"));
        tuesdayColumn.setCellValueFactory(new PropertyValueFactory<>("tuesday"));
        wednesdayColumn.setCellValueFactory(new PropertyValueFactory<>("wednesday"));
        thursdayColumn.setCellValueFactory(new PropertyValueFactory<>("thursday"));
        fridayColumn.setCellValueFactory(new PropertyValueFactory<>("friday"));
        saturdayColumn.setCellValueFactory(new PropertyValueFactory<>("saturday"));
        sundayColumn.setCellValueFactory(new PropertyValueFactory<>("sunday"));
    }

    private ObservableList<Timetable_v2> createTimeSlots() {
        ObservableList<Timetable_v2> timeSlots = FXCollections.observableArrayList();
        for (int i = 6; i < 25; i++) {
            String time = i + ":00";
            timeSlots.add(new Timetable_v2(time));
        }
        return timeSlots;
    }

    public void fetchAndDisplayCurrentWeekData(ObservableList<Timetable_v2> timeSlots) {
        // Get the current week's start and end dates
        LocalDate today = LocalDate.now();
        System.out.println("Today: " + today);
        LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);

        // Fetch the current week's data
        List<ClassSchedule> classSchedules = timeTableDAO.getClassSchedule(startOfWeek, endOfWeek);
        List<Assignment> assignments = timeTableDAO.getAssignmentSchedule(startOfWeek, endOfWeek);
        List<StudySession> studySessions = timeTableDAO.getStudySessionSchedule(startOfWeek, endOfWeek);
        List<Exam> exams = timeTableDAO.getExamSchedule(startOfWeek, endOfWeek);

        System.out.println("Fetched Class Schedules: " + classSchedules.size());
        System.out.println("Fetched Assignments: " + assignments.size());
        System.out.println("Fetched Study Sessions: " + studySessions.size());
        System.out.println("Fetched Exams: " + exams.size());

        // Map tasks to time slots
        mapTasksToTimeSlots(timeSlots, classSchedules, assignments, studySessions, exams);

        // Update the timetable
        timetable.setItems(timeSlots);
        timetable.refresh();
    }

    private void mapTasksToTimeSlots(ObservableList<Timetable_v2> timeSlots, List<ClassSchedule> classSchedules, List<Assignment> assignments, List<StudySession> studySessions, List<Exam> exams) {
        // Clear existing tasks
        for (Timetable_v2 timeSlot : timeSlots) {
            timeSlot.setMonday("");
            timeSlot.setTuesday("");
            timeSlot.setWednesday("");
            timeSlot.setThursday("");
            timeSlot.setFriday("");
            timeSlot.setSaturday("");
            timeSlot.setSunday("");
        }

        // Map class schedules
        for (ClassSchedule schedule : classSchedules) {
            String time = schedule.getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            String day = schedule.getStartTime().getDayOfWeek().toString();
            String task = schedule.getCourseName();
            updateTimeSlot(timeSlots, time, day, task);
        }

        // Map assignments
        for (Assignment assignment : assignments) {
            System.out.println("Assignment: " + assignment.getTitle());
            String time = String.format("%02d:00", assignment.getDeadline().getHour());
            if (time.equals("00:00")) {
                time = "9:00";
            }
            String day = assignment.getDeadline().getDayOfWeek().toString();
            String task = "ASSIGNMENT: " + assignment.getTitle() + " (" + assignment.getCourseName() + ")";
            updateTimeSlot(timeSlots, time, day, task);
            System.out.println("Time: " + time + " Day: " + day + " Task: " + task);
        }

        // Map study sessions
        for (StudySession session : studySessions) {
            String time = session.getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            String day = session.getStartTime().getDayOfWeek().toString();
            String task = "STUDY SESSION: " + session.getTitle() + " (" + session.getCourseName() + ")";
            updateTimeSlot(timeSlots, time, day, task);
        }

        // Map exams
        for (Exam exam : exams) {
            String time = exam.getExamDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            if (time.equals("00:00")) {
                time = "9:00";
            }
            String day = exam.getExamDate().getDayOfWeek().toString();
            String task = "EXAM: " + exam.getTitle() + " (" + exam.getCourseName() + ")";
            updateTimeSlot(timeSlots, time, day, task);
        }
    }

    private void updateTimeSlot(ObservableList<Timetable_v2> timeSlots, String time, String day, String task) {
        System.out.println("Updating time slot: " + time + " on " + day + " with task: " + task);
        for (Timetable_v2 timeSlot : timeSlots) {
            System.out.println("Checking if " + timeSlot.getTime() + " equals " + time);
            if (timeSlot.getTime().equals(time)) {
                switch (day) {
                    case "MONDAY":
                        timeSlot.setMonday(task);
                        System.out.println("Monday: " + task);
                        break;
                    case "TUESDAY":
                        timeSlot.setTuesday(task);
                        break;
                    case "WEDNESDAY":
                        timeSlot.setWednesday(task);
                        break;
                    case "THURSDAY":
                        timeSlot.setThursday(task);
                        break;
                    case "FRIDAY":
                        timeSlot.setFriday(task);
                        break;
                    case "SATURDAY":
                        timeSlot.setSaturday(task);
                        break;
                    case "SUNDAY":
                        timeSlot.setSunday(task);
                        break;
                }
                break;
            }
        }
    }

    @FXML
    private void addButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addTask.fxml"));
            Scene scene = new Scene(loader.load());
            AddTaskController controller = loader.getController();
           //controller.setTimetableController(this);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}