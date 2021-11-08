package controller;

import gateway.CILoadable;
import use_case.factory.UserFactory;
import use_case.UserManager;
import util.*;

import java.util.UUID;

/**
 * A controller class responsible for reacting to user input related to user login.
 */
public class UserActivityController implements CILoadable {

    private UserManager um;
    private UUID loggedInUser;
    private UserFactory uf = new UserFactory();

    /**
     * Initializes this UserActivityController.
     */
    public UserActivityController(){
        this.um = new UserManager();
    }

    /**
     * Login a login-able user, by reading inputs from the user at the keyboard.
     * Return the userID of the successfully logged in user; null otherwise (where login activity fails for
     * some reason(s)).
     *
     * @return the UUID of the user that successfully login, null if login activity fails for some reason(s).
     */
    public UUID userLogin(String username, String password) throws NullUserException, WrongPasswordException, BannedUserException {
        UUID tempUserID;

        tempUserID = um.userLogin(username, password);
        loggedInUser = tempUserID;
        return tempUserID;
    }

    /**
     * Sign up a login-able user (regular or admin), by reading inputs from the user at the keyboard.
     * Return the userID of the successfully signed up user; null otherwise
     *
     * @return the userID of the user that successfully sign up, null otherwise.
     */
    public boolean signUp(String username, String password, String email, String usertype) throws UserSignupException {

        String usernameCheck = usernameIsValid(username);
        if (!usernameCheck.equals("")){
            throw new UserSignupException("Username invalid: " + usernameCheck);
        }

        if (usertype.equals("Trial")){
            loggedInUser = um.addUser(uf.getUser(username, usertype));
            return false;
        }

        String pwCheck = passwordIsValid(password);
        if (!pwCheck.equals("")){
            throw new UserSignupException("Password invalid: " + pwCheck);
        }

        String emailCheck = emailIsValid(email);
        if (!emailCheck.equals("")){
            throw new UserSignupException("Email invalid: " + emailCheck);
        }

        um.addUser(uf.getUser(username, password, email, usertype));
        return true;
    }

    public String recoverPassword(String email) throws NullUserException {
        return um.recoverPassword(email);
    }

    /**
     * Change the password to the given newPassword.
     * Return the userID of the successfully signed up user; null otherwise
     * @param newPassword a string, the new password
     * @throws UserSignupException if the given newPassword is not valid.
     * @throws NullUserException if the user does not exist.
     */
    public void changePassword(String newPassword) throws NullUserException, UserSignupException {

        String pwCheck = passwordIsValid(newPassword);
        if (!pwCheck.equals("")){
            throw new UserSignupException("Password invalid: " + pwCheck);
        }
        if (loggedInUser != null){
            um.changePassword(loggedInUser, newPassword);
        }
        else{
            throw new NullUserException();
        }
    }

    /**
     * Get the currently logged in user
     * @return UUID of the logged in user
     */
    public UUID getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Ban a user given their username and ban duration
     * @param username username of the user to be banned
     * @param duration String representing the number of days the user is banned
     * @return true if the user was banned
     * @throws NullUserException If the user does not exist
     * @throws NumberFormatException if the duration cannot be converted to an integer
     */
    public boolean banUser(String username, String duration) throws NullUserException, NumberFormatException{

        int len = Integer.parseInt(duration);

        return um.banUser(loggedInUser, um.getUserIDByUsername(username), len);
    }

    /**
     * Log out the currently logged in user
     * @return true if the logged out user is registered.
     */
    public boolean logOut(){
        boolean res = um.logOut(loggedInUser);
        loggedInUser = null;
        return res;
    }

    /**
     * Get info contained in the logged in user.
     * @return array containing user information:
     * 0 - username
     * 1 - user type
     */
    public String[] getProfile(){
        String[] res = new String[]{um.getUsername(loggedInUser), um.getUserType(loggedInUser)};

        if (getLoggedInUser() == null){
            return new String[0];
        }
        return res;
    }

    /**
     * Get the displayed username of the user given their UUID.
     * @param viewer UUID of the user viewing the username
     * @param user UUID of the user being viewed
     * @return displayed username from the viewer's perspective.
     */
    public String getDisplayedUsername(UUID viewer, UUID user){
        if (um.isAdmin(viewer)){
            return um.getUsername(user);
        }
        return um.getUser(user).toString();
    }

    /**
     * Get stored username of the user (not as it appears on screen)
     * @param user UUID of the user
     * @return username of the user
     */
    public String getUsername(UUID user){
        return um.getUsername(user);
    }

    /**
     * Loads this UserActivityController given an Array of managers.
     * @param params Array of managers
     */
    @Override
    public void load(Object[] params) {
        this.um = (UserManager) params[0];
    }

    // =========== Helper methods ===========
    private boolean UserTypeInvalid(String inputUsertype) {
        try {
            if (!inputUsertype.equalsIgnoreCase("regular") && !inputUsertype.equalsIgnoreCase("admin")) {
                throw new InvalidUserTypeException();
            }
        } catch (InvalidUserTypeException e){
            return true;
        }
        return false;
    }

    /**
     * Checks whether the given password is valid.
     * @param password the given password.
     * @return "" if the password is valid or a string describing why the given password is invalid.
     */
    private String passwordIsValid(String password) {

        boolean lenCheck = 6 <= password.length() && password.length() <= 25;

        if (!lenCheck){
            return "password must be at least between 6-25 characters";
        }
        return "";
    }

    /**
     * Checks whether the given email is valid.
     * @param email the given email.
     * @return "" if the email is valid or a string describing why the given email is invalid.
     */
    private String emailIsValid(String email){
        String res = "";

        if (!email.matches("[a-z0-9]+([_.-][a-z0-9]+)*@[a-z0-9-]+[.][a-z]{2,4}")){
            res = "Email not in correct format";
        }

        else if (email.length() > 320){
            res = "Email too long";
        }

        return res;
    }

    /**
     * Checks whether the given username is valid.
     * @param username the given username
     * @return "" if the username is valid or a string describing why the given username is invalid.
     */
    private String usernameIsValid(String username) {
        boolean unique = um.usernameAvailable(username);

        boolean lenCheck = 3 <= username.length() && username.length() <= 15;
        if (!lenCheck){
            return "Username must be between 3-15 characters long";
        }
        return "";
    }

}

