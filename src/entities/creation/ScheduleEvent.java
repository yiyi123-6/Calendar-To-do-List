package entities.creation;

import java.io.Serializable;
import java.util.Date;

/**
 * A class that represents an event that can be scheduled in the future.
 */
public class ScheduleEvent extends Event implements Serializable {

    private Date date;

    /**
     * Initializes this ScheduleEvent, given name, description, privacy, date of this event.
     *
     * @param name name of the event
     * @param note description of this event
     * @param privacy privacy of this event
     * @param date date of this event
     */
    public ScheduleEvent(String name, String note, boolean privacy, Date date) {
        super(name, note, privacy);
        this.date = date;
    }

    /**
     * String representation of this ScheduleEvent
     * @return String representation of this ScheduleEvent
     */
    @Override
    public String toString() {
        return getName() + " - \"" + getNote() + "\"| Scheduled: " + getDate().toString();
    }

    // ============== Getters =================

    /**
     * Get the date of this ScheduleEvent
     * @return date of the ScheduleEvent
     */
    public Date getDate() {
        return date;
    }



}
