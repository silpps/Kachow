package models;

import javafx.beans.property.SimpleStringProperty;

//Class to add timestamps to timetable
public class Timetable_v1 {
    private final SimpleStringProperty time;

    public Timetable_v1(String time) {
        this.time = new SimpleStringProperty(time);
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }




}
