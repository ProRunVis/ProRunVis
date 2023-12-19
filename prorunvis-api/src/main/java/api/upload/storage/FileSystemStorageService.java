package api.upload.storage;

import jakarta.servlet.http.Part;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileSystemStorageService implements StorageService{

    private final Path rootLocation;

    public FileSystemStorageService(StorageProperties properties){
        if(properties.getLocation().trim().isEmpty()){
            //TODO throw exception
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }
    @Override
    public void init() {

    }

    @Override
    public void store(Part part) {  

    }

    @Override
    public void deleteAll() {

    }
}
