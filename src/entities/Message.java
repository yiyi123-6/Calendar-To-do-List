package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * A class representing a message created by an user.
 */
public class Message implements Serializable {
    private UUID messageID = UUID.randomUUID();
    private UUID senderUserID;
    private List<UUID> receiverIDs;
    private String title;
    private String content;
    private List<UUID> attached; // UUID of a creation that this message links to, if any.
    private List<UUID> followUpMessageIDs; // sorted in past to recent

    /**
     * Initializes a new Message, given the title and content of the message.
     * Also given the sender and receiver's.
     * @param senderUserID the unique id of sender
     * @param receiverIDs the unique id of receiver
     * @param title of the message
     * @param content of the message
     * @param attached list of unique id
     */

    public Message(UUID senderUserID, List<UUID> receiverIDs, String title, String content, UUID[] attached) {
        this.senderUserID = senderUserID;
        this.receiverIDs = receiverIDs;
        this.title = title;
        this.content = content;
        this.attached = Arrays.asList(attached);
        this.followUpMessageIDs = new ArrayList<>();
    }

    // ==================== Getters =======================//

    /**
     * Get the id of this Message
     *
     * @return id of this Message
     */

    public UUID getMessageID() {
        return messageID;
    }

    /**
     * Get the id of the Sender
     *
     * @return id of the Sender
     */

    public UUID getSender() {
        return senderUserID;
    }

    /**
     * Get the title of this Message
     *
     * @return title of this Message
     */

    public String getTitle() {
        return title;
    }

    /**
     * Get the content of this Message
     *
     * @return content of this Message
     */

    public String getContent() {
        return content;
    }

    /**
     * Get list of unique id of this Message
     *
     * @return list of unique id of this Message
     */

    public List<UUID> getAttachments() {
        return new ArrayList<>(attached);
    }

    /**
     * Get the id of the Receiver
     *
     * @return the id of the Receiver
     */

    public List<UUID> getReceiverIDs() {
        return receiverIDs;
    }

    /**
     * Get id of the followup Message
     *
     * @return id of the followup Message
     */

    public List<UUID> getFollowUpMessageIDs() {
        return followUpMessageIDs;
    }

    // ====================================================

    /**
     * Add id of the followup Message
     *
     * @param replyMessage id of the followup message
     */

    public void addFollowupMessage(UUID replyMessage) {
        this.followUpMessageIDs.add(replyMessage);
    }

    /**
     * Add id of the Creation Link
     *
     * @param creationLink id of the creation link
     */

    public void addCreationLink(UUID creationLink){
        if (!attached.contains(creationLink)){
            attached.add(creationLink);
        }
    }
}

