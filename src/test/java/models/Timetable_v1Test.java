package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class Timetable_v1Test {

    @Test
    void getTime() {
        Timetable_v1 timetable = new Timetable_v1("08:00");
        assertEquals("08:00", timetable.getTime(), "Time should be 08:00");

    }

    @Test
    void timeProperty() {
        Timetable_v1 timetable = new Timetable_v1("08:00");
        assertEquals("08:00", timetable.timeProperty().getValue(), "Time should be 08:00");
    }
}