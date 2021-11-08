package presenter;
/**
 * Interface that represents a GUI view involved with user login. Classes that implement this will be able
 * to receive info from UserActivityPresenter.
 */
public interface UserStartView {

    /**
     * Show the sign up response
     * @param response response from the presenter
     */
    void updateSignUpView(String response);

    /**
     * show the login response
     * @param response response from the presenter
     */
    void updateLoginView(String response);

    /**
     * Show the password recovery response
     * @param response response from the presenter
     */
    void updateRecoveryView(String response);
}
