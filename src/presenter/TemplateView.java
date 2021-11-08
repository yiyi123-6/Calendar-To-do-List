package presenter;

import java.util.Map;
/**
 * Interface that represents a GUI view involved with template making/editing. Classes that implement this will be able
 * to receive info from TemplatePresenter.
 */
public interface TemplateView {

    /**
     * Updates the template browser view
     * @param templateInfo mapping of paths to template info
     */
    void updateTemplateBrowser(Map<String, String> templateInfo);

    /**
     * Updates the single template view
     * @param response Info contained in the selected template
     */
    void updateTemplateView(String[] response);

    /**
     * Updates the template editing view
     * @param response response from presenter on the editing action
     */
    void updateTemplateEditor(String response);
}
