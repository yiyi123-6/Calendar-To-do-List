package presenter;

import controller.UserActivityController;
import util.BannedUserException;
import util.NullUserException;
import util.UserSignupException;
import util.WrongPasswordException;

/**
 * Class responsible for showing information related to user activity
 */
public class UserActivityPresenter {

    private UserStartView startView;
    private UserProfileView profileView;
    private UserActivityController uac;

    /**
     * Initializes this UserActivityPresenter
     */
    public UserActivityPresenter(UserStartView startView, UserProfileView profileView, UserActivityController uac){
        this.startView = startView;
        this.profileView = profileView;
        this.uac = uac;
    }

    /**
     * Signs up the user and updates the view
     * @param username username of the user
     * @param password password of the user
     * @param email email of the user
     * @param usertype user type of the user
     * @return true if the user was registered.
     */
    public boolean Signup(String username, String password, String email, String usertype){
        try {
            return uac.signUp(username, password, email, usertype);
        } catch (UserSignupException e) {
            startView.updateSignUpView(e.getMessage());
            return false;
        }
    }

    /**
     * Starts a trial and updates the view
     * @param username username of the trial user
     * @return true if the trial was successfully started
     */
    public boolean startTrial(String username){
        try {
            uac.signUp(username, "", "", "Trial");
            return true;
        } catch (UserSignupException e) {
            startView.updateSignUpView(e.getMessage());
        }
        return false;
    }

    /**
     *  Logs in a user and updates the view on the response
     * @param username username of the user
     * @param password password of the user
     * @return true if the user was able to log in
     */
    public boolean Login(String username, String password){
        try {
            uac.userLogin(username, password);
            return true;
        } catch (NullUserException e) {
            startView.updateLoginView("User does not exist.");
        } catch (BannedUserException e) {
            startView.updateLoginView("User is banned.");
        } catch (WrongPasswordException e) {
            startView.updateLoginView("Password is wrong.");
        }
        return false;
    }

    /**
     * Generate a temporary password and display it
     * @param email email of the user
     */
    public void generateTempPassword(String email){
        try {
            String tempPass = uac.recoverPassword(email);
            startView.updateRecoveryView(tempPass);
        } catch (NullUserException e) {
            startView.updateRecoveryView("Email not registered");
        }
    }

    /**
     * Access the profile of the logged in user and display it
     */
    public void accessProfile(){
        String[] res = uac.getProfile();

        if (res.length > 1){
            profileView.updateProfileView(res);
        }
    }

    /**
     * Suspend a user and update the view on the response
     * @param username username of the user
     * @param duration duration of the ban
     */
    public void suspendUser(String username, String duration){

        String response;
        try {
            boolean banned = uac.banUser(username, duration);
            if (banned){
                response = "User successfully banned";
            }
            else{
                response = "User could not be banned";
            }
        } catch (NullUserException e) {
            response = "User does not exist";
        } catch (NumberFormatException e) {
            response = "Invalid duration";
        }
        profileView.updateBanView(response);
    }

    /**
     * Change the password and update the view on the status
     * @param newPassword new password of the user
     */
    public void changePassword(String newPassword){
        try {
            uac.changePassword(newPassword);
            profileView.updatePasswordChangeView("Password successfully changed");
        } catch (NullUserException e) {
            profileView.updatePasswordChangeView("User must login to change password");
        } catch (UserSignupException e) {
            profileView.updatePasswordChangeView(e.getMessage());
        }
    }

    /**
     * Log out the user and update the view on the response
     * @return true if a registered user logged out
     */
    public boolean logOut(){
        return uac.logOut();
    }
}
