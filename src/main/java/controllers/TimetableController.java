package controllers;

import dao.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;


/**
 * Controller class for the Timetable view.
 * This class handles the user interactions and logic for displaying and managing the timetable.
 */
public class TimetableController implements Initializable {

    @FXML
    private HBox weekBox;

    @FXML
    private VBox coursesColumn;

    @FXML
    private VBox mondayColumn, tuesdayColumn, wednesdayColumn, thursdayColumn, fridayColumn, saturdayColumn, sundayColumn;
    private VBox[] dayColumns = new VBox[7];

    @FXML
    private Label coursesLabel;
    @FXML
    private Button addButton;
    @FXML
    private Label mondayDate, tuesdayDate, wednesdayDate, thursdayDate, fridayDate, saturdayDate, sundayDate, currentWeekLabel, nameLabel, mondayLabel, tuesdayLabel, wednesdayLabel, thursdayLabel, fridayLabel, saturdayLabel, sundayLabel;
    private Label[] dateLabels;

    @FXML
    private Button nextWeekButton;
    @FXML
    private Button lastWeekButton;


    private TimeTableDAO timeTableDAO;
    private LocalDate startOfWeek;
    private LocalDate endOfWeek;
    private IDAO<StudySession> studySessionDAO;
    private IDAO<Exam> examDAO;
    private IDAO<Assignment> assignmentDAO;
    private IDAO<ClassSchedule> classScheduleDAO;
    private SettingDAO settingDAO;
    private IDAO<Course> courseDAO;
    private Map<Integer, String> courses;
    private Locale locale;
    private ResourceBundle bundle;
    private static final String bundleName = "messages";

    /**
     * Initializes the controller and sets up the timetable view.
     *
     * @param url the location used to resolve relative paths for the root object
     * @param resourceBundle the resource bundle for localization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the DAO classes
        settingDAO = new SettingDAO();
        timeTableDAO = new TimeTableDAO();
        studySessionDAO = new StudySessionDAO();
        examDAO = new ExamDAO();
        assignmentDAO = new AssignmentDAO();
        classScheduleDAO = new ClassScheduleDAO();
        courseDAO = new CourseDAO();

        // setting the dateLabels array
        dateLabels = new Label[]{
                mondayDate, tuesdayDate, wednesdayDate, thursdayDate, fridayDate, saturdayDate, sundayDate
        };

        // Fetch the language
        Map<String, String> language = settingDAO.getLanguage();

        courses = timeTableDAO.getCourses();

        this.locale = Locale.of(language.get("language"), language.get("region"));
        Locale.setDefault(this.locale);
        bundle = ResourceBundle.getBundle(bundleName, locale);

        LocalDate today = LocalDate.now();
        this.startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        this.endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);

        fetchAndDisplayCurrentWeeksData(bundle);
    }

    /**
     * Updates the course map with the latest courses from the database.
     */
    public void updateCourseMap() {
        this.courses = timeTableDAO.getCourses();
    }

