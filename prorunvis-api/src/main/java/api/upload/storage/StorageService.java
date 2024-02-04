package api.upload.storage;

import jakarta.servlet.http.Part;

/**
 * A Service for storing data provided from a http request.
 * The user of this interface can create and delete all
 * necessary directories and has control of the way each {@link Part}
 * of the request is stored.
 */
public interface StorageService {

    /**
     * Initializes the storage location and structure used by this storage
     * service element.
     */
    void init();

    /**
     * Stores data to the given storage location. This method should not be
     * called without {@link #init()} having been called before.
     * @param part The data to store. The data is provided in form of a
     *             {@link Part} from a http request.
     */
    void store(Part part);

    /**
     * Deletes all data stored by this storage service or storage services
     * which are providing the same storage location.
     */
    void deleteAll();
}
