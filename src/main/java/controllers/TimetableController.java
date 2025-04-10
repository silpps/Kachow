package controllers;

import dao.*;
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


public class TimetableController implements Initializable {

    @FXML
    private HBox weekBox;

    @FXML
    private VBox mondayColumn, tuesdayColumn, wednesdayColumn, thursdayColumn, fridayColumn, saturdayColumn, sundayColumn;

    @FXML
    private Label mondayDate, tuesdayDate, wednesdayDate, thursdayDate, fridayDate, saturdayDate, sundayDate, currentWeekLabel, nameLabel, mondayLabel, tuesdayLabel, wednesdayLabel, thursdayLabel, fridayLabel, saturdayLabel, sundayLabel;

    @FXML
    private Button addButton, nextWeekButton, lastWeekButton;

    private TimeTableDAO timeTableDAO;
    private LocalDate startOfWeek;
    private LocalDate endOfWeek;
    private IDAO<StudySession> studySessionDAO;
    private IDAO<Exam> examDAO;
    private IDAO<Assignment> assignmentDAO;
    private IDAO<ClassSchedule> classScheduleDAO;
    private SettingDAO settingDAO;
    private Map<Integer, String> courses;
    private Locale locale;
    private ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the DAO classes
        settingDAO = new SettingDAO();
        timeTableDAO = new TimeTableDAO();
        studySessionDAO = new StudySessionDAO();
        examDAO = new ExamDAO();
        assignmentDAO = new AssignmentDAO();
        classScheduleDAO = new ClassScheduleDAO();

        // Fetch the language
        Map<String, String> language = settingDAO.getLanguage();

        courses = timeTableDAO.getCourses();

        this.locale = Locale.of(language.get("language"), language.get("region"));

