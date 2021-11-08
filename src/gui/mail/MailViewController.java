package gui.mail;

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import presenter.MailPresenter;
import presenter.MailView;
import controller.UserActivityController;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * Class responsible for controlling the Mail View of JavaFX.
 */
public class MailViewController implements UIControllerLoadable, MailView {

    public VBox mailContainer;
    private Stage stage;
    private ControllerBuilder builder;
    private MailPresenter mp;
    private UUID sender;

    // ============== Button Action =================

    /**
     * Action when the user click Send button.
     */
    @FXML
    private void onSend() {
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
        controller.setSender(sender);
        stage.show();
    }


    /**
     * Action when the user click Back button.
     * Switch to the MainMenuView
     */
    @FXML
    private void onBack() {
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


    /**
     * Callback method when new mail is sent
     * @param sender sender's UUID
     * @param receivers receiver's username
     * @param subject subject of message
     * @param content content of message
     * @param attachments attachments of message
     */
    public void sendCallback(UUID sender, String receivers, String subject, String content, UUID[] attachments){
        Object[] mailParams = new Object[]{sender, receivers, subject, content, attachments};
        mp.sendMessage(mailParams);
    }

    /**
     * Callback method when a mail is replied to
     * @param mailParams array containing mail info
     * @param repliedMsg UUID of the message being replied to
     */
    public void replyCallback(Object[] mailParams, UUID repliedMsg){
        mp.replyMessage(mailParams, repliedMsg);
    }

    /**
     * Initialize the MailPresenter, stage and ControllerBuilder.
     * @param presenter mail presenter
     * @param stage the JavaFX container
     * @param builder the controller builder
     */
    @Override
    public void loadController(Object presenter, Stage stage, ControllerBuilder builder) {
        this.mp = (MailPresenter) presenter;
        this.stage = stage;
        this.builder = builder;

        UserActivityController uac = (UserActivityController) builder.getControllers()[0];

        sender = uac.getLoggedInUser();
        mp.updateInbox(uac.getLoggedInUser());
    }

    // ============== Update View =================

    /**
     * Update the Inbox view by adding the message.
     * @param res mapping of the UUID and list of message info
     */
    @Override
    public void updateInboxView(Map<UUID, String[]> res) {
        // messageID : [sender username, title, contents]
        for (UUID msgID: res.keySet()){
            String[] info = res.get(msgID);
            String currTitle = "From: " + info[0] + " | Subject:"  + info[1] + " | Preview: " + info[2];
            mailContainer.getChildren().add(constructMailButton(msgID, currTitle));
        }
    }

    /**
     * Show the Message view by displaying the message
     * @param res info contained in message
     * @param messageID UUID of the message
     */
    @Override
    public void updateMessageView(String[] res, UUID messageID) {
        if (res.length == 0){
            return;
        }

        System.out.println(Arrays.toString(res));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/SingleMailView.fxml")
        );
        try {
            stage.setScene(new Scene(loader.load(), 600, 400));
        } catch (IOException e) {
            return;
        }

        SingleMailViewController controller = loader.getController();
        builder.ConfigureUIController(controller, stage);
        controller.setMessageInfo(res, messageID, sender);

        stage.show();
    }

    /**
     * Update the status on a message being sent
     * @param s status of the message that was sent
     */
    @Override
    public void updateSendingView(String s) {

        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Message status");
        if (s.equals("Message successfully sent!")){
            a.setAlertType(Alert.AlertType.CONFIRMATION);
        }
        a.setContentText(s);
        a.showAndWait();
    }

    // =========== Helper Methods =============

    // construct a mail entry that can be clicked
    private Button constructMailButton(UUID mailID, String info){
        Button mailButton = new Button(info);
        mailButton.setMaxWidth(Double.MAX_VALUE);
        mailButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mp.showMessage(mailID);
            }
        });

        return mailButton;
    }
}
