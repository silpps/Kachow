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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class TimetableController_v2 implements Initializable {

    @FXML
    private TableView<Timetable_v2> timetable;

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
        fillTimeSlots();

        // Fetch and display the current week's data
        fetchAndDisplayCurrentWeekData();
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

    private void fillTimeSlots() {
        ObservableList<Timetable_v2> timeSlots = FXCollections.observableArrayList();
        for (int i = 6; i < 25; i++) {
            String time = i + ":00";
            timeSlots.add(new Timetable_v2(time));
        }
        timetable.setItems(timeSlots);
    }

    private void fetchAndDisplayCurrentWeekData() {
        // Get the current week's start and end dates
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);

        // Fetch the current week's data
        List<ClassSchedule> classSchedules = timeTableDAO.getClassSchedule(startOfWeek, endOfWeek);
        List<Assignment> assignments = timeTableDAO.getAssignmentSchedule(startOfWeek, endOfWeek);
        List<StudySession> studySessions = timeTableDAO.getStudySessionSchedule(startOfWeek, endOfWeek);
        List<Exam> exams = timeTableDAO.getExamSchedule(startOfWeek, endOfWeek);

        // Map tasks to time slots
        Map<String, Timetable_v2> timeSlotMap = createTimeSlotMap();
        mapTasksToTimeSlots(timeSlotMap, classSchedules, assignments, studySessions, exams);

        // Update the timetable
        ObservableList<Timetable_v2> updatedTimeSlots = FXCollections.observableArrayList(timeSlotMap.values());
        timetable.setItems(updatedTimeSlots);
    }

    private Map<String, Timetable_v2> createTimeSlotMap() {
        Map<String, Timetable_v2> timeSlotMap = new HashMap<>();
        for (int i = 6; i < 25; i++) {
            String time = i + ":00";
            timeSlotMap.put(time, new Timetable_v2(time));
        }
        return timeSlotMap;
    }

    private void mapTasksToTimeSlots(Map<String, Timetable_v2> timeSlotMap, List<ClassSchedule> classSchedules, List<Assignment> assignments, List<StudySession> studySessions, List<Exam> exams) {
        // Map class schedules
        for (ClassSchedule schedule : classSchedules) {
            String time = schedule.getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            String day = schedule.getStartTime().getDayOfWeek().toString();
            String task = schedule.getCourseName();
            updateTimeSlot(timeSlotMap, time, day, task);
        }

        // Map assignments
        for (Assignment assignment : assignments) {
            String time = assignment.getDueDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            String day = assignment.getDueDate().getDayOfWeek().toString();
            String task = assignment.getTitle() + " (" + assignment.getCourseName() + ")";
            updateTimeSlot(timeSlotMap, time, day, task);
        }

        // Map study sessions
        for (StudySession session : studySessions) {
            String time = session.getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            String day = session.getStartTime().getDayOfWeek().toString();
            String task = session.getTitle() + " (" + session.getCourseName() + ")";
            updateTimeSlot(timeSlotMap, time, day, task);
        }

        // Map exams
        for (Exam exam : exams) {
            String time = exam.getExamDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            String day = exam.getExamDate().getDayOfWeek().toString();
            String task = exam.getTitle() + " (" + exam.getCourseName() + ")";
            updateTimeSlot(timeSlotMap, time, day, task);
        }
    }

    private void updateTimeSlot(Map<String, Timetable_v2> timeSlotMap, String time, String day, String task) {
        Timetable_v2 timeSlot = timeSlotMap.get(time);
        if (timeSlot != null) {
            switch (day) {
                case "MONDAY":
                    timeSlot.setMonday(task);
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
        }
    }

    @FXML
    private void addButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addTask.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}