package entities.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/**
 * Represents a user that is able to make, delete and retrieve creations.
 */
public abstract class CreationUser extends User{

    private List<UUID> creations = new ArrayList<>();

    /**
     * Initialize a User given their username.
     *
     * @param username username of the user
     */
    public CreationUser(String username) {
        super(username);
    }

    /**
     * Adds a creation to this User's collection of modules.
     *
     * @param creationID UUID of the module to be added
     */
    public void addCreation(UUID creationID) {
        if (!creations.contains(creationID)) {
            creations.add(creationID);
        }
    }

    /**
     * Removes a creation from this user's collection of modules
     *
     * @param creationID UUID of the module to be removed
     */
    public void removeCreation(UUID creationID) {
        creations.remove(creationID);
    }

    /**
     * Get a list of all module ids belonging to this user.
     *
     * @return ArrayList of module UUIDs contained in this user.
     */
    public UUID[] getCreations() {
        return creations.toArray(new UUID[0]);
    }
}