    /**
     * Fetches and displays the current week's data in the timetable.
     *
     * @param bundle the resource bundle for localization
     */
    public void fetchAndDisplayCurrentWeeksData(ResourceBundle bundle) {
        clearTimetable();
        loadLanguage(locale);

        // Format to dd/MM
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale);
        DateTimeFormatter formatter2 = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale);

        nextWeekButton.setText(bundle.getString("nextWeekButton"));
        lastWeekButton.setText(bundle.getString("lastWeekButton"));

        if ("ar".equals(bundle.getLocale().getLanguage())) {
            weekBox.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
        }
        else
        {
            weekBox.setNodeOrientation(javafx.geometry.NodeOrientation.LEFT_TO_RIGHT);
        }

        // Set the dates to the labels
        mondayDate.setText(startOfWeek.format(formatter));
        // loop through the rest with for-loop
        for (int i = 1; i < 7; i++) {
            dateLabels[i].setText(startOfWeek.plusDays(i).format(formatter));
        }

        // Set the current week label
        currentWeekLabel.setText(startOfWeek.format(formatter2) + " - " + endOfWeek.format(formatter2));

        populateCoursesColumn();


        // Fetch current week's data from database
        List<ClassSchedule> classSchedules = timeTableDAO.getClassSchedule(startOfWeek, endOfWeek);
        List<Assignment> assignments = timeTableDAO.getAssignmentSchedule(startOfWeek, endOfWeek);
        List<StudySession> studySessions = timeTableDAO.getStudySessionSchedule(startOfWeek, endOfWeek);
        List<Exam> exams = timeTableDAO.getExamSchedule(startOfWeek, endOfWeek);

        // Combine all tasks into a single list
        List<MyEvent> allTasks = new ArrayList<>();
        allTasks.addAll(classSchedules);
        allTasks.addAll(assignments);
        allTasks.addAll(studySessions);
        allTasks.addAll(exams);

        // Add tasks to the correct day's VBox
        addTasksToDay(allTasks, bundle);
    }

    /**
     * Shows the next week in the timetable.
     */
    @FXML
    private void showNextWeek() {
        this.startOfWeek = this.startOfWeek.plusDays(7);
        this.endOfWeek = this.endOfWeek.plusDays(7);
        fetchAndDisplayCurrentWeeksData(bundle);

    }

    /**
     * Shows the previous week in the timetable.
     */
    @FXML
    private void showPreviousWeek() {
        this.startOfWeek = this.startOfWeek.minusDays(7);
        this.endOfWeek = this.endOfWeek.minusDays(7);
        fetchAndDisplayCurrentWeeksData(bundle);
    }

    /**
     * Clears the timetable by removing all tasks from the columns.
     */
    private void clearTimetable() {
        dayColumns = new VBox[] {
                mondayColumn, tuesdayColumn, wednesdayColumn, thursdayColumn, fridayColumn, saturdayColumn, sundayColumn
        };
        for (VBox dayColumn : dayColumns) {
            dayColumn.getChildren().clear();
        }
    }

    /**
     * Adds tasks to the appropriate day columns in the timetable.
     *
     * @param tasks  the list of tasks to add
     * @param bundle the resource bundle for localization
     * @param <T>    the type of tasks
     */
    private <T> void addTasksToDay(List<T> tasks, ResourceBundle bundle) {
        tasks.sort(Comparator.comparing(task -> switch (task) {
            case ClassSchedule classSchedule -> classSchedule.getStartTime();
            case StudySession studySession -> studySession.getStartTime();
            case Exam exam -> exam.getExamDate();
            case Assignment assignment -> assignment.getDeadline();
            case null, default -> LocalDateTime.MAX;
        }));

        for (T task : tasks) {
            VBox dayColumn = getDayColumn(getTaskDayOfWeek(task));
            VBox taskBox = createTaskBox(task, bundle);
            taskBox.setOnMouseClicked(e -> detailsPopup(task, bundle));
            dayColumn.getChildren().add(taskBox);
        }
    }

    /**
     * Returns the VBox corresponding to the given day of the week.
     *
     * @param dayOfWeek the day of the week (1 = Monday, 7 = Sunday)
     * @return the VBox for the specified day
     */
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

    /**
     * Creates a VBox for the given task type.
     *
     * @param task   the task object
     * @param bundle the resource bundle for localization
     * @return the VBox containing the task details
     */
    private VBox createTaskBox(Object task, ResourceBundle bundle) {
        return switch (task) {
            case ClassSchedule classSchedule -> createClassScheduleBox(classSchedule, bundle);
            case Assignment assignment -> createAssignmentBox(assignment, bundle);
            case StudySession studySession -> createStudySessionBox(studySession, bundle);
            case Exam exam -> createExamBox(exam, bundle);
            case null, default -> throw new IllegalArgumentException("Unknown task type");
        };

    }

    /**
     * Creates a VBox for the ClassSchedule task.
     *
     * @param classSchedule the ClassSchedule object
     * @param bundle        the resource bundle for localization
     * @return the VBox containing the ClassSchedule details
     */
    private VBox createClassScheduleBox(ClassSchedule classSchedule, ResourceBundle bundle) {
        VBox taskBox = new VBox();
        taskBox.getStyleClass().add("class-schedule-box");

        // Localized labels
        Label courseLabel = new Label(courses.get(classSchedule.getCourseId()));
        Label classScheduleTaskLabel = new Label(bundle.getString("classScheduleBoxLabel"));
        classScheduleTaskLabel.setStyle("-fx-font-weight: bold;");
        Label timeLabel = new Label(classSchedule.getStartTime().toLocalTime() + " - " + classSchedule.getEndTime().toLocalTime());
        Label locationLabel = new Label(bundle.getString("locationBoxLabel") + classSchedule.getLocation());

        taskBox.getChildren().addAll(courseLabel, classScheduleTaskLabel, timeLabel, locationLabel);
        return taskBox;
    }

    /**
     * Creates a VBox for the Assignment task.
     *
     * @param assignment the Assignment object
     * @param bundle     the resource bundle for localization
     * @return the VBox containing the Assignment details
     */
    private VBox createAssignmentBox(Assignment assignment, ResourceBundle bundle) {
        VBox taskBox = new VBox();
        taskBox.getStyleClass().add("assignment-box");

        // Localized labels
        Label courseLabel = new Label(courses.get(assignment.getCourseId()));
        Label assignmentTaskLabel = new Label(bundle.getString("assignmentLabel"));
        assignmentTaskLabel.setStyle("-fx-font-weight: bold;");
        Label timeLabel = new Label(bundle.getString("deadlineLabel") + assignment.getDeadline().toLocalTime().toString());
        Label statusLabel = new Label(bundle.getString("statusLabel") + assignment.getStatus());

        taskBox.getChildren().addAll(courseLabel, assignmentTaskLabel, timeLabel, statusLabel);
        return taskBox;
    }

    /**
     * Creates a VBox for the StudySession task.
     *
     * @param studySession the StudySession object
     * @param bundle       the resource bundle for localization
     * @return the VBox containing the StudySession details
     */
    private VBox createStudySessionBox(StudySession studySession, ResourceBundle bundle) {
        VBox taskBox = new VBox();
        taskBox.getStyleClass().add("study-session-box");

        // Localized labels
        Label courseLabel = new Label(courses.get(studySession.getCourseId()));
        Label studySessionTaskLabel = new Label(bundle.getString("studySessionLabel"));
        studySessionTaskLabel.setStyle("-fx-font-weight: bold;");
        Label timeLabel = new Label(studySession.getStartTime().toLocalTime() + " - " + studySession.getEndTime().toLocalTime());

        taskBox.getChildren().addAll(courseLabel, studySessionTaskLabel, timeLabel);
        return taskBox;
    }

    /**
     * Creates a VBox for the Exam task.
     *
     * @param exam   the Exam object
     * @param bundle the resource bundle for localization
     * @return the VBox containing the Exam details
     */
    private VBox createExamBox(Exam exam, ResourceBundle bundle) {
        VBox taskBox = new VBox();
        taskBox.getStyleClass().add("exam-box");

        // Localized labels
        Label courseLabel = new Label(courses.get(exam.getCourseId()));
        Label examTaskLabel = new Label(bundle.getString("examLabel"));
        examTaskLabel.setStyle("-fx-font-weight: bold;");
        Label timeLabel = new Label(exam.getExamDate().toLocalTime().toString());
        Label locationLabel = new Label(bundle.getString("locationBoxLabel") + exam.getLocation());

        taskBox.getChildren().addAll(courseLabel, examTaskLabel, timeLabel, locationLabel);
        return taskBox;
    }

    /**
     * Returns the day of the week for the given task.
     *
     * @param task the task object
     * @return the day of the week (1 = Monday, 7 = Sunday)
     */
    private int getTaskDayOfWeek(Object task) {
        return switch (task) {
            case ClassSchedule classSchedule -> classSchedule.getStartTime().getDayOfWeek().getValue();
            case Assignment assignment -> assignment.getDeadline().getDayOfWeek().getValue();
            case StudySession studySession -> studySession.getStartTime().getDayOfWeek().getValue();
            case Exam exam -> exam.getExamDate().getDayOfWeek().getValue();
            case null, default -> throw new IllegalArgumentException("Unknown task type");
        };
    }

    /**
     * Handles the action when the add button is clicked.
     * Opens a new window to add a new task.
     */
    @FXML
    private void addButtonClicked() {
        try {
            Locale defaultLocale = Locale.getDefault();
            bundle = ResourceBundle.getBundle(bundleName, defaultLocale);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addTask.fxml"), bundle);
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/timetable.css");

            AddTaskController controller = loader.getController();
            controller.setTimetableController(this);
            controller.setBundle(bundle);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays a popup with the details of the selected event.
     *
     * @param event  the event object
     * @param bundle the resource bundle for localization
     */
    private <T> void detailsPopup(T event, ResourceBundle bundle) {
        try {
            Stage popupStage = new Stage();
            popupStage.setTitle(bundle.getString("eventDetailsLabel"));

            VBox popupVBox = new VBox(10);
            popupVBox.setStyle("-fx-padding: 20;");

            Label titleLabel = new Label(bundle.getString("titleLabel") + " ");
            TextField titleField = new TextField(getEventTitle((MyEvent) event));
            Label titleErrorLabel = new Label();
            titleErrorLabel.setId("errorLabel");

            if (!(event instanceof ClassSchedule)) {
                popupVBox.getChildren().addAll(titleLabel, titleField, titleErrorLabel);
            }

            Label dateLabel = new Label(bundle.getString("dateLabel"));
            DatePicker datePicker = new DatePicker(getEventDate((MyEvent) event).toLocalDate());
            Label dateErrorLabel = new Label();
            dateErrorLabel.setId("errorLabel");

            datePicker.setConverter(new StringConverter<LocalDate>() {
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                @Override
                public String toString(LocalDate date) {
                    return date != null ? date.format(formatter) : "";
                }

                @Override
                public LocalDate fromString(String string) {
                    return string != null && !string.isEmpty() ? LocalDate.parse(string, formatter) : null;
                }
            });

            Label fromTimeLabel = new Label(bundle.getString("fromTimeLabel") + " ");
            ChoiceBox<String> fromTimeChoiceBox = new ChoiceBox<>();
            fromTimeChoiceBox.getItems().addAll("06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");
            fromTimeChoiceBox.setValue(getEventStartTime((MyEvent) event));

            Label toTimeLabel = new Label(bundle.getString("toTimeLabel") + " ");
            ChoiceBox<String> toTimeChoiceBox = new ChoiceBox<>();
            toTimeChoiceBox.getItems().addAll("07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00");
            toTimeChoiceBox.setValue(getEventEndTime((MyEvent) event));
            Label toTimeErrorLabel= new Label();
            toTimeErrorLabel.setId("errorLabel");

            Label descriptionLabel = new Label(bundle.getString("descriptionLabel") + " ");
            TextArea descriptionField = new TextArea(getEventDescription((MyEvent) event));

            TextField locationField = null;
            if (event instanceof ClassSchedule || event instanceof Exam) {
                Label locationLabel = new Label(bundle.getString("locationBoxLabel") + " ");
                locationField = new TextField(getEventLocation(event));
                popupVBox.getChildren().addAll(locationLabel, locationField);
            }
            final TextField finalLocationField = locationField;

            if (event instanceof Assignment assignment) {
                Label statusLabel = new Label(bundle.getString("statusLabel") + " ");
                ToggleGroup statusGroup = new ToggleGroup();

                RadioButton notStartedButton = new RadioButton(bundle.getString("notStartedButton"));
                notStartedButton.setToggleGroup(statusGroup);
                RadioButton ongoingButton = new RadioButton(bundle.getString("ongoingButton"));
                ongoingButton.setToggleGroup(statusGroup);
                RadioButton completedButton = new RadioButton(bundle.getString("completedButton"));
                completedButton.setToggleGroup(statusGroup);

                switch (assignment.getStatus()) {
                    case "Ongoing" -> ongoingButton.setSelected(true);
                    case "Completed" -> completedButton.setSelected(true);
                    default -> notStartedButton.setSelected(true);
                }

                popupVBox.getChildren().addAll(statusLabel, notStartedButton, ongoingButton, completedButton);
            }

            VBox timeBox = new VBox(10, fromTimeLabel, fromTimeChoiceBox);


            if (!(event instanceof Assignment || event instanceof Exam)) {
                timeBox.getChildren().add(toTimeLabel);
                timeBox.getChildren().add(toTimeChoiceBox);
                timeBox.getChildren().add(toTimeErrorLabel);
                toTimeErrorLabel = timeBox.getChildren().get(4) instanceof Label ? (Label) timeBox.getChildren().get(4) : new Label();
            }

            Label finalToTimeErrorLabel = toTimeErrorLabel;


            Button saveButton = new Button(bundle.getString("saveButton"));
            Button deleteButton = new Button(bundle.getString("deleteButton"));
            Button backButton = new Button(bundle.getString("backButton"));

            HBox actionButtons = new HBox(30, deleteButton, saveButton);
            actionButtons.setAlignment(Pos.CENTER);

            VBox buttonVBox = new VBox(40, actionButtons, backButton);
            buttonVBox.setAlignment(Pos.CENTER);

            saveButton.setOnAction(e -> handleSaveEvent(event, titleField, titleErrorLabel, datePicker, dateErrorLabel, fromTimeChoiceBox, finalToTimeErrorLabel, toTimeChoiceBox, descriptionField, finalLocationField, popupStage));
            deleteButton.setOnAction(e -> handleDeleteEvent(event, popupStage));

            backButton.setOnAction(e -> popupStage.close()); // Close the popup without any changes

            popupVBox.getChildren().addAll(dateLabel, datePicker, dateErrorLabel, timeBox, descriptionLabel, descriptionField, buttonVBox);

            if ("ar".equals(bundle.getLocale().getLanguage())) {
                popupVBox.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
            }
            Scene popupScene = new Scene(popupVBox, 400, 600);
            popupScene.getStylesheets().add("/timetable.css");
            popupStage.setScene(popupScene);
            popupStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Handles the save event when the save button is clicked in the popup.
     *
     * @param event         the event object
     * @param titleField    the title field
     * @param datePicker    the date picker
     * @param fromTimeChoiceBox the "from" time choice box
     * @param toTimeChoiceBox   the "to" time choice box
     * @param descriptionField  the description field
     * @param locationField     the location field
     * @param popupStage        the popup stage
     */
    private <T> void handleSaveEvent(T event, TextField titleField, Label titleErrorLabel, DatePicker datePicker, Label dateErrorLabel, ChoiceBox<String> fromTimeChoiceBox, Label toTimeErrorLabel, ChoiceBox<String> toTimeChoiceBox, TextArea descriptionField, TextField locationField, Stage popupStage) {
        String newTitle = titleField.getText();
        LocalDate newDate = datePicker.getValue();
        String newFromTime = fromTimeChoiceBox.getValue();
        String newToTime = toTimeChoiceBox.getValue();
        String newDescription = descriptionField.getText();

        dateErrorLabel.setText("");
        titleErrorLabel.setText("");
        toTimeErrorLabel.setText("");

        if (newDate == null) {
            dateErrorLabel.setText(bundle.getString("dateErrorLabel"));
            return;
        }

        LocalTime startTime = LocalTime.parse(newFromTime, DateTimeFormatter.ofPattern("H:mm"));
        LocalTime endTime = LocalTime.parse(newToTime, DateTimeFormatter.ofPattern("H:mm"));

        LocalDateTime startDateTime = LocalDateTime.of(newDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(newDate, endTime);

        try {
            if (event instanceof StudySession studySession) {       // STUDY SESSION
                if(newTitle.isEmpty() || startTime.isAfter(endTime)) {
                    titleErrorLabel.setText(newTitle.isEmpty() ? bundle.getString("titleErrorLabel") : "");
                    toTimeErrorLabel.setText(startTime.isAfter(endTime) ? bundle.getString("toTimeBeforeFromTimeError") : "");
                    return;
                }
                studySession.setTitle(newTitle);
                studySession.setDescription(newDescription);
                studySession.setStartTime(startDateTime);
                studySession.setEndTime(endDateTime);
                studySessionDAO.update(studySession);
            } else if (event instanceof Exam exam) {        // EXAM
                if (newTitle.isEmpty()) {
                    titleErrorLabel.setText(bundle.getString("titleErrorLabel"));
                    return;
                }
                exam.setTitle(newTitle);
                exam.setDescription(newDescription);
                LocalDateTime updatedExamDate = LocalDateTime.of(newDate, startTime);
                exam.setExamDate(updatedExamDate);
                if (locationField != null) {
                    exam.setLocation(locationField.getText());
                }
                examDAO.update(exam);
            } else if (event instanceof Assignment assignment) {        // ASSIGNMENT
                if (newTitle.isEmpty()) {
                    titleErrorLabel.setText(bundle.getString("titleErrorLabel"));
                    return;
                }
                assignment.setTitle(newTitle);
                assignment.setDescription(newDescription);
                assignment.setDeadline(startDateTime);
                ToggleGroup statusGroup = ((VBox) popupStage.getScene().getRoot()).getChildren().stream()
                        .filter(node -> node instanceof RadioButton)
                        .map(node -> ((RadioButton) node).getToggleGroup())
                        .findFirst()
                        .orElse(null);

                if (statusGroup != null) {
                    RadioButton selectedStatus = (RadioButton) statusGroup.getSelectedToggle();
                    assignment.setStatus(selectedStatus.getText());
                }
                assignmentDAO.update(assignment);
            } else if (event instanceof ClassSchedule classSchedule) {  // CLASS SCHEDULE
                if (startTime.isAfter(endTime)) {
                    toTimeErrorLabel.setText(bundle.getString("toTimeBeforeFromTimeError"));
                    return;
                }

                classSchedule.setDescription(newDescription);
                classSchedule.setStartTime(startDateTime);
                classSchedule.setEndTime(endDateTime);
                if (locationField != null) {
                    classSchedule.setLocation(locationField.getText());
                }
                classScheduleDAO.update(classSchedule);
            }
            System.out.println("Event saved successfully: " + getEventTitle((MyEvent) event));

            showAlert(Alert.AlertType.INFORMATION, bundle.getString("eventSavedTitle"), bundle.getString("eventSavedMessage"));

            popupStage.close();
            fetchAndDisplayCurrentWeeksData(bundle);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, bundle.getString("eventSaveErrorTitle"), bundle.getString("eventSaveErrorMessage"));
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


    /**
     * Handles the delete event when the delete button is clicked in the popup.
     *
     * @param event      the event object
     * @param popupStage the popup stage
     */
    private void handleDeleteEvent(Object event, Stage popupStage) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(bundle.getString("confirmationTitle"));
        confirmationAlert.setHeaderText(bundle.getString("confirmationHeader"));
        confirmationAlert.setContentText(bundle.getString("confirmationMessage"));

        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                System.out.println("Delete event: " + getEventTitle((MyEvent) event));
                deleteEvent(event);
                showAlert(Alert.AlertType.INFORMATION, bundle.getString("eventDeletedTitle"), bundle.getString("eventDeletedMessage"));
                popupStage.close();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, bundle.getString("eventDeleteErrorTitle"), bundle.getString("eventDeleteErrorMessage"));
                e.printStackTrace();
            }
        } else {
            System.out.println("Delete action canceled");
        }
    }

    private void deleteEvent(Object event) {
        if (event instanceof ClassSchedule classSchedule) {
            int id = classSchedule.getId();
            classScheduleDAO.delete(id);
        } else if (event instanceof StudySession studySession) {
            int id = studySession.getId();
            studySessionDAO.delete(id);
        } else if (event instanceof Exam exam) {
            int id = exam.getId();
            examDAO.delete(id);
        } else if (event instanceof Assignment assignment) {
            int id = assignment.getId();
            assignmentDAO.delete(id);
        }

        fetchAndDisplayCurrentWeeksData(bundle);
    }

    /**
     * Returns the day of the week for the given event.
     *
     * @param myEvent the event object
     * @return the day of the week (1 = Monday, 7 = Sunday)
     */
    private String getEventTitle(MyEvent myEvent) {
        return myEvent.getTitle();
    }

    /**
     * Returns the date of the event.
     *
     * @param myEvent the event object
     * @return the date of the event
     */
    private LocalDateTime getEventDate(MyEvent myEvent) {
        return myEvent.getDate();
    }

    /**
     * Returns the start time of the event.
     *
     * @param myEvent the event object
     * @return the start time of the event
     */
    private String getEventStartTime(MyEvent myEvent) {
        return myEvent.getEventStartTime().toString();
    }

    /**
     * Gets the end time of the event
     *
     * @param myEvent the event object
     * @return the end time of the event
     */
    private String getEventEndTime(MyEvent myEvent) {
        return myEvent.getEndTime().toLocalTime().toString();
    }

    /**
     * Returns the location of the event.
     *
     * @param event the event object
     * @return the location of the event
     */
    private <T> String getEventLocation(T event) {
        if (event instanceof ClassSchedule classSchedule) {
            return classSchedule.getLocation();
        } else if (event instanceof Exam exam) {
            return exam.getLocation();
        }
        return "";
    }

    /**
     * Populates the courses column with course boxes.
     */
    private void populateCoursesColumn() {
        coursesColumn.getChildren().clear();

        for (Map.Entry<Integer, String> courseEntry : courses.entrySet()) {
            VBox courseBox = createCourseBox(courseEntry.getKey(), courseEntry.getValue());
            coursesColumn.getChildren().add(courseBox);
        }
    }

    /**
     * Creates a VBox for a course.
     *
     * @param courseId   the ID of the course
     * @param courseName the name of the course
     * @return the VBox containing the course details
     */
    private VBox createCourseBox(int courseId, String courseName) {
        VBox courseBox = new VBox();
        courseBox.getStyleClass().add("course-box");
        courseBox.setSpacing(5); // Add spacing between elements
        courseBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");

        Label courseNameLabel = new Label(courseName);
        courseNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label courseIdLabel = new Label("ID: " + courseId);
        courseIdLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        courseBox.getChildren().addAll(courseNameLabel, courseIdLabel);

        // Optional: Add click event to handle course selection or details
        courseBox.setOnMouseClicked(e -> handleCourseClick(courseId, courseName));

        return courseBox;
    }

/**
 * Handles the action when a course box is clicked.
 *
 * @param courseId   the ID of the clicked course
 * @param courseName the name of the clicked course
 */
private void handleCourseClick(int courseId, String courseName) {
    try {
        Stage popupStage = new Stage();
        popupStage.setTitle(bundle.getString("courseDetailsLabel"));

        VBox popupVBox = new VBox(20);
        popupVBox.setStyle("-fx-padding: 40;");

        Course course = courseDAO.get(courseId);

        // Course details
        Label courseNameLabel = new Label(bundle.getString("courseNameLabel"));
        TextField courseNameField = new TextField(courseName);

        Label instructorLabel = new Label(bundle.getString("instructorLabel"));
        TextField instructorField = new TextField(course.getInstructor());

        Label startDateLabel = new Label(bundle.getString("startDateLabel"));
        DatePicker startDatePicker = new DatePicker(course.getStartDate());
        Label endDateLabel = new Label(bundle.getString("endDateLabel"));
        DatePicker endDatePicker = new DatePicker(course.getEndDate());

        Button deleteButton = new Button(bundle.getString("deleteButton"));
        Button saveButton = new Button(bundle.getString("saveButton"));
        Button backButton = new Button(bundle.getString("backButton"));

        HBox buttonHBox = new HBox(40, deleteButton, saveButton);
        buttonHBox.setAlignment(Pos.CENTER);

        HBox backButtonHBox = new HBox(backButton);
        backButtonHBox.setAlignment(Pos.CENTER);
        backButton.setMaxWidth(Double.MAX_VALUE);

        saveButton.setOnAction(e -> handleEditCourse(course, courseNameField.getText(), instructorField.getText(), startDatePicker.getValue(), endDatePicker.getValue(), popupStage));
        deleteButton.setOnAction(e -> handleDeleteCourse(courseId, popupStage));
        backButton.setOnAction(e -> popupStage.close());

        popupVBox.getChildren().addAll(
                courseNameLabel, courseNameField,
                instructorLabel, instructorField,
                startDateLabel, startDatePicker,
                endDateLabel, endDatePicker,
                buttonHBox,
                backButtonHBox
        );

        if ("ar".equals(bundle.getLocale().getLanguage())) {
            popupVBox.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
        }

        Scene popupScene = new Scene(popupVBox, 300, 500);
        popupScene.getStylesheets().add("/timetable.css");
        popupStage.setScene(popupScene);
        popupStage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


/**
 * Handles the edit course action.
 *
 *
 */
private void handleEditCourse(Course course, String courseName, String instructor, LocalDate startDate, LocalDate endDate, Stage popupStage) {
    System.out.println("Edit course: " + courseName + " (ID: " + course.getCourseID() + ")");
    course.setCourseName(courseName);
    course.setInstructor(instructor);
    course.setStartDate(startDate);
    course.setEndDate(endDate);
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmationAlert.setTitle(bundle.getString("confirmationTitle"));
    confirmationAlert.setHeaderText(bundle.getString("confirmationHeader"));
    confirmationAlert.setContentText(bundle.getString("confirmationMessage"));

    Optional<ButtonType> result = confirmationAlert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
        try {
            courseDAO.update(course);
            popupStage.close();
            updateCourseMap();
            showAlert(Alert.AlertType.INFORMATION, bundle.getString("eventSavedTitle"), bundle.getString("eventSavedMessage"));
            // Update the UI after saving the exam
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }

                Platform.runLater(() -> {
                    System.out.println("Updating UI...");
                    fetchAndDisplayCurrentWeeksData(bundle);
                    System.out.println("UI updated.");
                });
            }).start();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, bundle.getString("eventSaveErrorTitle"), bundle.getString("eventSaveErrorMessage"));
            e.printStackTrace();
        }
    }
}

