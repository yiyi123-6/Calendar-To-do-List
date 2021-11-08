package presenter;
/**
 * Interface that represents a GUI view involved with the user profile. Classes that implement this will be able
 * to receive info from UserActivityPresenter.
 */
public interface UserProfileView {

    /**
     * Update the Ban view
     * @param response response from the presenter
     */
    void updateBanView(String response);

    /**
     * Shows the result of changing the password to the view
     * @param response response from the presenter
     */
    void updatePasswordChangeView(String response);

    /**
     * Updates the user profile with new info
     * @param response new info of the user profile
     */
    void updateProfileView(String[] response);
}
