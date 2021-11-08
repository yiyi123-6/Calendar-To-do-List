package presenter;

import java.util.Map;
import java.util.UUID;

/**
 * Interface that represents a GUI view involved with messaging making/editing. Classes that implement this will be able
 * to receive info from MailPresenter
 */
public interface MailView {

    /**
     * Updates the inbox view
     * @param res Mapping of message UUIDs to message info
     */
    void updateInboxView(Map<UUID, String[]> res);

    /**
     * Updates the Single message view
     * @param res message info
     * @param messageID UUID of the message
     */
    void updateMessageView(String[] res, UUID messageID);

    /**
     * Updates the sending view
     * @param s response from presenter
     */
    void updateSendingView(String s);
}
