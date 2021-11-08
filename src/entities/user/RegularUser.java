package entities.user;

public class RegularUser extends CreationUser implements LogInable{

    private String password;
    private String email;
    private String tempPass;
    /**
     * Initialize a User given their username.
     *
     * @param username username of the user
     */
    public RegularUser(String username, String password, String email) {
        super(username);
        this.password = password;
        this.email = email;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserType(){
        return "regular";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmail() {
        return email;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTempPassword() {
        return tempPass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTempPassword(String password) {
        tempPass = password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPassword(String password) {
        this.password = password;
    }
}
