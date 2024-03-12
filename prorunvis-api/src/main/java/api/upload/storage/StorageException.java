package api.upload.storage;

/**
 * Signals that a storage exception has occurred.
 */
public class StorageException extends RuntimeException {

    /**
     * Constructs a StorageException with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     */
    public StorageException(final String message) {
        super(message);
    }

    /**
     * Constructs a StorageException with the specified detail message
     * and cause.
     *
     * @param message The detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     * @param cause   The cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A null value is permitted,
     *                and indicates that the cause is nonexistent or unknown.)
     */
    public StorageException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
