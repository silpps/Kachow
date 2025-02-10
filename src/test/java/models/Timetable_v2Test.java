package models;

import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Timetable_v2Test {

    private Timetable_v2 timetable;

    @BeforeEach
    void setUp() {
        timetable = new Timetable_v2("8:00");

    }

    @Test
    void getTime() {
        assertEquals("8:00", timetable.getTime());

    }

    @Test
    void setTime() {
        timetable.setTime("9:00");
        assertEquals("9:00", timetable.getTime());
    }

    @Test
    void timeProperty() {
        SimpleStringProperty time = timetable.timeProperty();
        assertEquals("8:00", time.get());
        assertNotNull(time, "Time property is not null");
    }

    @Test
    void getMonday() {
        assertEquals("", timetable.getMonday());
    }

    @Test
    void setMonday() {
        timetable.setMonday("Math");
        assertEquals("Math", timetable.getMonday());

    }

    @Test
    void mondayProperty() {
        SimpleStringProperty monday = timetable.mondayProperty();
        assertEquals("", monday.get());
        assertNotNull(monday, "Monday property is not null");

    }

    @Test
    void getTuesday() {
        assertEquals("", timetable.getTuesday());
    }

    @Test
    void setTuesday() {
        timetable.setTuesday("Java");
        assertEquals("Java", timetable.getTuesday());
    }

    @Test
    void tuesdayProperty() {
        SimpleStringProperty tuesday = timetable.tuesdayProperty();
        assertEquals("", tuesday.get());
        assertNotNull(tuesday, "Tuesday property is not null");
    }

    @Test
    void getWednesday() {
        assertEquals("", timetable.getWednesday());
    }

    @Test
    void setWednesday() {
        timetable.setWednesday("Python");
        assertEquals("Python", timetable.getWednesday());
    }

    @Test
    void wednesdayProperty() {
        SimpleStringProperty wednesday = timetable.wednesdayProperty();
        assertEquals("", wednesday.get());
        assertNotNull(wednesday, "Wednesday property is not null");
    }

    @Test
    void getThursday() {
        assertEquals("", timetable.getThursday());
    }

    @Test
    void setThursday() {
        timetable.setThursday("C++");
        assertEquals("C++", timetable.getThursday());
    }

    @Test
    void thursdayProperty() {
        SimpleStringProperty thursday = timetable.thursdayProperty();
        assertEquals("", thursday.get());
        assertNotNull(thursday, "Thursday property is not null");
    }

    @Test
    void getFriday() {
        assertEquals("", timetable.getFriday());
    }

    @Test
    void setFriday() {
        timetable.setFriday("C#");
        assertEquals("C#", timetable.getFriday());
    }

    @Test
    void fridayProperty() {
        SimpleStringProperty friday = timetable.fridayProperty();
        assertEquals("", friday.get());
        assertNotNull(friday, "Friday property is not null");
    }

    @Test
    void getSaturday() {
        assertEquals("", timetable.getSaturday());
    }

    @Test
    void setSaturday() {
        timetable.setSaturday("Study Session");
        assertEquals("Study Session", timetable.getSaturday());
    }

    @Test
    void saturdayProperty() {
        SimpleStringProperty saturday = timetable.saturdayProperty();
        assertEquals("", saturday.get());
        assertNotNull(saturday, "Saturday property is not null");
    }

    @Test
    void getSunday() {
        assertEquals("", timetable.getSunday());
    }

    @Test
    void setSunday() {
        timetable.setSunday("Sleep");
        assertEquals("", timetable.getSunday());
    }

    @Test
    void sundayProperty() {
    }
}