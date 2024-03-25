package api.upload;

import api.upload.storage.StorageException;
import api.upload.storage.StorageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * Handles hosting the default landing page. Always hosts
     * index.html on the default path "/".
     *
     * @return A String representing the file to be hosted, which can
     * be used by the thymeleaf plugin
     */
    @GetMapping("/")
    public String getIndex() {
        return "index.html";
    }

    /**
     * Handles file uploads to the server. The files will be stored as
     * provided by the used {@link StorageService} of this controller.
     *
     * @param request The Http request containing the uploaded files
     *                as {@link Part}, which will be stored using the
     *                provided {@link #storageService}.
     */
    @PostMapping("/api/upload")
    @ResponseBody
    public void handleUpload(final HttpServletRequest request) {

        //ensure that no other files are contained within in/out
        storageService.deleteAll();

        try {
            for (Part part : request.getParts()) {
                storageService.store(part);
            }
        } catch (IOException | ServletException e) {
            throw new StorageException("No files for upload selected.");
        }
    }

    /**
     * An ExceptionHandler for handling {@link StorageException}s.
     * If an exceptions occurs, this handler returns a string representation of
     * the message and cause.
     *
     * @param e The thrown exception.
     * @return The message and cause of the exception as String.
     */
    @ExceptionHandler(StorageException.class)
    @ResponseBody
    public String handleException(final StorageException e) {
        String error = e.getMessage() + "\n";
        if (e.getCause() != null) {
            error += "\n" + e.getCause() + "\n";
        }

        return error;
    }
}
