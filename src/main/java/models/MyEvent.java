package models;

import java.time.LocalDateTime;
import java.time.LocalTime;

public abstract class MyEvent {
    public String getTitle() {
        return "Unknown Event";
    }
    public abstract LocalDateTime getDate();
    public LocalTime getEventStartTime() {
        return getDate().toLocalTime();
    }
    //TODO abstract String getEndTime()
    public String getDescription() {
        return "";
    }
}
