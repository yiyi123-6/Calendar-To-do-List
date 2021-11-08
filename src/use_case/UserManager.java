package use_case;

import entities.user.CreationUser;
import entities.user.LogInable;
import entities.user.User;
import util.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * A use case class, representing a user manager to store, manipulate, modify the user instance(s).
 */

public class UserManager implements Serializable {
    private Map<UUID, User> users;

    public UserManager() {
        users = new HashMap<>();
    }

    /**
     * Add a LoginUser to users, with given sign-in information.
     *
     * @return whether the LoginUser is successfully added to users
     * @throws InvalidUsernameAndPasswordException when password and username are ill-formatted
     * @throws InvalidUsernameException when username is ill-formatted
     * @throws InvalidPasswordException when password is ill-formatted
     */
    public UUID addUser(User added) throws UserSignupException {
        if (!(added instanceof LogInable)){
            users.put(added.getUserID(), added);
            return added.getUserID();
        }

        String password = ((LogInable) added).getPassword();
        if (getPasswordStrength(password) <= 3) {
            throw new UserSignupException("Password is too weak");
        }

        if (!usernameAvailable(added.getUsername())){
            throw new UserSignupException("Username already taken");
        }

        if (getUserByEmail(((LogInable) added).getEmail()) != null){
            throw new UserSignupException("User already registered to email");
        }

        users.put(added.getUserID(), added);
        return added.getUserID();

    }

    public void removeUser(UUID userID){
        users.remove(userID);
    }

    /**
     * Login a login-able user, with given userID and password, if the user id exists.
     *
     * @param username username of the user
     * @param password password of the user
     * @return UUID of the user that was logged in.
     * @throws NullUserException if the userID given is not existed
     * @throws WrongPasswordException if password given is incorrect
     */
    public UUID userLogin(String username, String password) throws NullUserException, WrongPasswordException, BannedUserException {
        UUID retrieved = getUserByUsername(username);
        if (retrieved == null) {
            throw new NullUserException();
        }
        LocalDate banDate = users.get(retrieved).getBannedUntil();
        if (banDate != null && LocalDate.now().isBefore(banDate)){
            throw new BannedUserException();
        }

        LogInable user = (LogInable) users.get(retrieved);
        if(user.getPassword().equals(password) || password.equals(user.getTempPassword())){
            return ((User) user).getUserID();
        }

        else{
            throw new WrongPasswordException();
        }
    }

    /**
     * Based on the given email, if the user id exists, reset the password of this user account with a
     * temporary password.
     * @param email email of the user account
     * @return a Stirng that is the temporary password to this account.
     * @throws NullUserException if based on the email, the userID does not exist
     */

    public String recoverPassword(String email) throws NullUserException {

        UUID userID = getUserByEmail(email);

        if (userID == null){
            throw new NullUserException();
        }

        String tempPass = UUID.randomUUID().toString().replace("-","").substring(0,14);
        ((LogInable) users.get(userID)).setTempPassword(tempPass);
        return tempPass;

    }

    /**
     * Set the Temporary password to user account null if the given userid exits.
     * @param userID userID of this user
     * @return returns a boolean, false if the given userID does not exist; ture if the operation is done .
     */
    public boolean logOut(UUID userID){
        if (!(users.get(userID) instanceof LogInable)){
            return false;
        }

        ((LogInable) users.get(userID)).setTempPassword(null);
        return true;
    }

    /**
     * Get a list of creation UUIDs that the browsing user can access.
     * @param userID UUID of the "browsing user"
     * @return array of public creation UUIDs
     */
    public UUID[] browseAs(UUID userID){

        boolean adminStatus = false;
        if (isAdmin(userID)){
            adminStatus = true;
        }

        ArrayList<UUID> res = new ArrayList<>();
        for (UUID id : users.keySet()){
            if ((!id.equals(userID)) || adminStatus){
                res.addAll(Arrays.asList(getUserModuleIDs(id)));
            }
        }
        return res.toArray(new UUID[0]);
    }

    /**
     * Bans a user given the user applying the ban, and the user receiving the ban
     * @param adminID UUID of the admin user who is applying the ban
     * @param userID UUID of the user receiving the ban
     * @param duration duration of the ban in days
     */
    public boolean banUser(UUID adminID, UUID userID, int duration){

        if (!users.get(adminID).getUserType().equals("admin")){
            return false;
        }
        User receiver = users.get(userID);
        LocalDate banDate = LocalDate.now().plusDays(duration);

        receiver.setBanDate(banDate);
        return true;
    }

    /**
     * Get a list of a user's module ids, if the user id exists.
     *
     * @param userID userID of the user.User being requested for module ids access
     * @return ArrayList of module ids of the user
     */
    public UUID[] getUserModuleIDs(UUID userID) {
        User user = users.get(userID);

        if (user instanceof CreationUser){
            CreationUser cUser = (CreationUser) user;
            return cUser.getCreations();
        }

        return new UUID[0];
    }

    public boolean isAdmin(UUID userID){
        return users.get(userID).getUserType().equals("admin");
    }

    /**
     * Get a user's UUID from a creation they made.
     * @param creationID UUID of the creation being searched
     * @return UUID of the user that owns this creation.
     */
    public UUID getUserByCreation(UUID creationID){
        for (UUID i : users.keySet()){
            User user = users.get(i);

            if (user instanceof CreationUser){
                CreationUser cUser = (CreationUser) user;
                boolean contained = Arrays.asList(cUser.getCreations()).contains(creationID);

                if (contained){
                    return i;
                }
            }
        }
        return null;
    }

