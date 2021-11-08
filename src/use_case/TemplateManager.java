package use_case;

import entities.CreationTemplate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A usecase class to store and modify creation templates.
 */
public class TemplateManager implements Serializable {

    // mapping: path --> template instance
    private Map<String, CreationTemplate> templates = new HashMap<>();

    // ============== Getters =================
    /**
     * Get template instance by its filepath.
     * @param path filepath of a template.
     * @return template instance with give filepath path
     */

    public CreationTemplate getTemplate(String path){
        return templates.get(path);
    }

    /**
     * Get the name of a template instance given its filepath.
     * @param path filepath of the template.
     * @return name of the template instance.
     */
    public String getTemplateName(String path){
        return getTemplate(path).getName();
    }
    /**
     * Get the prompts of a template by its path.
     * @param path filepath of the template.
     * @return array of prompts in the selected template instance
     */
    public String[] getPrompts(String path){
        return getTemplate(path).getPrompts();
    }

    // ============== Other Methods =================

    /**
     * Add a template to this TemplateManager
     * @param t CreationTemplate instance to be added
     */
    public void addTemplate(CreationTemplate t){
        templates.put(t.getPath(), t);
    }

    /**
     * Return array containing all paths of templates contained in this TemplateManager
     * @return array of all paths of template instances
     */
    public String[] getAllTemplates(){
        return templates.keySet().toArray(new String[0]);
    }
}
