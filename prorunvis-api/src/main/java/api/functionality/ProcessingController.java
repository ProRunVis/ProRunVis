package api.functionality;


import api.functionality.process.ProcessingException;
import api.functionality.process.ProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProcessingController {

    /**
     * The processing service used by this controller.
     */
    private final ProcessingService processingService;

    /**
     * @param processingService The processing service which this controller
     *                          will use for handling the processing of the
     *                          given input.
     */
    @Autowired
    public ProcessingController(final ProcessingService processingService) {
        this.processingService = processingService;
    }


    /**
     * Handles the processing of provided data. The data will be processed as
     * provided by the used {@link ProcessingService} of this controller.
     *
     * @return A JSON-String representation of the processed data.
     */
    @GetMapping("api/process")
    @ResponseBody
    public String getProcessingData() {
        if (!processingService.isReady()) {
            throw new ProcessingException("No Data has been uploaded!");
        }
        processingService.instrument();
        processingService.trace();
        processingService.process();
        return processingService.toJSON();
    }

    /**
     * An ExceptionHandler for handling {@link ProcessingException}s.
     * If an exceptions occurs, this handler returns a string representation of
     * the message and cause.
     *
     * @param e The thrown exception.
     * @return The message and cause of the exception as String.
     */
    @ExceptionHandler(ProcessingException.class)
    @ResponseBody
    public String handleException(final ProcessingException e) {
        String error = e.getMessage() + "\n";
        if (e.getCause() != null) {
            error += "\n" + e.getCause() + "\n";
        }

        return error;
    }
}
