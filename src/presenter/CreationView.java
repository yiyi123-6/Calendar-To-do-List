package presenter;

import java.util.Map;
import java.util.UUID;

/**
 * Interface that represents a GUI view involved with creation making/editing. Classes that implement this will be able
 * to receive info from CreationPresenter.
 */
public interface CreationView {

    /**
     * Updates a single creation view
     * @param params Map containing creation parameters
     * @param info Map containing creation specific info
     * @param creationID UUID of the creation
     */
    void updateCreationView(Map<String, String> params, Map<String, String> info, UUID creationID);

    /**
     * Updates the creation construction view
     * @param response response of the creation construction
     */
    void updateCreationConstructionView(String response);

    /**
     * Updates the creation edit view
     * @param response result of creation editing action
     */
    void updateCreationEditView(String response);

    /**
     * Updates the creation browser
     * @param creations Mapping of creation UUIDs to creation info
     */
    void updateCreationBrowserView(Map<UUID, String[]> creations);

    /**
     * Shows the prompts of a template when using to make a creation
     * @param response prompts of template
     * @param path path to the template
     */
    void showCreationFromTemplateView(String[] response, String path);
}
