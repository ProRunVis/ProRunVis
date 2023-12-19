package api.upload.storage;

import jakarta.servlet.http.Part;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
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
        try{
            Files.createDirectories(rootLocation);
        }catch(IOException e){
            //TODO throw exception
        }
    }

    @Override
    public void store(Part part) {  

    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
