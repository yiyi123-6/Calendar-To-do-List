package presenter;

import controller.TemplateController;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible for presenting information related to templates
 */
public class TemplatePresenter {
    private TemplateController tc;
    private TemplateView view;

    /**
     * Constructs this template presenter
     */
    public TemplatePresenter(TemplateController tc, TemplateView view){
        this.tc = tc;
        this.view = view;
    }

    /**
     * Shows the template browser
     */
    public void showTemplateBrowser(){

        Map<String, String> res = new HashMap<>();
        String[] templates = tc.getTemplates();

        for (String p : templates){
            res.put(p, tc.getTemplateInfo(p)[0]);
        }
        view.updateTemplateBrowser(res);
    }

    /**
     * Get the info of a template
     * @param path path to the template
     * @return info of the template as an array
     */
    public String[] getInfo(String path){
        return tc.getTemplateInfo(path);
    }

    /**
     * Show the view of a single template
     * @param path path to the template
     */
    public void showTemplate(String path){
        view.updateTemplateView(tc.getTemplateInfo(path));
    }

    /**
     * Edit a template and update the corresponding view
     * @param path path to the template
     * @param input input of the user
     * @param action selected action
     */
    public void editTemplate(String path, String input, String action){
        try {
            tc.editTemplate(path, input, action);
            view.updateTemplateEditor("Template successfully edited");
        } catch (ParseException e) {
            view.updateTemplateEditor(e.getMessage());
        }
    }
}
