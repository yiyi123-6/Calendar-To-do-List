package controller;

import java.util.Map;
import java.util.UUID;

/**
 * Implementing this interface allows the class to be be treated as
 * a "creation", and can perform all the behaviours of a creation: viewing, browsing, editing, creation
 */
public interface CreationControllable {

    /**
     * Constructs a creation given an array of inputs and path to a template.
     * @param params Array of raw user inputs
     * @param path path of a template instance
     * @return UUID of the creation that was constructed
     * @throws Exception thrown if something goes wrong in constructing the creation.
     */
    UUID constructCreation(String[] params, String path, UUID creator) throws Exception;

    /**
     * Edits a creation given its id.
     * @param creationID UUID of the creation.
     */
    void editCreation(UUID creationID, String action, String input) throws Exception;

    /**
     * Deletes a creation given its id.
     * @param creationID UUID of the creation
     */
    void deleteCreation(UUID creationID);

    /**
     * Set the privacy of a creation, given its id and new privacy status.
     * @param creationID UUID of the creation.
     * @param newPrivacy new privacy of the creation.
     */
    void setPrivacy(UUID creationID, boolean newPrivacy);

    /**
     * Get all public creations from an array of creations.
     * @param viewer UUID of the user browsing creations.
     * @return array containing creations accessible to the user.
     */
    UUID[] getBrowsableCreations(UUID viewer);

    /**
     * Get an array of creations owned by a user
     * @param userID UUID of the user
     * @return Array of creation UUIDs owned by the user
     */
    UUID[] getOwnCreations(UUID userID);

    /**
     * Get full information about a creation given its UUID.
     * @param creation UUID of the creation
     * @param viewer UUID of the user viewing the creation
     * @return Array containing information of creation. [name, contents]
     */
    Map<String, String> viewCreation(UUID creation, UUID viewer);

    /**
     * Get a summary of creations information given its UUID and viewing user's UUID.
     * @param creation UUID of the creation
     * @param viewer UUID of the viewer
     * @return String showing a summary of creation information
     */
    String[] viewCreationSummary(UUID creation, UUID viewer);

    /**
     * Get the type of creation given its UUID
     * @param creationID UUID of creation
     * @return String representation of creation type
     */
    String getCreationType(UUID creationID);

    /**
     * Get a map containing all the templates that can be used to make a creation
     * @return Map where each element maps a template's path to its name.
     */
    Map<String, String> getTemplateSelection();
    /**
     * Get whether a creation is contained in this creation controllable
     * @param creationID UUID of the creation.
     * @return true if the creation is contained in the creation controllable
     */
    boolean containsCreation(UUID creationID);
}
