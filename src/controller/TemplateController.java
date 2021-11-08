/**
 * Package that contains classes which help to edit, manage, and display template UI.
 */
package controller;

import gateway.CILoadable;
import use_case.TemplateManager;
import use_case.factory.TemplateFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class responsible for reacting to user input related to editing and accessing templates
 */
public class TemplateController implements CILoadable {
    private TemplateManager tm;
    private TemplateFactory tf = new TemplateFactory();

    /**
     * Initializes this template controller
     */
    public TemplateController(){
        this.tm = new TemplateManager();
    }

    /**
     * Get paths of all templates contained in this TemplateController
     * @return Array of all template paths.
     */
    public String[] getTemplates(){
        return tm.getAllTemplates();
    }

    /**
     * Display a template given a path to it
     * @return Array containing template's info:
     * 0 - template name
     * 1+ - template prompts
     */
    public String[] getTemplateInfo(String path){

        ArrayList<String> res = new ArrayList<>();
        res.add(tm.getTemplateName(path));
        res.addAll(Arrays.asList(tm.getPrompts(path)));

        return res.toArray(new String[0]);
    }

    /**
     * Edit a template based on user input.
     * @param path path of the template instance.
     */
    public void editTemplate(String path, String input, String rawSelection) throws ParseException {
        if (rawSelection.equals("1")){
            tm.getTemplate(path).setName(input);
        }

        else if (rawSelection.equals("2")){
            tm.getTemplate(path).setContents(input);
        }

        else{
            throw new ParseException("Invalid edit option selected", 0);
        }
    }

    /**
     * Initialize this template controller given an array of loaded managers
     * @param params Array of manager classes
     */
    @Override
    public void load(Object[] params) {
        this.tm = (TemplateManager) params[3];
        List<String> paths = Arrays.asList(tm.getAllTemplates());

        if (!paths.contains("todo placeholder")){
            tm.addTemplate(tf.getDefaultTemplates()[0]);
        }

        if (!paths.contains("schedule placeholder")){
            tm.addTemplate(tf.getDefaultTemplates()[1]);
        }

        if (!paths.contains("tagged placeholder")){
            tm.addTemplate(tf.getDefaultTemplates()[2]);
        }
    }
}
