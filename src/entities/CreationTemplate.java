package entities;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import java.io.Serializable;

/**
 * A class that represents a template used by users to make creations.
 */
public class CreationTemplate implements Serializable {

    private String name;
    private String filepath;
    private String contents;

    /**
     * Initializes a CreationTemplate
     * @param name name of the template
     * @param filepath filepath that the template file is located in
     * @param contents contents of the template file
     */
    public CreationTemplate(String name, String filepath, String contents){
        this.name = name;
        this.filepath = filepath;
        this.contents = contents;
    }

    // ============== Getters =================

    /**
     * Get the name of the template
     * @return name of the template
     */
    public String getName(){
        return name;
    }

    /**
     * Get the filepath of the template
     * @return absolute filepath of the template
     */
    public String getPath(){
        return filepath;
    }

    /**
     * Get the contents of the template file
     * @return contents of template file
     */
    public String getContents(){
        return contents;
    }

    /**
     * Get the prompts of the template
     * @return array containing each prompt contained in the template
     */
    public String[] getPrompts(){
        return splitPrompts(getContents());
    }

    // ============== Setters =================
    /**
     * Set new content to a template as content
     * @param info new content of the template
     */
    public void setContents(String info) throws java.text.ParseException {
        if (getPrompts().length != splitPrompts(info).length){
            throw new ParseException("Number of prompts should not change", 0);
        }

        this.contents = info;
    }

    /**
     * Set the name of this template
     * @param newName new name of the template
     */
    public void setName(String newName){
        name = newName;
    }

    private String[] splitPrompts(String rawPrompts){
        return rawPrompts.split("\n");
    }
}
