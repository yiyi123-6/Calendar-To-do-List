package use_case.factory;

import entities.CreationTemplate;
/**
 * Factory class responsible for building templates
 */

public class TemplateFactory {

    /**
     * Get the default templates to be used with this program
     * @return array containing the default templates
     */
    public CreationTemplate[] getDefaultTemplates(){

        String todoContents = "Name of this todo list?\n" +
                "Name of Todo event\n" +
                "Description of event\n" +
                "Private\n" +
                "Urgency (1-10)";
        String scheduleContents = "Name of the scheduled event list?\n" +
                "Name of schedule event\n" +
                "Description of event\n" +
                "Private\n" +
                "Date of event";
        String taggedContents = "Name of this tagged event?\n" +
                "Name of tagged event\n" +
                "Description of event\n" +
                "Private\n" +
                "Tags - separated by ;";

        CreationTemplate todo = new CreationTemplate("Todo list", "todo placeholder", todoContents);
        CreationTemplate schedule = new CreationTemplate("Scheduled list", "schedule placeholder", scheduleContents);
        CreationTemplate tagged = new CreationTemplate("Tagged Event list", "tagged placeholder", taggedContents);

        return new CreationTemplate[]{todo, schedule, tagged};
    }
}