/**
 * Handles the delete course action.
 *
 * @param courseId   the ID of the course to delete
 * @param popupStage the popup stage to close after deletion
 */
private void handleDeleteCourse(int courseId, Stage popupStage) {
    System.out.println("Delete course: ID " + courseId);
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmationAlert.setTitle(bundle.getString("confirmationTitle"));
    confirmationAlert.setHeaderText(bundle.getString("confirmationHeader"));
    confirmationAlert.setContentText(bundle.getString("confirmationMessage"));

    Optional<ButtonType> result = confirmationAlert.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
        try {

            courseDAO.delete(courseId);
            popupStage.close();
            updateCourseMap();
            // Update the UI after saving the exam
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }

                Platform.runLater(() -> {
                    System.out.println("Updating UI...");
                    fetchAndDisplayCurrentWeeksData(bundle);
                    System.out.println("UI updated.");
                });
            }).start();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, bundle.getString("eventDeleteErrorTitle"), bundle.getString("eventDeleteErrorMessage"));
            e.printStackTrace();
        }
    }
}


    /**
     * Handles the action when the English button is clicked.
     */
    @FXML
    private void onEnglishClicked() {
        setLanguage("en", "UK", "English");
    }

    /**
     * Handles the action when the Korean button is clicked.
     */
    @FXML
    private void onKoreanClicked() {
        setLanguage("ko", "KR", "Korean");
    }

    /**
     * Handles the action when the Arabic button is clicked.
     */
    @FXML
    private void onArabicClicked() {
        setLanguage("ar", "AE", "Arabic");
    }

    /**
     * Updates the language, saves it to the database, and refreshes the timetable.
     */
    private void setLanguage(String languageCode, String regionCode, String languageName) {
        // Update the language in the database
        settingDAO.setLanguage(languageCode, regionCode);
        Locale.setDefault(new Locale(languageCode, regionCode));
        this.locale = Locale.getDefault();
        System.out.printf("%s clicked", languageName);
        loadLanguage(locale);
        fetchAndDisplayCurrentWeeksData(bundle);
    }

    /**
     * Loads the localized strings for the application based on the current locale.
     *
     * @param locale the current locale
     */
    private void loadLanguage(Locale locale) {
        bundle = ResourceBundle.getBundle(bundleName, locale);

        nameLabel.setText(bundle.getString("nameLabel"));
        addButton.setText(bundle.getString("addLabel"));
        coursesLabel.setText(bundle.getString("coursesLabel"));
        Label[] dayLabels = new Label[] {
                mondayLabel, tuesdayLabel, wednesdayLabel, thursdayLabel, fridayLabel, saturdayLabel, sundayLabel
        };
        String[] dayBundleLabels = new String[] {
                "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"
        };
        for (int i = 0; i < dayLabels.length; i++) {
            Label dayLabel = dayLabels[i];
            dayLabel.setText(bundle.getString(dayBundleLabels[i]+"Label"));
        }
    }

    /**
     * Returns the description of the event.
     *
     * @param myEvent the event object
     * @return the description of the event
     */
    private String getEventDescription(MyEvent myEvent) {
        return myEvent.getDescription();
    }

    @FXML
    private void openHelpDialog() {
        try {
            Locale defaultLocale = Locale.getDefault();
            bundle = ResourceBundle.getBundle(bundleName, defaultLocale);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/helpView.fxml"), bundle);
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/timetable.css");

            HelpViewController controller = loader.getController();
            controller.setMainDialog(bundle.getString("helpMainDialog"));
            controller.setMainGuide(bundle.getString("helpMainGuide"));

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(bundle.getString("helpWindowName"));
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}