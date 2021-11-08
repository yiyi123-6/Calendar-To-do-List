package entities.creation;

import java.io.Serializable;
import java.util.UUID;

/**
 * A class representing an event created by a user and stored in an EventModule.
 */
public abstract class Event implements Serializable {

    private String name;
    private String note;
    private boolean privacy; // true means private; false means public
    private UUID eventID = UUID.randomUUID();

    /**
     * Initializes a new Event, given the name and description of the event. Automatically set to private.
     *
     * @param name name of the event
     * @param note description of event
     */
    public Event(String name, String note) {
        this.name = name;
        this.note = note;
        this.privacy = true;
    }

    /**
     * Initializes a new Event, given the name, privacy and description of the event.
     * @param name name of the event
     * @param note description of event
     * @param privacy privacy of the event
     */
    public Event(String name, String note, boolean privacy) {
        this.name = name;
        this.note = note;
        this.privacy = privacy;
    }

    // ============== Setters =================

    /**
     * Sets a new name for this Event
     *
     * @param name new name to be set for this Event
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets a new note for this Event
     *
     * @param note new note for this Event.
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Sets the privacy status of this Event.
     * @param privacy new privacy status of this Event
     */
    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }


    // ============== Getters =================

    /**
     * Get the name of this Event
     *
     * @return name of the Event
     */
    public String getName() {
        return name;
    }

    /**
     * Get the note describing this Event
     *
     * @return description of this Event
     */
    public String getNote() {
        return note;
    }

    /**
     * Get the ID of this Event
     *
     * @return UUID of this Event
     */
    public UUID getEventID() {
        return eventID;
    }

    /**
     * Get the privacy status of this Event
     *
     * @return True if the Event is private, False if public.
     */
    public boolean getPrivacy() {
        return privacy;
    }

    // ============== Other methods =================

    /**
     * Return a string representation of this event
     * @return String representation of the event
     */
    public abstract String toString();
}