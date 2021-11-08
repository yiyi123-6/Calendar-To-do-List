package entities.user;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class which represents a User in the program.
 */
public abstract class User implements Serializable {

    private UUID userID = UUID.randomUUID();
    private String username;
    private LocalDate banDate;
    private List<UUID> messageInbox;

    /**
     * Initialize a User given their username.
     * @param username username of the user
     */
    public User(String username){
        this.username = username;
        messageInbox = new ArrayList<>();
    }

    @Override
    public String toString(){
        return username;
    }

    // ============== Getters =================
    /**
     * Gets username of this User instance
     * @return username of the user
     */
    public String getUsername(){
        return username;
    }

    /**
     * Gets user ID of this User instance
     * @return user ID of the User
     */
    public UUID getUserID(){
        return userID;
    }

    /**
     * Get the user type of this user.
     * @return the user type of this user
     */
    public String getUserType() {
        return "trial";
    }

    /**
     * Get date that user is banned until.
     * @return date that user is banned until. null if user has not been banned.
     */
    public LocalDate getBannedUntil(){
        return banDate;
    }

    /**
     * Get the inbox of this user
     * @return List of message UUIDs
     */
    public List<UUID> getMessageInbox() {
        return messageInbox;
    }

    // ============== Setters =================

    /**
     * Sets this User's username to the given username
     * @param newUsername new username of the User
     */
    public void setUsername(String newUsername){
        username = newUsername;
    }

    /**
     * Sets the day that the user is banned until.
     * @param newDate the date when the user's ban gets lifted.
     */
    public void setBanDate(LocalDate newDate){
        banDate = newDate;
    }


    // ================= Add/Delete message from inbox =================//

    /**
     * Add the given messageID to the list of UUID messageInbox.
     * @param messageID UUID
     */
    public void addMessageIDtoInbox(UUID messageID) {
        this.messageInbox.add(messageID);
    }

    /**
     * Remove the given messageID from the list of UUID messageInbox.
     * @param messageID UUID
     */
    public void deleteMessageIDFromInbox(UUID messageID){
        this.messageInbox.remove(messageID);
    }
}
