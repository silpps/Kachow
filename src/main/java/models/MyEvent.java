package models;

import java.time.LocalDateTime;
import java.time.LocalTime;

public abstract class MyEvent {
    public String getTitle() {
        return "Unknown Event";
    }
    public abstract LocalDateTime getDate();
    public LocalTime getLTStartTime() {
        return getDate().toLocalTime();
    }
    //abstract String getEndTime();
    public String getDescription() {
        return "";
    }
}
