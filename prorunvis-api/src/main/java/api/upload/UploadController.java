package api.upload;

import api.upload.storage.StorageException;
import api.upload.storage.StorageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

/**
 * A {@link Controller} used for handling files, that are uploaded
 * to the server.
 */
@Controller
public class UploadController {

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
    public UploadController(final StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Handles file uploads to the server. The files will be stored as
     * provided by the used {@link StorageService} of this controller.
     * @param request The Http request containing the uploaded files
     *                as {@link Part}, which will be stored using the
     *                provided {@link #storageService}.
     */
    @PostMapping("/api/upload")
    public void handleUpload(final HttpServletRequest request) {

        try {

            for (Part part : request.getParts()) {
                storageService.store(part);
            }
        } catch (IOException | ServletException e) {
            throw new StorageException("One or more files from the directory could not be stored.");
        }
    }
}
