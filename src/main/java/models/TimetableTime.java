package models;

import javafx.beans.property.SimpleStringProperty;

//Class to add timestamps to timetable
public class TimetableTime {
    private final SimpleStringProperty time;

    public TimetableTime(String time) {
        this.time = new SimpleStringProperty(time);
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }




}
