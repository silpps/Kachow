package models;

import javafx.beans.property.SimpleStringProperty;

public class Timetable_v2 {
    private final SimpleStringProperty time;
    private final SimpleStringProperty monday;
    private final SimpleStringProperty tuesday;
    private final SimpleStringProperty wednesday;
    private final SimpleStringProperty thursday;
    private final SimpleStringProperty friday;
    private final SimpleStringProperty saturday;
    private final SimpleStringProperty sunday;

    public Timetable_v2(String time) {
        this.time = new SimpleStringProperty(time);
        this.monday = new SimpleStringProperty("");
        this.tuesday = new SimpleStringProperty("");
        this.wednesday = new SimpleStringProperty("");
        this.thursday = new SimpleStringProperty("");
        this.friday = new SimpleStringProperty("");
        this.saturday = new SimpleStringProperty("");
        this.sunday = new SimpleStringProperty("");
    }

    // Getters and setters for all properties
    public String getTime() { return time.get(); }
    public void setTime(String time) { this.time.set(time); }
    public SimpleStringProperty timeProperty() { return time; }

    public String getMonday() { return monday.get(); }
    public void setMonday(String monday) { this.monday.set(monday); }
    public SimpleStringProperty mondayProperty() { return monday; }

    public String getTuesday() { return tuesday.get(); }
    public void setTuesday(String tuesday) { this.tuesday.set(tuesday); }
    public SimpleStringProperty tuesdayProperty() { return tuesday; }

    public String getWednesday() { return wednesday.get(); }
    public void setWednesday(String wednesday) { this.wednesday.set(wednesday); }
    public SimpleStringProperty wednesdayProperty() { return wednesday; }

    public String getThursday() { return thursday.get(); }
    public void setThursday(String thursday) { this.thursday.set(thursday); }
    public SimpleStringProperty thursdayProperty() { return thursday; }

    public String getFriday() { return friday.get(); }
    public void setFriday(String friday) { this.friday.set(friday); }
    public SimpleStringProperty fridayProperty() { return friday; }

    public String getSaturday() { return saturday.get(); }
    public void setSaturday(String saturday) { this.saturday.set(saturday); }
    public SimpleStringProperty saturdayProperty() { return saturday; }

    public String getSunday() { return sunday.get(); }
    public void setSunday(String sunday) { this.sunday.set(sunday); }
    public SimpleStringProperty sundayProperty() { return sunday; }
}