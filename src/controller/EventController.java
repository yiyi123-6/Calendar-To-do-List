package controller;

import gateway.CILoadable;
import use_case.TemplateManager;
import use_case.EventContainerManager;
import use_case.factory.EventFactory;
import use_case.EventManager;
import use_case.UserManager;

import java.text.ParseException;
import java.util.*;

/**
 * Class responsible for reacting to user input related to Events and
 * coordinating changes made to EventManager and EventContainerManager.
 */
public class EventController implements CreationControllable, CILoadable {
    private EventManager em;
    private EventContainerManager ecm;
    private EventFactory ef;
    private UserManager um;
    private TemplateManager tm;

    /**
     * Initialize this EventController
     */
    public EventController(){
        this.em = new EventManager();
        this.ecm = new EventContainerManager();
        this.ef = new EventFactory();
        this.um = new UserManager();
        this.tm = new TemplateManager();
    }

    /**
     * Construct an EventContainer given an array of parameters, and the path to the template used to make the creation.
     * @param params User input to template prompts.
     * @param path Path of the template instance used to collect these prompts.
     * @param creator UUID of the user who created this creation
     * @return UUID of the EventContainer instance created.
     */
    @Override
    public UUID constructCreation(String[] params, String path, UUID creator) throws ParseException {
        // params set up like this: [EC name, event 1, event 2, event 3....]
        // event format: name, notation, privacy, .. (comma separated)
        if (params.length < 2){
            throw new ParseException("Not enough arguments", 0);
        }

        if (um.getUserType(creator).equals("admin")){
            throw new ParseException("Admin users cannot make creations", 0);
        }

        String creationType;
        if (path.equals("todo placeholder")){
            creationType = "Todo";
        }
        else if (path.equals("schedule placeholder")){
            creationType = "Schedule";
        }

        else if (path.equals("tagged placeholder")){
            creationType = "Tagged";
        }
        else{
            throw new ParseException("invalid creation type chosen", 0);
        }

        // parse rest of params (events)
        ArrayList<UUID> events = new ArrayList<>();
        for (int i = 1; i < params.length; i++){
            // update event manager
            UUID addedEvent = em.addEvent(ef.getEvent(params[i], creationType));
            events.add(addedEvent);
        }
        // update event container manager
        UUID newContainer = ecm.addEventContainer(params[0], creationType);
        ecm.addEventsToContainer(newContainer, events.toArray(new UUID[0]));
        um.addCreationToUser(creator, newContainer);
        return newContainer;
    }

    /**
     * Edit the selected EventContainer (add/delete events, rename EventContainer).
     * @param creationID UUID of the EventContainer.
     * @param action Selected action of the user.
     */
    @Override
    public void editCreation(UUID creationID, String action, String input) throws ParseException{

        // add event
        if (action.matches("Todo|Schedule|Tagged")){
            UUID addedEvent = em.addEvent(ef.getEvent(input, action));
            ecm.addEventToContainer(creationID, addedEvent);
        }

        // remove event
        else if (action.equals("3")){
            UUID eventID = UUID.fromString(input);
            ecm.removeEventFromModule(creationID, eventID);
            em.removeEvent(eventID);
        }

        // change name of container
        else if (action.equals("4")){
            ecm.setEventContainerName(creationID, input);
        }

        // change Container privacy
        else if (action.equals("5")){
            if (input.equals("true")){
                ecm.setPrivacy(creationID, true);
            }

            else if (input.equals("false")){
                ecm.setPrivacy(creationID, false);
            }
        }
    }

    /**
     * Deletes a creation given its id.
     * @param creationID UUID of the creation
     */
    @Override
    public void deleteCreation(UUID creationID) {
        UUID[] events = ecm.getContainedEvents(creationID);
        ecm.removeEventContainer(creationID);
        um.removeCreationFromUser(um.getUserByCreation(creationID), creationID);

        for(UUID eventID : events){
            em.removeEvent(eventID);
        }
    }

