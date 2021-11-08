package entities.creation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A class responsible for storing events. These are the "creations" a user makes.
 */
public class EventContainer implements Serializable {

    private UUID moduleID = UUID.randomUUID();
    private ArrayList<UUID> eventCollection = new ArrayList<UUID>();
    private String moduleName;
    private boolean privacy;
    private String containerType;

    /**
     * Initializes this EventModule, with given moduleName and moduleType, and privacyStatus set to false.
     * @param moduleName Name of this module
     * @param containerType String which identifies the type of events this EventContainer holds
     */
    public EventContainer(String moduleName, String containerType){
        this.moduleName = moduleName;
        this.containerType = containerType;
        privacy = false;
    }

    /**
     * Add an event to this EventModule
     * @param eventID UUID of the event to be added
     */
    public void addEvent(UUID eventID){
        if (!eventCollection.contains(eventID)){
            eventCollection.add(eventID);
        }
    }

    /**
     * Remove an event from this EventModule.
     * @param eventID UUID of the event to be removed.
     */
    public void removeEvent(UUID eventID){
        if (eventCollection.contains(eventID)){
            eventCollection.remove(eventID);
        }
    }

    /**
     * Return a string representation of this EventContainer.
     * @return String representation of EventContainer.
     */
    @Override
    public String toString(){
        return moduleName;
    }

    // ============== Getters =================

    /**
     * Get the id of this EventModule
     * @return UUID of the EventModule
     */
    public UUID getModuleID() {
        return moduleID;
    }

    /**
     * Get all events stored in this EventModule
     * @return ArrayList of all UUIDs in the EventModule
     */
    public UUID[] getEventCollection() {
        // copy to avoid aliasing
        return eventCollection.toArray(new UUID[0]);
    }

    /**
     * Return whether this event container is private or public
     * @return true for private, false if public.
     */
    public boolean getPrivacy(){
        return privacy;
    }

    /**
     * Get the container type of this EventContainer.
     * @return String identifying the type of events this EventContainer can hold.
     */
    public String getContainerType() {
        return containerType;
    }

    // ============== Setters =================

    /**
     * Set the name of this EventModule
     * @param moduleName new name of the EventModule
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * Set the privacy of this EventModule
     * @param newPrivacy The new privacy status of this EventModule. True for private, false for public
     */
    public void setPrivacy(boolean newPrivacy){
        privacy = newPrivacy;
    }
}
