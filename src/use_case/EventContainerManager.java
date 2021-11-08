package use_case;

import entities.creation.EventContainer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 * An use case class responsible for manipulating and modifying EventContainers.
 */

public class EventContainerManager implements Serializable {

    private HashMap<UUID, EventContainer> eventContainers = new HashMap<>();

    /**
     * Add an event container to eventContainers, and return the event container ID of the newly added event container.
     * @param name name of the eventContainer to be added
     * @param type type of the eventContainer to be added
     * @return the event container ID of the newly added event container.
     */
    public UUID addEventContainer(String name, String type) {
        EventContainer newEventContainer = new EventContainer(name, type);
        eventContainers.put(newEventContainer.getModuleID(), newEventContainer);
        return newEventContainer.getModuleID();
    }

    public void removeEventContainer(UUID containerID){
        eventContainers.remove(containerID);
    }

    /**
     * Add an event to an EventContainer given both their UUIDs.
     * @param containerID UUID of the EventContainer.
     * @param eventID UUID of the event.
     */
    public void addEventToContainer(UUID containerID, UUID eventID){
        eventContainers.get(containerID).addEvent(eventID);
    }

    /**
     * Add multiple Events to an EventContainer given their UUIDs.
     * @param containerID UUID of the EventContainer.
     * @param events array of Event UUIDs to be added to the EventContainer.
     */
    public void addEventsToContainer(UUID containerID, UUID[] events){
        for (UUID u : events){
            addEventToContainer(containerID, u);
        }
    }

    /**
     * Remove an Event from an EventContainer.
     * @param containerID the UUID of the container to be mutated.
     * @param eventID UUID of the event to be removed from the container.
     */
    public void removeEventFromModule(UUID containerID, UUID eventID) {
        eventContainers.get(containerID).removeEvent(eventID);
    }

    // ============== Getters =================

    /**
     * Get an EventContainer's name given its UUID.
     * @param creationID UUID of the container.
     * @return String representation of the EventContainer.
     */
    public String getContainerName(UUID creationID) {
        return eventContainers.get(creationID).toString();
    }

    /**
     * Get an EventContainer given its UUID
     * @param containerID UUID of the EventContainer
     * @return EventContainer instance matching the UUID given
     */
    public EventContainer getEventContainer(UUID containerID){
        return eventContainers.get(containerID);
    }

    /**
     * Get the events stored in an EventContainer
     * @param containerID UUID of the EventContainer
     * @return array of event UUIDs contained in this EventContainer.
     */
    public UUID[] getContainedEvents(UUID containerID){
        return eventContainers.get(containerID).getEventCollection();
    }

    /**
     * Get the type of an EventContainer
     * @param containerID UUID of the EventContainer
     * @return String specifying the type of Events that the EventContainer can hold.
     */
    public String getContainerType(UUID containerID){
        return eventContainers.get(containerID).getContainerType();
    }

    /**
     * Get the privacy of an EventContainer
     * @param containerID UUID of the EventContainer
     * @return false if public, true if private
     */
    public boolean getPrivacy(UUID containerID){
        return eventContainers.get(containerID).getPrivacy();
    }

    // ============== Setters =================

    /**
     * Set privacy of an EventContainer given its UUID, and its new privacy.
     * @param containerID UUID of the EventContainer
     * @param newPrivacy false for public, true for private.
     */
    public void setPrivacy(UUID containerID, boolean newPrivacy){
        eventContainers.get(containerID).setPrivacy(newPrivacy);
    }

    /**
     * Set the name of an EventContainer given its UUID and new name.
     * @param containerID UUID of the EventContainer.
     * @param newName New name of the EventContainer
     */
    public void setEventContainerName(UUID containerID, String newName){
        eventContainers.get(containerID).setModuleName(newName);
    }
}
