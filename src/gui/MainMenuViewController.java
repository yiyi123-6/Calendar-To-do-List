package gui;

import gui.creation.CreationViewController;
import gui.mail.MailViewController;
import gui.template.TemplateViewController;
import gui.user_activity.UserProfileController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import controller.UserActivityController;

import java.io.IOException;

/**
 * Class responsible for controling the Main Menu View of JavaFX.
 */
public class MainMenuViewController implements UIControllerLoadable{


    private Stage stage;
    private ControllerBuilder builder;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Button ownCreationButton;
    @FXML
    private VBox actionContainer;

    @FXML
    private Button profileButton;

    @FXML
    private Button exitButton;

    // ============== Button Action =================

    /**
     * Action when the user click exit button.
     */
    @FXML
    void onClickExit() {
        stage.close();
    }

    /**
     * Action when the user click Creation button.
     */
    @FXML
    void onClickCreation(){
        stage.close();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/CreationView.fxml")
        );
        stage = new Stage(StageStyle.DECORATED);
        try {
            stage.setScene(new Scene(loader.load(), 800, 600));
        } catch (IOException e) {
            return;
        }

        CreationViewController controller = loader.getController();
        builder.ConfigureUIController(controller, stage);

        stage.show();
    }

    /**
     * Action when the user click Profile button.
     *
     * @param event of the MainMenuView
     */
    @FXML
    private void onClickProfile(ActionEvent event){
        stage.close();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/UserProfileView.fxml")
        );
        stage = new Stage(StageStyle.DECORATED);
        try {
            stage.setScene(new Scene(loader.load(), 600, 400));
        } catch (IOException e) {
            return;
        }

        UserProfileController controller = loader.getController();
        builder.ConfigureUIController(controller, stage);
        stage.show();
    }

    /**
     * Action when the user click the Mail button.
     */
    @FXML
    private void onMailClick() {
        stage.close();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/MailView.fxml")
        );
        stage = new Stage(StageStyle.DECORATED);
        try {
            stage.setScene(new Scene(loader.load(), 600, 400));
        } catch (IOException e) {
            return;
        }

        MailViewController controller = loader.getController();
        builder.ConfigureUIController(controller, stage);
        stage.show();
    }

    // ============== Initializer =================

    /**
     * Initialize all the buttons on the MainMenuView.
     */
    @FXML
    void initialize() {
        assert ownCreationButton != null : "fx:id=\"ownCreationButton\" was not injected: check your FXML file 'MainMenuView.fxml'.";
        assert profileButton != null : "fx:id=\"profileButton\" was not injected: check your FXML file 'MainMenuView.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'MainMenuView.fxml'.";

    }

    /**
     * Initialize the Presenter, stage and ControllerBuilder.
     * Create the MainMenuView.
     * @param presenter user presenter
     * @param stage the JavaFX container
     * @param builder the controller builder
     *
     */
    @Override
    public void loadController(Object presenter, Stage stage, ControllerBuilder builder) {
        this.stage = stage;
        this.builder = builder;

        UserActivityController uac = (UserActivityController) builder.getControllers()[0];
        if (uac.getProfile()[1].equals("admin")){
            Button templateButton = constructTemplateButton();
            actionContainer.getChildren().add(templateButton);
        }
    }

    /**
     * Construct a template button.
     * Switch to TemplateView.
     */
    private Button constructTemplateButton(){
        Button templateButton = new Button("Templates");
        templateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/TemplateView.fxml")
                );
                stage = new Stage(StageStyle.DECORATED);
                try {
                    stage.setScene(new Scene(loader.load(), 600, 400));
                } catch (IOException e) {
                    return;
                }

                TemplateViewController controller = loader.getController();
                builder.ConfigureUIController(controller, stage);
                stage.show();
            }
        });

        return templateButton;
    }

}
