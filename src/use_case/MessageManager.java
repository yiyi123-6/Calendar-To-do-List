package use_case;

import entities.Message;
import util.NullMesageException;

import java.io.Serializable;
import java.util.*;

public class MessageManager implements Serializable {
    private Map<UUID, Message> messages;


    public MessageManager(){
        this.messages = new HashMap<>();
    }

    /**
     * Adds a new message to the Map messages
     * @param senderUserID UserID of the sender of this message
     * @param receiverIDs UserID of the receiver of this message
     * @param title the title of this message
     * @param content the content of this message
     * @param attached the UUIDs of creations attached to this message
     * @return the messageID of this newly added message.
     */
    public UUID addMessage(UUID senderUserID, List<UUID> receiverIDs, String title, String content, UUID[] attached){
        Message message = new Message(senderUserID, receiverIDs, title, content, attached);
        messages.put(message.getMessageID(), message);
        return message.getMessageID();
    }


    /**
     * Give full information of the message and its followups.
     * @param messageID ID of the message.
     * @return array containing full information of message and its follow up messages:
     * 0 - sender UUID as string
     * 1 - receiver UUIDs as string (comma separated)
     * 2 - content
     * 3 - attached creation UUIDs as string (comma separated)
     */
    public String[] getMessageInfo(UUID messageID) throws NullMesageException {
        Message currMsg = messages.get(messageID);
        if (currMsg == null){
            throw new NullMesageException();
        }

        String title = currMsg.getTitle();
        String content = currMsg.getContent();
        String creationLinkStr = "";
        String receivers = "";

        for (UUID id : currMsg.getAttachments()){
            creationLinkStr += id.toString() + ",";
        }
        for (UUID uid : currMsg.getReceiverIDs()){
            receivers += uid.toString() + ",";
        }

        List<UUID> followUpIDs = currMsg.getFollowUpMessageIDs();
        List<String> res = Arrays.asList(currMsg.getSender().toString(), receivers, title, content, creationLinkStr);

        if (followUpIDs.isEmpty()){
            return res.toArray(new String[0]);
        }

        for (UUID msg : followUpIDs){
            List<String> followUpInfo = Arrays.asList(getMessageInfo(msg));
            res.addAll(followUpInfo);
        }
        return res.toArray(new String[0]);
    }

    /**
     * Return a preview of the message given its UUID
     * @param messageID UUID of the message
     * @return Array containing message info:
     * 0 - sender UUID as string
     * 1 - shortened title
     * 2 - shortened contents
     * @throws NullMesageException if message does not exist
     */
    public String[] getMessagePreview(UUID messageID) throws NullMesageException {

        Message currMsg = messages.get(messageID);

        if (currMsg == null){
            throw new NullMesageException();
        }

        String title = currMsg.getTitle();
        String content = currMsg.getContent();

        return new String[] {currMsg.getSender().toString(), getChar(title, 20), getChar(content, 50)};

    }

    /**
     * Helper for getPreviewMessageInfo, return fixed amount of first few characters of a given string.
     * @param str string given to be cut
     * @param num number of character needed
     * @return the first few char char of string.
     */
    private String getChar(String str, int num) {
        if (str.length() > num) {
            return str.substring(0, num) + "...";
        }
        else {
            return str;
        }
    }

    /**
     * Add the id of given reply message to the follow-up message ids of the given replied message.
     * @param repliedMessageID the message id of the message being replied
     * @param replyMessageID the message id of the reply message
     */
    public void AddFollowUpToMessage(UUID repliedMessageID, UUID replyMessageID) {
        messages.get(repliedMessageID).addFollowupMessage(replyMessageID);
    }
}
