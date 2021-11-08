package use_case;

import entities.creation.Event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A use case class responsible for storing events and modifying events.
 */

public class EventManager implements Serializable {

    private Map<UUID, Event> events = new HashMap<>();

    public UUID addEvent(Event addedEvent){
        events.put(addedEvent.getEventID(), addedEvent);
        return addedEvent.getEventID();
    }

    /**
     * Remove an event from this EventManager
     * @param eventID UUID of the event to be removed.
     */
    public void removeEvent(UUID eventID){
        events.remove(eventID);
    }

    // ============= Getters ==============

    /**
     * Get info stored in an event.
     * @param event Event instance to retrieve info from
     * @return array containing Event's UUID, name, description, and privacy (in that order)
     */
    public ArrayList<Object> getEventInfo(Event event){
        ArrayList<Object> listOfEvents = new ArrayList<>();
        listOfEvents.add(event.getEventID());
        listOfEvents.add(event.getName());
        listOfEvents.add(event.getNote());
        listOfEvents.add(event.getPrivacy());
        return listOfEvents;
    }

    /**
     * Get an event by its UUID
     * @param eventID UUID of the event.
     * @return Event instance matching the given UUID.
     */
    public Event getEvent(UUID eventID){
        return events.get(eventID);
    }

}


