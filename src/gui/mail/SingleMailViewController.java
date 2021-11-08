package gui.mail;

import controller.CreationControllable;
import gui.ControllerBuilder;
import gui.UIControllerLoadable;
import gui.creation.CreationViewController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import controller.UserActivityController;

import java.io.IOException;
import java.util.UUID;

/**
 * Class responsible for controlling the GUI in SingleMailView
 */
public class SingleMailViewController implements UIControllerLoadable {

    private Stage stage;
    private ControllerBuilder builder;
    private UUID messageID;
    private UUID mailSender;
    private UUID viewer;

    @FXML
    private TextArea messageArea;
    @FXML
    private VBox attachmentContainer;

    /**
     * Initializes GUI elements
     */
    @FXML
    public void initialize() {
        messageArea.setWrapText(true);
        messageArea.setEditable(false);
    }

    // reply button clicked
    @FXML
    private void onReply() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/SendMessageView.fxml")
        );
        try {
            stage.setScene(new Scene(loader.load(), 600, 400));
        } catch (IOException e) {
            return;
        }
        SendMessageViewController controller = loader.getController();
        builder.ConfigureUIController(controller, stage);
        controller.setReplyMode(viewer, mailSender, messageID);
        controller.setSender(viewer);
        stage.show();
    }

    // back button clicked
    @FXML
    private void onBack() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/MailView.fxml")
        );
        try {
            stage.setScene(new Scene(loader.load(), 600, 400));
        } catch (IOException e) {
            return;
        }
        MailViewController controller = loader.getController();
        builder.ConfigureUIController(controller, stage);

        stage.show();
    }

    /**
     * Loads this SingleMailViewController given a presenter, stage and Controllerbuilder
     * @param presenter presenter
     * @param stage the JavaFX container
     * @param builder the controller builder
     */
    @Override
    public void loadController(Object presenter, Stage stage, ControllerBuilder builder) {
        this.stage = stage;
        this.builder = builder;
    }

    /**
     * Displays the message info to the text area in this view
     * @param info Array of message info
     * @param messageID message's UUID
     * @param viewer UUID of user viewing message
     */
    public void setMessageInfo(String[] info, UUID messageID, UUID viewer){
        this.mailSender = UUID.fromString(info[0]);
        this.viewer = viewer;
        UserActivityController uac = (UserActivityController) builder.getControllers()[0];
        String displayed = "";

        int i = 0;
        for (String s : info){
            // sender
            if (i % 5 == 0){
                displayed += "--------------------------------------------------\n";
                displayed += "From: " + uac.getDisplayedUsername(viewer, UUID.fromString(s)) + "\n";
            }
            // receivers
            else if (i % 5 == 1){
                displayed += "To: " + processReceivers(s.split(",")) + "\n";
            }
            // title
            else if (i % 5 == 2 ){
                displayed += "Subject: " +  s + "\n";
            }
            // content
            else if (i % 5 == 3 ){
                displayed += s + "\n";
            }
            // attachments
            else {
                addAttachments(s);
            }
            i++;
        }
        this.messageID = messageID;
        messageArea.setText(displayed);
    }

    // processes receieved user ids
    private String processReceivers(String[] ids){
        StringBuilder res = new StringBuilder();
        UserActivityController uac = (UserActivityController) builder.getControllers()[0];
        for (String s : ids){
            res.append(uac.getDisplayedUsername(viewer, UUID.fromString(s)));
        }
        return res.toString();
    }

    // add attachment buttons to attachment container vbox
    private void addAttachments(String attachments){
        if (attachments.isEmpty()){
            return;
        }

        String[] ids = attachments.split(",");
        for (String s : ids){
            Button b = constructCreationLinkButton(UUID.fromString(s));
            if (b != null){
                attachmentContainer.getChildren().add(b);
            }
        }
    }

    // construct a creation link
    private Button constructCreationLinkButton(UUID creationID){

        CreationControllable cc = (CreationControllable) builder.getControllers()[1];
        if (!cc.containsCreation(creationID)){
            return null;
        }

        String[] creationInfo = cc.viewCreationSummary(creationID, viewer);
        Button b = new Button(creationInfo[0] + " ( " + creationInfo[1] + " ) By: "+ creationInfo[2]);

        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                CreationViewController controller = getCreationBrowserController();
                if (controller == null){
                    return;
                }
                controller.linkCreationCallback(creationID);
                stage.show();
            }
        });

        return b;
    }

    // get controller for creation browser
    private CreationViewController getCreationBrowserController(){

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/CreationView.fxml")
        );
        stage = new Stage(StageStyle.DECORATED);
        try {
            stage.setScene(new Scene(loader.load(), 800, 600));
        } catch (IOException e) {
            return null;
        }

        CreationViewController controller = loader.getController();
        builder.ConfigureUIController(controller, stage);
        return controller;
    }
}
