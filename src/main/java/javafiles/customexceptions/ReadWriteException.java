package javafiles.customexceptions;

/**
 * An Exception indicating that a JSONIO Object is invalid or unable
 * to be used in such a way because the mode of the JSONIO Object is
 * invalid, not usable for the called method, or file type is not .json.
 *
 * @author Dylan Browne
 */
public class ReadWriteException extends Exception {
    /**
     * Creates a new ReadWriteException with the given message
     *
     * @param message The message that the exception has
     */
    public ReadWriteException(String message) {
        super(message);
    }

    /**
     * Creates a new ReadWriteException with the given cause.
     *
     * @param cause The cause of the Exception
     */
    public ReadWriteException(Throwable cause) {
        super(cause);
    }
}