        LocalDate today = LocalDate.now();
        this.startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        this.endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);

        fetchAndDisplayCurrentWeeksData(bundle);
    }


    public void updateCourseMap() {
        this.courses = timeTableDAO.getCourses();
    }

    public void fetchAndDisplayCurrentWeeksData(ResourceBundle bundle) {
        clearTimetable();
        loadLanguage(locale);

        // Format to dd/MM
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale);
        DateTimeFormatter formatter2 = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale);

        nextWeekButton.setText(bundle.getString("nextWeekButton"));
        lastWeekButton.setText(bundle.getString("lastWeekButton"));

        if (locale.getLanguage().equals("ar")) {
            weekBox.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
        }
        else
        {
            weekBox.setNodeOrientation(javafx.geometry.NodeOrientation.LEFT_TO_RIGHT);
        }

        // Set the dates to the labels
        mondayDate.setText(startOfWeek.format(formatter));
        tuesdayDate.setText(startOfWeek.plusDays(1).format(formatter));
        wednesdayDate.setText(startOfWeek.plusDays(2).format(formatter));
        thursdayDate.setText(startOfWeek.plusDays(3).format(formatter));
        fridayDate.setText(startOfWeek.plusDays(4).format(formatter));
        saturdayDate.setText(startOfWeek.plusDays(5).format(formatter));
        sundayDate.setText(startOfWeek.plusDays(6).format(formatter));


        //TODO: Localize date format
        currentWeekLabel.setText(startOfWeek.format(formatter2) + " - " + endOfWeek.format(formatter2));



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
        addTasksToDay(allTasks, bundle);
    }

    @FXML
    private void showNextWeek() {
        this.startOfWeek = this.startOfWeek.plusDays(7);
        this.endOfWeek = this.endOfWeek.plusDays(7);
        fetchAndDisplayCurrentWeeksData(bundle);

    }

    @FXML
    private void showPreviousWeek() {
        this.startOfWeek = this.startOfWeek.minusDays(7);
        this.endOfWeek = this.endOfWeek.minusDays(7);
        fetchAndDisplayCurrentWeeksData(bundle);
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


    //TODO: Localize labels
    private VBox createTaskBox(Object task, ResourceBundle bundle) {
        return switch (task) {
            case ClassSchedule classSchedule -> createClassScheduleBox(classSchedule, bundle);
            case Assignment assignment -> createAssignmentBox(assignment, bundle);
            case StudySession studySession -> createStudySessionBox(studySession, bundle);
            case Exam exam -> createExamBox(exam, bundle);
            case null, default -> throw new IllegalArgumentException("Unknown task type");
        };

    }

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

    private VBox createAssignmentBox(Assignment assignment, ResourceBundle bundle) {
        VBox taskBox = new VBox();
        taskBox.getStyleClass().add("assignment-box");

        // Localized labels
        Label courseLabel = new Label(courses.get(assignment.getCourseId()));
        Label assignmentTaskLabel = new Label(bundle.getString("assignmentLabel"));
        assignmentTaskLabel.setStyle("-fx-font-weight: bold;");
        Label timeLabel = new Label(bundle.getString("deadlineLabel") + assignment.getDeadline().toLocalTime().toString());
        Label statusLabel = new Label(bundle.getString("statusLabel") + (assignment.getStatus()));

        taskBox.getChildren().addAll(courseLabel, assignmentTaskLabel, timeLabel, statusLabel);
        return taskBox;
    }

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
            Locale locale = Locale.getDefault();
            ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

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


    //TODO: Localize labels and date and time formats
    private <T> void detailsPopup(T event, ResourceBundle bundle) {
        try {
            Stage popupStage = new Stage();
            popupStage.setTitle(bundle.getString("eventDetailsLabel"));

            VBox popupVBox = new VBox(10);
            popupVBox.setStyle("-fx-padding: 20;");

            Label titleLabel = new Label(bundle.getString("titleLabel") + " ");
            TextField titleField = new TextField(getEventTitle(event));

            if (!(event instanceof ClassSchedule)) {
                popupVBox.getChildren().addAll(titleLabel, titleField);
            }


            Label dateLabel = new Label(bundle.getString("dateLabel"));
            DatePicker datePicker = new DatePicker(getEventDate(event).toLocalDate());

            datePicker.setConverter(new StringConverter<LocalDate>() {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
            fromTimeChoiceBox.setValue(getEventStartTime(event));

            Label toTimeLabel = new Label(bundle.getString("toTimeLabel") + " ");
            ChoiceBox<String> toTimeChoiceBox = new ChoiceBox<>();
            toTimeChoiceBox.getItems().addAll("07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00");
            toTimeChoiceBox.setValue(getEventEndTime(event));

            Label descriptionLabel = new Label(bundle.getString("descriptionLabel") + " ");
            TextArea descriptionField = new TextArea(getEventDescription(event));

            TextField locationField = null;
            if (event instanceof ClassSchedule classSchedule || event instanceof Exam exam) {
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
                    case "Not Started" -> notStartedButton.setSelected(true);
                    case "Ongoing" -> ongoingButton.setSelected(true);
                    case "Completed" -> completedButton.setSelected(true);
                }

                popupVBox.getChildren().addAll(statusLabel, notStartedButton, ongoingButton, completedButton);
            }
            VBox timeBox = new VBox(10, fromTimeLabel, fromTimeChoiceBox);

            if (!(event instanceof Assignment || event instanceof Exam)) {
                timeBox.getChildren().add(toTimeLabel);
                timeBox.getChildren().add(toTimeChoiceBox);
            }

            Button saveButton = new Button(bundle.getString("saveButton"));
            Button deleteButton = new Button(bundle.getString("deleteButton"));

            HBox buttonHBox = new HBox(30, deleteButton, saveButton);
            buttonHBox.setAlignment(Pos.CENTER);

            saveButton.setOnAction(e -> handleSaveEvent(event, titleField, datePicker, fromTimeChoiceBox, toTimeChoiceBox, descriptionField, finalLocationField, popupStage));
            deleteButton.setOnAction(e -> handleDeleteEvent(event, popupStage));

            popupVBox.getChildren().addAll(dateLabel, datePicker, timeBox, descriptionLabel, descriptionField, buttonHBox);

            if (bundle.getLocale().getLanguage().equals("ar")) {
                popupVBox.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
            }
            Scene popupScene = new Scene(popupVBox, 300, 600);
            popupScene.getStylesheets().add("/timetable.css");
            popupStage.setScene(popupScene);
            popupStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private <T> void handleSaveEvent(T event, TextField titleField, DatePicker datePicker, ChoiceBox<String> fromTimeChoiceBox, ChoiceBox<String> toTimeChoiceBox, TextArea descriptionField, TextField locationField, Stage popupStage) {
        String newTitle = titleField.getText();
        LocalDate newDate = datePicker.getValue();
        String newFromTime = fromTimeChoiceBox.getValue();
        String newToTime = toTimeChoiceBox.getValue();
        String newDescription = descriptionField.getText();

        LocalTime startTime = LocalTime.parse(newFromTime, DateTimeFormatter.ofPattern("H:mm"));
        LocalTime endTime = LocalTime.parse(newToTime, DateTimeFormatter.ofPattern("H:mm"));


        // Automatically set end time to one hour after start time if end time is not after start time
        if (!endTime.isAfter(startTime)) {
            endTime = startTime.plusHours(1);
            toTimeChoiceBox.setValue(endTime.toString());
        }

        LocalDateTime startDateTime = LocalDateTime.of(newDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(newDate, endTime);

        if (event instanceof StudySession studySession) {
            studySession.setTitle(newTitle);
            studySession.setDescription(newDescription);
            studySession.setStartTime(startDateTime);
            studySession.setEndTime(endDateTime);
            studySessionDAO.update(studySession);
        } else if (event instanceof Exam exam) {
            exam.setTitle(newTitle);
            exam.setDescription(newDescription);
            LocalDateTime updatedExamDate = LocalDateTime.of(newDate, startTime);
            exam.setExamDate(updatedExamDate);
            if (locationField != null) {
                exam.setLocation(locationField.getText());
            }
            examDAO.update(exam);
        } else if (event instanceof Assignment assignment) {
            assignment.setTitle(newTitle);
            assignment.setDescription(newDescription);
            assignment.setDeadline(startDateTime);
            ToggleGroup statusGroup = (ToggleGroup) ((VBox) popupStage.getScene().getRoot()).getChildren().stream()
                    .filter(node -> node instanceof RadioButton)
                    .map(node -> ((RadioButton) node).getToggleGroup())
                    .findFirst()
                    .orElse(null);

            if (statusGroup != null) {
                RadioButton selectedStatus = (RadioButton) statusGroup.getSelectedToggle();
                assignment.setStatus(selectedStatus.getText());
            }
            assignmentDAO.update(assignment);
        } else if (event instanceof ClassSchedule classSchedule) {
            classSchedule.setDescription(newDescription);
            classSchedule.setStartTime(startDateTime);
            classSchedule.setEndTime(endDateTime);
            if (locationField != null) {
                classSchedule.setLocation(locationField.getText());
            }
            classScheduleDAO.update(classSchedule);
        }

        popupStage.close();
        fetchAndDisplayCurrentWeeksData(bundle);
    }



    private void handleDeleteEvent(Object event, Stage popupStage) {
        System.out.println("Delete event: " + getEventTitle(event));

        deleteEvent(event);
        popupStage.close();
    }

    private void deleteEvent(Object event) {
        if (event instanceof ClassSchedule) {
            int id = ((ClassSchedule) event).getId();
            classScheduleDAO.delete(id);
        } else if (event instanceof StudySession) {
            int id = ((StudySession) event).getId();
            studySessionDAO.delete(id);
        } else if (event instanceof Exam) {
            int id = ((Exam) event).getId();
            examDAO.delete(id);
        } else if (event instanceof Assignment) {
            int id = ((Assignment) event).getId();
            assignmentDAO.delete(id);
        }

        fetchAndDisplayCurrentWeeksData(bundle);
    }

    private <T> String getEventTitle(T event) {
        if (event instanceof ClassSchedule classSchedule) {
            return "";
        } else if (event instanceof Assignment assignment) {
            return assignment.getTitle();
        } else if (event instanceof StudySession studySession) {
            return studySession.getTitle();
        } else if (event instanceof Exam exam) {
            return exam.getTitle();
        }
        return "Unknown Event";
    }

    private <T> LocalDateTime getEventDate(T event) {
        if (event instanceof ClassSchedule classSchedule) {
            return classSchedule.getStartTime();
        } else if (event instanceof Assignment assignment) {
            return assignment.getDeadline();
        } else if (event instanceof StudySession studySession) {
            return studySession.getStartTime();
        } else if (event instanceof Exam exam) {
            return exam.getExamDate();
        }
        return LocalDateTime.now(); // Default to now if unknown event
    }

    private <T> String getEventStartTime(T event) {
        if (event instanceof ClassSchedule classSchedule) {
            return classSchedule.getStartTime().toLocalTime().toString();
        } else if (event instanceof StudySession studySession) {
            return studySession.getStartTime().toLocalTime().toString();
        } else if (event instanceof Exam exam) {
            return exam.getExamDate().toLocalTime().toString();
        } else if (event instanceof Assignment assignment) {
            return assignment.getDeadline().toLocalTime().toString();
        }
        return "00:00"; // Default
    }

    private <T> String getEventEndTime(T event) {
        if (event instanceof ClassSchedule classSchedule) {
            return classSchedule.getEndTime().toLocalTime().toString();
        } else if (event instanceof StudySession studySession) {
            return studySession.getEndTime().toLocalTime().toString();
        } else if (event instanceof Exam exam) {
            return exam.getExamDate().toLocalTime().toString();
        } else if (event instanceof Assignment assignment) {
            return assignment.getDeadline().toLocalTime().toString();
        }
        return "01:00"; // Default
    }

    private <T> String getEventLocation(T event) {
        if (event instanceof ClassSchedule classSchedule) {
            return classSchedule.getLocation();
        } else if (event instanceof Exam exam) {
            return exam.getLocation();
        }
        return "";
    }

    private <T> String getEventDescription(T event) {
        if (event instanceof ClassSchedule classSchedule) {
            return classSchedule.getDescription();
        } else if (event instanceof Assignment assignment) {
            return assignment.getDescription();
        } else if (event instanceof StudySession studySession) {
            return studySession.getDescription();
        } else if (event instanceof Exam exam) {
            return exam.getDescription();
        }
        return "";
    }

    @FXML
    private void onEnglishClicked() {
        // Update the language in the database
        settingDAO.setLanguage("en", "UK");

        Locale.setDefault(new Locale("en", "UK"));
        this.locale = Locale.getDefault();
        System.out.println("English clicked");
        bundle = ResourceBundle.getBundle("messages", locale);
        loadLanguage(locale);

        fetchAndDisplayCurrentWeeksData(bundle);
    }

    @FXML
    private void onKoreanClicked() {
        // Update the language in the database
        settingDAO.setLanguage("ko", "KR");
        Locale.setDefault(new Locale("ko", "KR"));
        this.locale = Locale.getDefault();
        System.out.println("Korean clicked");
        bundle = ResourceBundle.getBundle("messages", locale);
        loadLanguage(locale);

        fetchAndDisplayCurrentWeeksData(bundle);
    }



    @FXML
    private void onArabicClicked() {
        // Update the language in the database
        settingDAO.setLanguage("ar", "AE");
        Locale.setDefault(new Locale("ar", "AE"));
        this.locale = Locale.getDefault();
        System.out.println("Arabic clicked");

        loadLanguage(locale);
        bundle = ResourceBundle.getBundle("messages", locale);

        fetchAndDisplayCurrentWeeksData(bundle);
    }

    private void loadLanguage(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        nameLabel.setText(bundle.getString("nameLabel"));
        addButton.setText(bundle.getString("addLabel"));
        mondayLabel.setText(bundle.getString("mondayLabel"));
        tuesdayLabel.setText(bundle.getString("tuesdayLabel"));
        wednesdayLabel.setText(bundle.getString("wednesdayLabel"));
        thursdayLabel.setText(bundle.getString("thursdayLabel"));
        fridayLabel.setText(bundle.getString("fridayLabel"));
        saturdayLabel.setText(bundle.getString("saturdayLabel"));
        sundayLabel.setText(bundle.getString("sundayLabel"));

    }


}