    /**
     * Set privacy of the selected EventContainer.
     * @param creationID UUID of the creation.
     * @param newPrivacy new privacy of the creation.
     */
    @Override
    public void setPrivacy(UUID creationID, boolean newPrivacy) {
        ecm.setPrivacy(creationID, newPrivacy);

        if (newPrivacy){
            System.out.println(ecm.getContainerName(creationID) + " now private");
        }
        else{
            System.out.println(ecm.getContainerName(creationID) + " now public");
        }
    }

    /**
     * Get all public creations from an array of creations.
     * @param viewer UUID of the user browsing creations.
     * @return array containing creations accessible to the user.
     */
    @Override
    public UUID[] getBrowsableCreations(UUID viewer) {
        ArrayList<UUID> res = new ArrayList<>();
        UUID[] creations = um.browseAs(viewer);

        if (um.isAdmin(viewer)){
            return creations;
        }

        // filter private creations
        for (UUID cid : creations){
            if (!ecm.getPrivacy(cid)){
                res.add(cid);
            }
        }
        return res.toArray(new UUID[0]);
    }

    /**
     * Get an array of creations owned by a user
     * @param userID UUID of the user
     * @return Array of creation UUIDs owned by the user
     */
    @Override
    public UUID[] getOwnCreations(UUID userID) {
        return um.getUserModuleIDs(userID);
    }


    /**
     * Get a string representation of the selected EventContainer.
     * @param creation UUID of the creation
     * @param viewer UUID of the user viewing the creation
     * @return Array containing string representation of all events in the event container.
     */
    @Override
    public Map<String, String> viewCreation(UUID creation, UUID viewer) {
        Map<String, String> res = new HashMap<>();

        UUID[] events = ecm.getContainedEvents(creation);
        boolean viewSelf = um.getUserByCreation(creation).equals(viewer);
        boolean isAdmin = um.isAdmin(viewer);

        for (UUID eid : events){

            boolean isPrivate = ((boolean) em.getEventInfo(em.getEvent(eid)).get(3));

            if (!isPrivate || isAdmin || viewSelf){
                res.put(eid.toString(), em.getEvent(eid).toString());
            }
        }
        return res;
    }
    /**
     * Get a summary of creation's information given its UUID and viewing user's UUID.
     * @param creation UUID of the creation
     * @param viewer UUID of the viewer
     * @return Array showing a summary of creation information:
     * 0 - creation name
     * 1 - Event container type
     * 2 - creation author
     */
    @Override
    public String[] viewCreationSummary(UUID creation, UUID viewer) {
        String[] res = new String[3];
        res[0] = ecm.getContainerName(creation);
        res[1] = ecm.getContainerType(creation);

        UUID owner = um.getUserByCreation(creation);

        if (um.getUserType(viewer).equals("admin") || viewer.equals(owner)){
            res[2] = um.getUsername(um.getUserByCreation(creation));
        }

        else{
            res[2] = um.getUser(um.getUserByCreation(creation)).toString();
        }

        return res;
    }

    /**
     * Get the type of creation given its UUID
     * @param creationID UUID of creation
     * @return String representation of creation type
     */
    @Override
    public String getCreationType(UUID creationID) {
        return ecm.getContainerType(creationID);
    }

    @Override
    public Map<String, String> getTemplateSelection() {
        Map<String, String> res = new HashMap<>();
        String[] templates = tm.getAllTemplates();

        for (String p : templates){
            res.put(p, tm.getTemplateName(p));
        }
        return res;
    }

    /**
     * Get whether a creation is contained in this creation controllable
     * @param creationID UUID of the creation.
     * @return true if the creation is contained in the creation controllable
     */
    @Override
    public boolean containsCreation(UUID creationID) {
        return ecm.getEventContainer(creationID) != null;
    }

    @Override
    public void load(Object[] params) {
        this.um = (UserManager) params[0];
        this.ecm = (EventContainerManager) params[1];
        this.em = (EventManager) params[2];
        this.tm = (TemplateManager) params[3];
    }
}
