package presenter;

import controller.UserMessageController;
import util.MultiReceiverException;
import util.NullMesageException;
import util.NullUserException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Class responsible for presenting information related to Messaging
 */
public class MailPresenter {

    UserMessageController umc;
    MailView view;

    // =======================InboxPresenter========================//

    /**
     * Initializes this mail presenter
     */
    public MailPresenter(UserMessageController umc, MailView view) {
        this.umc = umc;
        this.view = view;
    }

    /**
     * Updates the inbox view of the user
     * @param userID UUID of the user
     */
    public void updateInbox(UUID userID){  // Also responsible for "showInbox"
        Map<UUID, String[]> res = new HashMap<>();
        for (UUID messageID : umc.getInbox(userID)){
            try {
                String[] curr = umc.getMessageInfo(messageID, "preview");
                res.put(messageID, curr);
            } catch (NullMesageException e) {
                umc.deleteMessageFromInbox(messageID, userID);
            }
        }
        view.updateInboxView(res);
    }

    // ==================== MessagePresenter ===================== // Can split into another class if needed, MessagePresenter

    /**
     * Shows a single message
     * @param messageID UUID of the message
     */
    public void showMessage(UUID messageID) {  // Also responsible for "showMessage"
        String[] res = new String[0];
        try {
            res = umc.getMessageInfo(messageID, "full");
            view.updateMessageView(res, messageID);
        } catch (NullMesageException e) {
            view.updateMessageView(res, messageID);
        }

    }

    /**
     * Reply to a message and update the view
     * @param repliedMessage the message id of the message gets replied, always the "master"/first one in a whole dialog
     *                       (the first one sent by the opposite)
     * @param params message parameters
     */
    public void replyMessage(Object[] params, UUID repliedMessage){
        try {
            umc.replyMessage(params, repliedMessage);
            view.updateSendingView("Message successfully sent!");
            showMessage(repliedMessage);
        } catch (NullUserException e) {
            view.updateSendingView("One of the username input is not valid, please check before click send!");
        } catch (MultiReceiverException e) {
            view.updateSendingView("You can only send to one user");
        }
    }

    /**
     * Send a message and update the view
     * @param params
     */
    public void sendMessage(Object[] params){
        try {
            umc.sendMessage(params);
            view.updateSendingView("Message successfully sent!");

        } catch (NullUserException e) {
            view.updateSendingView("One of the receiver's usernames could not be found");
        } catch (MultiReceiverException e) {
            view.updateSendingView("Your account cannot send to multiple users");
        }
    }
}
