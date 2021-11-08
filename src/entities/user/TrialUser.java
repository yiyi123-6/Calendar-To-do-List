package entities.user;

public class TrialUser extends CreationUser{
    /**
     * Initialize a User given their username.
     *
     * @param username username of the user
     */
    public TrialUser(String username) {
        super(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserType(){
        return "trial";
    }
}
