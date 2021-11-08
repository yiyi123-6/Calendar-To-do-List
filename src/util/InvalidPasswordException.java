/**
 * Package which contains the exceptions thrown by this application.
 */
package util;

/**
 * Exception class thrown when an ill-formatted password is given.
 */
public class InvalidPasswordException extends Exception{
    private String message;
    public InvalidPasswordException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
