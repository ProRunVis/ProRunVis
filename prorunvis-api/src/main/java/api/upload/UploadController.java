package api.upload;

import api.upload.storage.StorageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class UploadController {

    private final StorageService storageService;

    @Autowired
    public UploadController(StorageService storageService){
        this.storageService = storageService;
    }
    @PostMapping("/api/upload")
    public void handleUpload(HttpServletRequest request){

        try {

            for (Part part : request.getParts()) {
                storageService.store(part);
            }
        }catch (IOException | ServletException e){
            //TODO throw custom exception
        }
    }
}
