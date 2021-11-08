package util;

/**
 * Exception class that is thrown when a user could not sign up
 */
public class UserSignupException extends Exception{
    private String message;
    public UserSignupException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
