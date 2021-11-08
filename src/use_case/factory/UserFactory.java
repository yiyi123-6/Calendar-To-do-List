package use_case.factory;

import entities.user.*;
import util.UserSignupException;
/**
 * Factory class responsible for building Users
 */
public class UserFactory {

    /**
     * Constructs a non-loginable user
     * @param username username of the user
     * @param userType type of the user
     * @return the correct user instance
     * @throws UserSignupException if data is ill-formated
     */
    public User getUser(String username, String userType) throws UserSignupException {
        if (userType.equals("Trial")){
            return new TrialUser(username);
        }

        else{
            throw new UserSignupException("Invalid user type");
        }
    }

    /**
     * Constructs a loginable user
     * @param username username of the user
     * @param password password of the user
     * @param email email of the user
     * @param userType type of user
     * @return the correct user instance
     * @throws UserSignupException if data is ill-formatted
     */
    public User getUser(String username, String password, String email, String userType) throws UserSignupException {
        if (userType.equals("Regular")){
            return new RegularUser(username, password, email);
        }
        else if (userType.equals("Anonymous")){
            return new AnonymousUser(username, password, email);
        }

        else if(userType.equals("Admin")){
            return new AdminUser(username, password, email);
        }

        else{
            throw new UserSignupException("Invalid user type");
        }
    }
}
