package entities.creation;

/**
 * An event that contains tags which can be used to categorize them.
 */
public class TaggedEvent extends Event {
    private String[] tags;

    /**
     * Constructs this tag event
     * @param name name of the tagged event
     * @param note notes on the tagged event
     * @param privacy privacy of the tagged event
     * @param tags tags of the tagged event
     */
    public TaggedEvent(String name, String note, boolean privacy, String[] tags){
        super(name, note, privacy);
        this.tags = tags.clone();
    }

    /**
     * Get the tagged event as a string
     * @return string representation of tagged event
     */
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(getName() + " - \"" + getNote() + " | Tags: ");

        for (String s: tags){
            res.append("[");
            res.append(s).append("]");
        }
        return res.toString();
    }
}
