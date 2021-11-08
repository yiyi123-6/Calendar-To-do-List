package gui.user_activity;

import gui.Application;
import gui.ControllerBuilder;
import gui.MainMenuViewController;
import gui.UIControllerLoadable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import presenter.UserActivityPresenter;
import presenter.UserProfileView;

import java.io.IOException;
import java.util.Optional;

/**
 * Class responsible for controlling User profile GUI
 */
public class UserProfileController implements UIControllerLoadable, UserProfileView {
    private Stage stage;
    private ControllerBuilder builder;
    private UserActivityPresenter uap;

    @FXML
    private Text userProfileTitle;
    @FXML
    private HBox profileHBox;

    /**
     * Initialize the Presenter, stage and ControllerBuilder.
     * @param presenter user activity presenter
     * @param stage the JavaFX container
     * @param builder the controller builder
     */


    @Override
    public void loadController(Object presenter, Stage stage, ControllerBuilder builder) {
        this.uap = (UserActivityPresenter) presenter;
        this.stage = stage;
        this.builder = builder;
        uap.accessProfile();
    }

    // ============== Update View =================

    /**
     * Show a confirmation dialog after the user is banned
     * @param response a String show to the user
     */
    @Override
    public void updateBanView(String response) {
        Alert a;
        if (response.equals("User successfully banned")){
            a = new Alert(Alert.AlertType.CONFIRMATION);
        }
        else{
            a = new Alert(Alert.AlertType.ERROR);
        }

        a.setContentText(response);
        a.setTitle("User ban result");
        a.show();
    }

    /**
     * Show a confirmation dialog after changing the password
     * @param response a String show to the user
     */
    @Override
    public void updatePasswordChangeView(String response) {
        Alert a;
        if (response.equals("Password successfully changed")){
            a = new Alert(Alert.AlertType.CONFIRMATION);
        }
        else{
            a = new Alert(Alert.AlertType.ERROR);
        }

        a.setContentText(response);
        a.setTitle("Password change");
        a.show();
    }

    /**
     * Show a confirmation dialog after changing the password
     * @param response a String show to the user
     */
    @Override
    public void updateProfileView(String[] response) {
        userProfileTitle.setText("Profile of: " + response[0] + " (" + response[1] + ")");

        if (response[1].equals("trial")){
            Text t = new Text("Create a non-trial account to edit your profile.");
            profileHBox.getChildren().add(t);
        }
        else{
            Button changePassword = constructPasswordChangeButton();
            profileHBox.getChildren().add(changePassword);
        }

        if (response[1].equals("admin")){
            Button banUser = constructBanButton();
            profileHBox.getChildren().add(banUser);
        }

        stage.show();
    }

    // ============== Button Action =================

    /**
     * Action when the user click Back button.
     * Switch to MainMenuView
     */
    @FXML
    void onClickBack() throws IOException {
        stage.close();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/MainMenuView.fxml")
        );
        stage = new Stage(StageStyle.DECORATED);
        stage.setScene(new Scene(loader.load(), 600, 400));

        MainMenuViewController controller = loader.getController();
        builder.ConfigureUIController(controller, stage);
        stage.show();
    }

    /**
     * Show the confirmation after signing out and go back to start view
     * Switch to StartView
     * @param actionEvent action event for UserProfile
     */
    public void onSignOut(ActionEvent actionEvent) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Log out");
        if (!uap.logOut()){
            a.setContentText("Trial successfully ended");
            try {
                builder.getControllerInitializer().loadControllers();
            } catch (ClassNotFoundException e) {
                a.setContentText("Save data could not be loaded");
                a.setAlertType(Alert.AlertType.ERROR);
            }
        }
        else{
            a.setContentText("Logged out successfully");
            builder.getControllerInitializer().saveControllers();
        }
        a.show();

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/StartView.fxml"));
        try {
            stage.setScene(new Scene(fxmlLoader.load(), 600, 400));
        } catch (IOException e) {
            return;
        }
        StartViewController controller = fxmlLoader.getController();
        builder.ConfigureUIController(controller, stage);
        stage.show();
    }

    // ============== Construct Button =================

    /**
     * Show a dialog to ban a user
     */
    private Button constructBanButton(){
        Button banUser = new Button("Ban a user");
        banUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                TextInputDialog unameDialog = new TextInputDialog("Enter username");
                unameDialog.setTitle("User ban");
                Optional<String> uname = unameDialog.showAndWait();


                TextInputDialog td = new TextInputDialog("Enter duration in days");
                td.setTitle("User ban");
                Optional<String> duration = td.showAndWait();

                if (uname.isPresent() && duration.isPresent()){
                    uap.suspendUser(uname.get(), duration.get());
                }
            }
        });

        return banUser;
    }

    /**
     * Show a dialog to change password
     */
    private Button constructPasswordChangeButton(){
        Button changePassword = new Button("Change password");
        changePassword.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog td = new TextInputDialog("Enter your new password");
                td.setTitle("Password change");
                Optional<String> res = td.showAndWait();

                if (res.isPresent()){
                    uap.changePassword(res.get());
                }
            }
        });

        return changePassword;
    }
}
