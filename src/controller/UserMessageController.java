package controller;

import gateway.CILoadable;
import use_case.MessageManager;
import use_case.UserManager;
import util.MultiReceiverException;
import util.NullMesageException;
import util.NullUserException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Controller class responsible for reacting to user input related to Messaging
 */
public class UserMessageController implements CILoadable {
    UserManager userManager;
    MessageManager messageManager;

    /**
     * Send a message using given array containing raw message information.
     * @param MessageParams the info of this message in the form of
     *                      [senderID (UUID), receiverNames (String[]), title (String), content (String), creationLink (UUID)]
     * @return the UUID of sent message
     * @throws NullUserException if one of the username in sender usernames can not be found in the system
     * @throws MultiReceiverException if non-admin user tries to send message to more than one user
     */
    public UUID sendMessage(Object[] MessageParams) throws NullUserException, MultiReceiverException {
        UUID messageID = constructMessage(MessageParams);
        for (UUID receiverID : parseReceivers(MessageParams)) {
            userManager.addMessageToInbox(messageID, receiverID);
        }
        return messageID;
    }

    /**
     * Reply a message to given message, using given raw message information.
     * @param MessageParams the info of this message in the form of
     *                     [senderID (UUID), receiverNames (String[]), title (String), content (String), creationLink (UUID)]
     * @param repliedMessageID the message ID of message that gets replied
     * @return the UUID of sent message
     * @throws NullUserException if one of the username in sender usernames can not be found in the system
     * @throws MultiReceiverException if non-admin user tries to send message to more than one user
     */
    public UUID replyMessage(Object[] MessageParams, UUID repliedMessageID) throws NullUserException, MultiReceiverException {
        UUID sentMessageID = sendMessage(MessageParams);
        messageManager.AddFollowUpToMessage(repliedMessageID, sentMessageID);
        return sentMessageID;
    }

    /**
     * Constructs a new message object using the MessageParams, returning ID of newly created message.
     * @param MessageParams the info of this message in the form of
     *                      [senderID (UUID), receiverNames (String[]), title (String), content (String), attachments (UUID[])]
     * @return the UUID of this newly created message.
     * @throws NullUserException if one of the input receiver name can't be found (user do not exist)
     * @throws MultiReceiverException if the sender is not admin but try to send to more than one receiver
     */
    private UUID constructMessage(Object[] MessageParams) throws NullUserException, MultiReceiverException {
        List<UUID> receiverIDs = parseReceivers(MessageParams);

        return messageManager.addMessage((UUID) MessageParams[0], receiverIDs,
                (String) MessageParams[2], (String)MessageParams[3], (UUID[]) MessageParams[4]);
    }

    /**
     * Parses the receivers from the MessageParams and find their corresponding userids.
     * Return the receivers' user ids' as a list of UUID.
     * @param MessageParams the info of this message in the form of
     *                      [senderID (UUID), receiverNames (String[]), title (String), content (String), creationLink (UUID)]
     * @return the List<UUID> of these receivers.
     */
    private List<UUID> parseReceivers(Object[] MessageParams) throws MultiReceiverException, NullUserException {
            UUID senderID = (UUID) MessageParams[0];
            String[] receiverNames = ((String) MessageParams[1]).split(",");
            userManager.NumReceiverValidationCheck(senderID, receiverNames);

            List<UUID> receiverIDs = new ArrayList<>();
            for (String receiverName : receiverNames) {
                receiverIDs.add(userManager.getUserIDByUsername(receiverName));
                }
            return receiverIDs;
        }

    /**
     * Delete a message from a user's inbox.
     * @param messageID the message ID of the message of interest
     * @param userID the user ID of the user who request to delete the message from its inbox
     */
    public void deleteMessageFromInbox(UUID messageID, UUID userID){
        userManager.deleteMessageFromInbox(messageID, userID);
    }

    /**
     * Return an array of string representing the message information, with the detail level specified.
     * @param messageID the message ID of the message of interest
     * @param detailLevel the detail level of message info needed, either "preview" or "full".
     * @return an array of string representing the message information needed.
     */
    public String[] getMessageInfo(UUID messageID, String detailLevel) throws NullMesageException {
        if (detailLevel.equals("full")){
            return messageManager.getMessageInfo(messageID);
        }
        else{
            String[] rawPreview = messageManager.getMessagePreview(messageID);
            rawPreview[0] = userManager.getUser(UUID.fromString(rawPreview[0])).toString();
            return rawPreview;
        }
    }

    /**
     * Get the inbox of a user
     * @param user UUID of the user
     * @return Array of message UUIDs in the user's inbox
     */
    public UUID[] getInbox(UUID user){
        return userManager.getUserMessageIDs(user).toArray(new UUID[0]);
    }

    /**
     * Loads this UserMessageController given an array of managers
     * @param params Array of managers.
     */
    @Override
    public void load(Object[] params) {
        this.userManager = (UserManager) params[0];
        this.messageManager = (MessageManager) params[4];
    }
}
