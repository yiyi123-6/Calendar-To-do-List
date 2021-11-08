package entities.user;

import java.io.Serializable;

/**
 * An Interface for which the class implements it can retrieve and set a password.
 */
public interface LogInable extends Serializable {
    /**
     * Gets the password
     * @return password as string
     */
    String getPassword();
    /**
     * Gets the email
     * @return email of the user as a string
     */
    String getEmail();

    /**
     * Get the temporary password of the user
     * @return temporary password as a string
     */
    String getTempPassword();

    /**
     * Set the temporary password of the user for recovery purposes
     */
    void setTempPassword(String password);

    /**
     * Set the password
     * @param password new password
     */
    void setPassword(String password);
}
