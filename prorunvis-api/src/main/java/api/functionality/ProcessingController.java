package api.functionality;


import api.functionality.process.ProcessingException;
import api.functionality.process.ProcessingService;
import api.upload.storage.StorageService;
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


    @GetMapping("api/process")
    @ResponseBody
    public String getProcessingData() throws ProcessingException{
        if (!processingService.isReady()) throw new ProcessingException("No Data has been uploaded!");
        processingService.instrument();
        processingService.trace();
        processingService.process();
        return processingService.toJSON();
    }

    @ExceptionHandler(ProcessingException.class)
    @ResponseBody
    public String handleException(ProcessingException e){
        String error = e.getMessage()+"\n";
        if(e.getCause() != null){
            error += "\n"+e.getCause()+"\n";
        }

        return error;
    }
}
