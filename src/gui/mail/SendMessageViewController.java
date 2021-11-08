package gui.mail;

import controller.CreationControllable;
import gui.ControllerBuilder;
import gui.UIControllerLoadable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import controller.UserActivityController;

import java.io.IOException;
import java.util.*;

/**
 * Class responsible for controlling the GUI in SendMessageView
 */
public class SendMessageViewController implements UIControllerLoadable {

    private Stage stage;
    private ControllerBuilder builder;
    private Map<String, UUID> attachmentLookup = new HashMap<>();
    private List<String> attachments = new ArrayList<>();
    private UUID sender;
    private UUID receiverReply;
    private UUID replyMsgID;
    private boolean replyMode = false;

    @FXML
    private ChoiceBox<String> creationSelector;
    @FXML
    private VBox attachmentContainer;
    @FXML
    private TextField subjectField;
    @FXML
    private TextField toField;
    @FXML
    private TextArea contentArea;

    /**
     * Loads the GUI controller given the presenter, stage, and ControllerBuilder
     * @param presenter presenter
     * @param stage the JavaFX container
     * @param builder the controller builder
     */
    @Override
    public void loadController(Object presenter, Stage stage, ControllerBuilder builder) {
        this.builder = builder;
        this.stage = stage;
    }

    /**
     * Sets the sender of the mail given their UUID
     * @param sender UUID of the sender
     */
    public void setSender(UUID sender){
        this.sender = sender;

        CreationControllable cc = (CreationControllable) builder.getControllers()[1];
        List<String> attachmentItems = new ArrayList<>();

        // configure attachable creations
        for (Object clist : new Object[]{cc.getBrowsableCreations(sender), cc.getOwnCreations(sender)}){
            for (UUID cid : (UUID[]) clist){
                String[] res = cc.viewCreationSummary(cid, sender);
                String curr = "\"" + res[0] + "\" (" + res[1] +") by:"+ res[2];
                String added = addAttachmentEntry(cid, curr);
                attachmentItems.add(added);
            }
        }
        creationSelector.setItems(FXCollections.observableArrayList(attachmentItems));
    }

    /**
     * Configures the controller to reply to messages instead of creating new messages
     * @param sender UUID of the sender
     * @param receiver UUID of the receiver of the message
     * @param msg UUID of the message
     */
    public void setReplyMode(UUID sender, UUID receiver, UUID msg){
        this.sender = sender;
        this.receiverReply = receiver;
        this.replyMsgID = msg;
        UserActivityController uac = (UserActivityController) builder.getControllers()[0];
        toField.setText(uac.getDisplayedUsername(sender, receiver));
        toField.setEditable(false);
    }

    /**
     * Sends the mail when the send button is clicked
     */
    @FXML
    private void onSend() {
        String to =  toField.getText();
        String subject =  subjectField.getText();
        String contents = contentArea.getText();

        List<UUID> attached = new ArrayList<>();

        // collect attachments
        for (String creation : attachments){
            attached.add(attachmentLookup.get(creation));
        }

        MailViewController prev = getMailView();
        if (prev == null){
            return;
        }

        if (replyMode){
            UserActivityController uac = (UserActivityController) builder.getControllers()[0];

            Object[] mailParams = new Object[]{sender, uac.getUsername(receiverReply), subject, contents, attached.toArray(new UUID[0])};
            prev.replyCallback(mailParams, replyMsgID);
        }
        else{
            prev.sendCallback(sender, to, subject, contents, attached.toArray(new UUID[0]));
        }
        stage.show();
    }

    /**
     * Returns user to mailView when clicked
     */
    @FXML
    private void onBack() {
        getMailView();
        stage.show();
    }

    /**
     * Initialize this SendMessageViewController GUI elements.
     */
    @FXML
    public void initialize(){
        contentArea.setWrapText(true);
    }

    // ============== Helper Methods ==============

    // adds a number in case of creations with same string representation
    private String addAttachmentEntry(UUID creationID, String strRep){
        int i = 1;
        String curr = strRep;

        while (attachmentLookup.containsKey(curr)){
            curr = strRep + "(" + i + ")";
            i++;
        }

        attachmentLookup.put(curr, creationID);
        return curr;

    }

    private MailViewController getMailView(){
        stage.close();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/MailView.fxml")
        );
        stage = new Stage(StageStyle.DECORATED);
        try {
            stage.setScene(new Scene(loader.load(), 600, 400));
        } catch (IOException e) {
            return null;
        }

        MailViewController controller = loader.getController();
        builder.ConfigureUIController(controller, stage);
        return controller;
    }

    @FXML
    private void onAttach(ActionEvent actionEvent) {
        String selected = creationSelector.getSelectionModel().getSelectedItem();

        if (selected != null && !attachments.contains(selected)){
            Text attachment = new Text(selected);
            attachmentContainer.getChildren().add(attachment);
            attachments.add(selected);
        }
    }
}