    /**
     * Add a module id to a user's collection of module ids.
     *
     * @param userID   userID of the user.User
     * @param CreationID Creation to be added to the given user's list of module ids
     */
    public void addCreationToUser(UUID userID, UUID CreationID) {
        User user = users.get(userID);

        if (user instanceof CreationUser){
            CreationUser cUser = (CreationUser) user;
            cUser.addCreation(CreationID);
        }
    }

    /**
     * Remove a module id from a user's collection of module ids.
     *
     * @param userID   userID of the user.User
     * @param creationID Creation to be added to the given user's list of module ids
     */
    public void removeCreationFromUser(UUID userID, UUID creationID){
        User user = users.get(userID);

        if (user instanceof CreationUser){
            CreationUser cUser = (CreationUser) user;
            cUser.removeCreation(creationID);
        }
    }

    /**
     * Return user type of the user corresponds to the given userID, if the user id exists as a key in
     * this.users.
     *
     * @param userID user id of the user
     * @return the user type of the user corresponds to the given userID
     */
    public String getUserType(UUID userID) {
        User user = users.get(userID);
        return user.getUserType();
    }

    /**
     * Get a user given their userID
     * @param userID userID of the user
     * @return user instance
     */
    public User getUser(UUID userID){
        return users.get(userID);
    }

    /**
     * Get the username of a user by UUID
     * @param userID UUID of the user
     * @return username of the user
     */
    public String getUsername(UUID userID){
        return users.get(userID).getUsername();
    }

    /**
     * Mutate the password of user corresponding to given userID, reset and password to
     * given password if the given password is valid and returns true; otherwise, no mutation
     * happens and returns false.
     *
     * @param userID userID of the user to be changed password
     * @param newPassword desired new password of user corresponding to userID
     * @return whether the password is changed
     */
    public void changePassword(UUID userID, String newPassword) throws UserSignupException {

        if (getPasswordStrength(newPassword) > 3 && users.get(userID) instanceof LogInable){
            ((LogInable) users.get(userID)).setPassword(newPassword);
            return;
        }
        throw new UserSignupException("Password is too weak");
    }

    /**
     * Get whether this username is available
     * @param username username of the user
     * @return true if the username is available
     */
    public boolean usernameAvailable(String username){
        boolean unique = true;
        for (User user : users.values()){
            if (user.getUsername().equals(username)) {
                unique = false;
                break;
            }
        }
        return unique;
    }

    /**
     * Using an integer to represent the strength of the password.
     * @param password the given password as a string.
     * @return an integer denoting the strength of the given password,
     * where <=3 - too weak, 4 - weak, 5 - med, >=6 - strong
     */
    public int getPasswordStrength(String password){
        int length = password.length() < 9 ? 3 : 4;
        int symbols = password.matches(".*[^a-zA-Z0-9 ]+.*") ? 1 : 0;
        int mixed = password.matches(".*[A-Z]+.*") && password.matches(".*[a-z]+.*")? 1 : 0;
        int num = password.matches(".*[0-9]+.*") ? 1 : 0;

        return length + symbols + mixed + num;
    }

    /**
     * get user ID of the user
     * @param username username of the user
     * @return UUID of the user
     * @throws NullUserException if the user could not be found
     */
    public UUID getUserIDByUsername(String username) throws NullUserException {
        for (User user : users.values()){
            if (user.getUsername().equals(username)){
                return user.getUserID();
            }
        }
        throw new NullUserException();
    }

    /**
     * Get messages owned by the user
     * @param userID UUID of the user
     * @return list of messages UUIDs owned by the user
     */
    public List<UUID> getUserMessageIDs(UUID userID) {
        return users.get(userID).getMessageInbox();
    }


    /**
     * Adds a message to the user's inbox
     * @param messageID UUID of the message
     * @param receiverID User receiving the message
     */
    public void addMessageToInbox(UUID messageID, UUID receiverID){
        User receiver = users.get(receiverID);
        receiver.addMessageIDtoInbox(messageID);
    }

    /**
     * Removes a message from the user's inbox
     * @param messageID UUID of the message
     * @param userID UUID of the user
     */
    public void deleteMessageFromInbox(UUID messageID, UUID userID){
        User receiver = users.get(userID);
        receiver.deleteMessageIDFromInbox(messageID);
    }

    /**
     * ValidationCheck to prevent users other than Admin User try to send messages to multiple receivers.
     * @param senderID UUID of the sender
     * @param receiverNames the string array of all receivers' names.
     * @throws MultiReceiverException if users other than admin user try to include more than
     * 1 receiver on the receiver name list.
     */
    public void NumReceiverValidationCheck(UUID senderID, String[] receiverNames) throws MultiReceiverException {
        String senderUserType = this.getUserType(senderID);
        if (!senderUserType.equalsIgnoreCase("admin") && receiverNames.length != 1) {
            throw new MultiReceiverException();
        }
    }


    private UUID getUserByUsername(String username){
        for (UUID userID : users.keySet()){
            if (users.get(userID).getUsername().equals(username)){
                return userID;
            }
        }
        return null;
    }

    private UUID getUserByEmail(String email){
        for (UUID userID : users.keySet()){
            User curr = users.get(userID);
            if ((curr instanceof LogInable && ((LogInable) curr).getEmail().equals(email))){
                return userID;
            }
        }
        return null;
    }
}





