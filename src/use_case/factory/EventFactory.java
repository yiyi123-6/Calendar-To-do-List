package use_case.factory;

import entities.creation.Event;
import entities.creation.ScheduleEvent;
import entities.creation.TaggedEvent;
import entities.creation.TodoListEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Factory class responsible for building events
 */
public class EventFactory {

    /**
     * Creates an event given a string
     * @param params string containing event parameters
     * @param eventType the type of event to be made
     * @return the correct event object
     * @throws ParseException if the event could not be created
     */
    public Event getEvent(String params, String eventType) throws ParseException {
        if (eventType.equals("Todo")){

            return constructTodo(parseTodoEvent(params));
        }

        else if (eventType.equals("Schedule")){
            return constructSchedule(parseScheduleEvent(params));
        }

        else if (eventType.equals("Tagged")){
            return constructTagged(parseTaggedEvent(params));
        }

        else {
            return null;
        }
    }


    // make a todolist event from inputs
    private Event constructTodo(Object[] input){

        String name = (String)input[0];
        String desc = (String) input[1];
        boolean privacy = (boolean) input[2];
        int urgency = (int) input[3];

        return new TodoListEvent(name, desc, privacy, urgency);
    }

    // make a schedule event from inputs
    private Event constructSchedule(Object[] input){
        String name = (String)input[0];
        String desc = (String) input[1];
        boolean privacy = (boolean) input[2];
        Date currDate = (Date) input[3];

        return new ScheduleEvent(name, desc, privacy, currDate);
    }

    // make a tagged event from inputs
    private Event constructTagged(Object[] input){

        String name = (String)input[0];
        String desc = (String) input[1];
        boolean privacy = (boolean) input[2];
        String[] tags = ((String) input[3]).split(";");

        return new TaggedEvent(name, desc, privacy, tags);

    }


    private Object[] parseTaggedEvent(String params) throws ParseException{
        // 0 = name, 1 = desc, 2 = privacy, 3 = tags
        Object[] parsed = parseEventInfo(params);
        Object[] res = new Object[parsed.length];

        if (parsed.length < 4){
            throw new ParseException("Not enough arguments given to make a tagged list. Given: " + parsed.length, 0);
        }
        String rawTags = params.split(",")[3];
        System.arraycopy(parsed, 0, res, 0, 3);

        res[3] = rawTags;
        return res;
    }

    // collect responses for adding a single todolist
    private Object[] parseTodoEvent(String params) throws ParseException {
        // 0 = name, 1 = desc, 2 = privacy, 3 = urgency
        Object[] parsed = parseEventInfo(params);
        Object[] res = new Object[parsed.length];

        if (parsed.length < 4){
            throw new ParseException("Not enough arguments given to make a todo-list. Given: " + parsed.length, 0);
        }

        String urgency = params.split(",")[3];

        boolean validUrgency = false;
        for (int u = 1; u <= 10; u++){
            if (String.valueOf(u).equals(urgency)){
                validUrgency = true;
                break;
            }
        }

        if (!validUrgency){
            throw new ParseException("Invalid urgency. Given: " + parsed[3], 0);
        }

        System.arraycopy(parsed, 0, res, 0, 3);

        res[3] = Integer.parseInt(urgency);
        return res;
    }

    // collect responses for adding a single schedule list
    private Object[] parseScheduleEvent(String params) throws ParseException {
        // 0 = name, 1 = desc, 2 = privacy, 3 = date (MM-DD-YYYY hours:minutes)
        Object[] parsed = parseEventInfo(params);
        Object[] res = new Object[parsed.length];

        if (parsed.length < 4){
            throw new ParseException("Not enough arguments given to make a todo-list. Given: " + parsed.length, 0);
        }

        Date currDate = new SimpleDateFormat("yy-MM-dd").parse(params.split(",")[3]);
        System.arraycopy(parsed, 0, res, 0, 3);
        res[3] = currDate;

        return res;
    }

    // parse info about shared by all events
    private Object[] parseEventInfo(String params) throws ParseException{
        // 0 = name, 1 = desc, 2 = privacy
        String[] splitted = params.split(",");
        Object[] res = new Object[splitted.length];

        if (splitted.length < 3){
            throw new ParseException("Not enough arguments given to make an Event. Given: " + splitted.length, 0);
        }

        if (splitted[2].equals("false")){
            res[2] = false;
        }

        else if (splitted[2].equals("true")){
            res[2] = true;
        }

        else{
            throw new ParseException("Invalid Privacy: " + splitted[2], 0);
        }

        res[0] = splitted[0];
        res[1] = splitted[1];

        return res;
    }
}
