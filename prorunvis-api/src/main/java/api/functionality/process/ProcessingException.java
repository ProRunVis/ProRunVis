package api.functionality.process;

public class ProcessingException extends RuntimeException {

    /**
     * Constructs a ProcessingException with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     */
    public ProcessingException(final String message) {
        super(message);
    }

    /**
     * Constructs a ProcessingException with the specified detail message
     * and cause.
     *
     * @param message The detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     * @param cause   The cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A null value is permitted,
     *                and indicates that the cause is nonexistent or unknown.)
     */
    public ProcessingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
