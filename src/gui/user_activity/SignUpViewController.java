package gui.user_activity;

import gui.ControllerBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class responsible for controlling the Sign up GUI
 */
public class SignUpViewController {

    private Stage stage;
    private ControllerBuilder builder;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setBuilder(ControllerBuilder builder){
        this.builder = builder;
    }
    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private ComboBox<?> userTypeComboBox;

    // ============== Button Action =================
    /**
     * Action when the user click Back button.
     * Switch to StartView
     */
    @FXML
    void onClickBackButton() throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/StartView.fxml")
        );

        stage.setScene(new Scene(loader.load(), 600, 400));
        StartViewController startViewController = loader.getController();
        builder.ConfigureUIController(startViewController, stage);
        stage.show();
    }

    /**
     * Action when the user click Submit button.
     * Switch to StartView
     */
    @FXML
    void onClickSubmitButton() throws IOException {
        //
        //Add some signup code here like saving or checking then proceed the MainMenuView.
        //
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/StartView.fxml")
        );
        stage.setScene(new Scene(loader.load(), 600, 400));
        StartViewController startViewController = loader.getController();
        builder.ConfigureUIController(startViewController, stage);
        startViewController.signUpCallback(usernameTextField.getText(), passwordField.getText(), (String) userTypeComboBox.getValue(), emailTextField.getText());
        stage.show();
    }

    /**
     * Initialize all the buttons and text field of Sign Up View.
     *
     */
    @FXML
    void initialize() {
        assert usernameTextField != null : "fx:id=\"usernameTextField\" was not injected: check your FXML file 'SignUpView.fxml'.";
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'SignUpView.fxml'.";
        assert submitButton != null : "fx:id=\"submitButton\" was not injected: check your FXML file 'SignUpView.fxml'.";
        assert userTypeComboBox != null : "fx:id=\"userTypeComboBox\" was not injected: check your FXML file 'SignUpView.fxml'.";
    }
}
