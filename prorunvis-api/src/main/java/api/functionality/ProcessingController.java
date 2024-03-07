package api.functionality;


import api.functionality.process.ProcessingException;
import api.functionality.process.ProcessingService;
import api.upload.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProcessingController {

    /**
     * The storage service used by this controller.
     */
    private final StorageService storageService;

    private final ProcessingService processingService;

    /**
     * @param storageService The storage service which this controller
     *                       will use for handling file storage for
     *                       uploaded files.
     */
    @Autowired
    public ProcessingController(final StorageService storageService, final ProcessingService processingService) {
        this.storageService = storageService;
        this.processingService = processingService;
    }


    @GetMapping("api/process")
    @ResponseBody
    public String getProcessingData(){
        if(!processingService.isReady()) throw new ProcessingException("No Data has been uploaded!");
        processingService.trace();
        processingService.process();
        return processingService.toJSON();
    }
}
