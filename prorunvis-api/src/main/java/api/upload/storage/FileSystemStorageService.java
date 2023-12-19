package api.upload.storage;

import jakarta.servlet.http.Part;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService implements StorageService{

    private final Path rootLocation;

    public FileSystemStorageService(StorageProperties properties){
        if(properties.getLocation().trim().isEmpty()){
            throw new StorageException("File storage directory cannot be empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }
    @Override
    public void init() {
        try{
            Files.createDirectories(rootLocation);
        }catch(IOException e){
            throw new StorageException("Could not create directory.", e);
        }
    }

    @Override
    public void store(Part part) {  
        try{
            String fileName = FilenameUtils.separatorsToSystem(part.getSubmittedFileName());
            Path file = rootLocation.resolve(fileName);

            if(Files.notExists(file.getParent())){
                Files.createDirectories(file.getParent());
            }

            try (InputStream inputStream = part.getInputStream()){
                Files.copy(inputStream, file, StandardCopyOption.REPLACE_EXISTING);
            }
        }catch(IOException e){
            throw new StorageException("Could not store file.", e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
