package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * This abstract class represents a generic event with a title, date, and description.
 */
public abstract class MyEvent {
    /**
     * Returns the title of the event.
     * Subclasses can override this method to provide a specific title.
     *
     * @return the title of the event, or "Unknown Event" if not overridden
     */
    public String getTitle() {
        return "Unknown Event";
    }

    /**
     * Abstract method to retrieve the date of the event.
     * Subclasses must implement this method to provide the event's date.
     *
     * @return the date of the event
     */
    public abstract LocalDateTime getDate();

    /**
     * Returns the start time of the event as a {@link LocalTime}.
     * This is derived from the event's date.
     *
     * @return the start time of the event
     */
    public LocalTime getEventStartTime() {
        return getDate().toLocalTime();
    }

    /**
     * Gets the end time of the event
     * @return the end time of the event
     */
    public LocalDateTime getEndTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 0));
    }

    /**
     * Returns the description of the event.
     * Subclasses can override this method to provide a specific description.
     *
     * @return the description of the event, or an empty string if not overridden
     */
    public String getDescription() {
        return "";
    }
}
