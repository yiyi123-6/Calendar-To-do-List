package entities.creation;

import java.io.Serializable;

/**
 * A class representing an entry in a To-do list.
 */
public class TodoListEvent extends Event implements Serializable {

    private int urgency;

    /**
     * Initializes this TodoListEvent, given the name, description and urgency of this event.
     * (auto-set the privacy to private)
     *
     * @param name name of the TodoListEvent
     * @param note description of this TodoListEvent
     * @param urgency how urgent the event is
     */
    public TodoListEvent(String name, String note, int urgency){
        super(name, note);
        this.urgency = urgency;
    }

    /**
     * Initializes this creation.TodoListEvent, given the name, description, privacy ,and urgency of this event.
     *
     * @param name name of the TodoListEvent
     * @param note description of this TodoListEvent
     * @param urgency how urgent the event is
     * @param privacy false for public, true for private
     */
    public TodoListEvent(String name, String note, boolean privacy, int urgency){
        super(name, note, privacy);
        this.urgency = urgency;
    }

    /**
     * String representation of this TodoList event.
     * @return String representation of this TodoList event.
     */
    @Override
    public String toString() {
        return getName() + " - \"" + getNote() + "\"| Urgency: " + getUrgency();
    }

    // ============== Getters =================

    /**
     * Get the urgency of this creation.TodoListEvent.
     * @return urgency of creation.TodoListEvent
     */
    public int getUrgency(){
        return urgency;
    }

}
