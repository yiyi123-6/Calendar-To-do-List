package gui.user_activity;

import gui.ControllerBuilder;
import gui.MainMenuViewController;
import gui.UIControllerLoadable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import presenter.UserActivityPresenter;
import presenter.UserStartView;

import java.io.IOException;
import java.util.Optional;

/**
 * Class responsible for controlling the Start View of the GUI
 */

public class StartViewController implements UserStartView, UIControllerLoadable {
    private UserActivityPresenter uap;
    private Stage stage;
    private ControllerBuilder builder;

    @FXML
    private Button signUpButton;

    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button startTrialButton;

    /**
     * Initialize the Presenter, stage and ControllerBuilder.
     * @param presenter user activity presenter
     * @param stage the JavaFX container
     * @param builder the controller builder
     *
     */
    @Override
    public void loadController(Object presenter, Stage stage, ControllerBuilder builder) {
        this.uap = (UserActivityPresenter) presenter;
        this.stage = stage;
        this.builder = builder;
    }

    // ============== Setters =================

    /**
     * Sets a new stage for this StartView
     *
     * @param stage new stage to be set for this StartView
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets a new presenter for this StartView
     *
     * @param uap new presenter to be set for this StartView
     */

    public void setPresenter(UserActivityPresenter uap){
        this.uap = uap;
    }

    // ============== Getters =================

    /**
     * Get the stage of this StartView
     *
     * @return stage of the StartView
     */
    public Stage getStage() {
        return stage;
    }

    // ============== Button Action =================

    /**
     * Action when the user click login button.
     */
    @FXML
    void onClickLogin(){
        boolean loggedIn = uap.Login(usernameTextField.getText(), passwordField.getText());
        if (loggedIn){
            showMainMenu();
        }
    }

    /**
     * Action when the user click password reset button.
     */
    @FXML
    void onClickPWReset(){
        TextInputDialog td = new TextInputDialog("");
        td.setHeaderText("Enter your email");
        td.setTitle("Password recovery");
        Optional<String> res = td.showAndWait();

        if (res.isPresent()){
            uap.generateTempPassword(td.getEditor().getText());
        }
    }

    /**
     * Action when the user click signup button.
     */

    @FXML
    void onClickSignup() throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/SignUpView.fxml")
        );
        stage.setScene(new Scene(loader.load(), 600, 400));
        SignUpViewController controller = loader.getController();

        controller.setBuilder(builder);
        controller.setStage(stage);
        stage.show();
    }

    /**
     * Action when the user click start trial button.
     */
    @FXML
    void onClickStartTrial() {
        if (uap.startTrial(usernameTextField.getText())){
            showMainMenu();
        }
    }

    /**
     * Call back after signing up
     * @param username the new sign up username
     * @param password the new sign up password
     * @param usertype the new sign up user type
     * @param email the new sign up email
     */
    void signUpCallback(String username, String password, String usertype, String email){
        if (usertype == null){
            usertype = "";
        }

        if (uap.Signup(username, password, email, usertype)){
            showConfirmation("Sign up successful", "Sign up").show();
            builder.getControllerInitializer().saveControllers();
        }
    }

    /**
     * Initialize all the buttons and text field.
     *
     */
    @FXML
    void initialize() {
        assert signUpButton != null : "fx:id=\"signupButton\" was not injected: check your FXML file 'StartView.fxml'.";
        assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'StartView.fxml'.";
        assert usernameTextField != null : "fx:id=\"usernameTextField\" was not injected: check your FXML file 'StartView.fxml'.";
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'StartView.fxml'.";
        assert startTrialButton != null : "fx:id=\"startTrialButton\" was not injected: check your FXML file 'StartView.fxml'.";
    }

    // ============== Alert of Action =================

    /**
     * Show the alert when the user sign up.
     *
     * @param response after signing up
     */
    @Override
    public void updateSignUpView(String response) {
        showAlert(response, "Sign up error");
    }

    /**
     * Show the alert when the user login.
     *
     * @param response after login
     */
    @Override
    public void updateLoginView(String response) {
        showAlert(response, "Login error");
    }

    /**
     * Show the alert when the user recover the password.
     *
     * @param response after recover the password
     */
    @Override
    public void updateRecoveryView(String response) {

        TextArea textArea = new TextArea("Your temporary password is: " + response);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        GridPane pane = new GridPane();
        pane.setMaxWidth(Double.MAX_VALUE);
        pane.add(textArea, 0, 0);

        if (response.equals("Email not registered")){
            showAlert(response, "Password recovery");
        }
        else{
            Alert a = showConfirmation("Your temporary password is: " + response, "Password recovery");
            a.getDialogPane().setContent(pane);
            a.show();
        }
    }


    // ============== Pop Up Interface =================

    /**
     * Setup the show alert interface.
     *
     * @param message of the alert
     * @param title of the alert
     */
    private void showAlert(String message, String title){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(message);
        a.setTitle(title);
        a.show();
    }

    /**
     * Setup the show confirmation interface.
     *
     * @param message of the alert
     * @param title of the alert
     */
    private Alert showConfirmation(String message, String title){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText(message);
        a.setTitle(title);
        return a;
    }

    /**
     * Switch to the show main menu view.
     *
     */
    private void showMainMenu(){
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/MainMenuView.fxml")
        );
        try {
            stage.setScene(new Scene(loader.load(), 600, 400));
        } catch (IOException e) {
            return;
        }

        MainMenuViewController controller = loader.getController();
        builder.ConfigureUIController(controller, stage);
        stage.show();
    }
}

