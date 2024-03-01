package api.process;


import api.upload.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ProcessingController {

    /**
     * The storage service used by this controller.
     */
    private final StorageService storageService;

    /**
     * @param storageService The storage service which this controller
     *                       will use for handling file storage for
     *                       uploaded files.
     */
    @Autowired
    public ProcessingController(final StorageService storageService) {
        this.storageService = storageService;
    }
}
