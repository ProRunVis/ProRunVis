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

/**
 * A Storage service used to store given data in a directory specified
 * through a {@link StorageProperties} element.
 */
@Service
public class FileSystemStorageService implements StorageService {

    /**
     * Path to rootLocation.
     */
    private final Path rootLocation;

    /**
     * Path to out location.
     */
    private final Path outLocation;

    /**
     * @param properties The storage properties for the storage service
     */
    public FileSystemStorageService(final StorageProperties properties) {
        if (properties.getLocation().trim().isEmpty()) {
            throw new StorageException("File storage directory cannot be empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
        if (properties.getOutLocation().trim().isEmpty()) {
            this.outLocation = Paths.get("resources/out");
        } else {
            this.outLocation = Paths.get(properties.getOutLocation());
        }
    }

    /**
     * Initializes the storage service by creating the folders.
     * specified by <code>rootLocation</code> and <code>outLocation</code>
     */
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
            Files.createDirectories(outLocation);
        } catch (IOException e) {
            throw new StorageException("Could not create directory.", e);
        }
    }

    /**
     * Stores the contents of a file to a new {@link java.io.File}
     * in the directory specified by <code>rootLocation</code>.
     * @param part a part of a http-request representing a file
     *             to be stored. If the file does not exist it is
     *             created inside the rootLocation.
     */
    @Override
    public void store(final Part part) {
        try {
            String fileName = FilenameUtils.separatorsToSystem(part.getSubmittedFileName());
            Path file = rootLocation.resolve(fileName);

            if (Files.notExists(file.getParent())) {
                Files.createDirectories(file.getParent());
            }

            try (InputStream inputStream = part.getInputStream()) {
                Files.copy(inputStream, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Could not store file.", e);
        }
    }

    /**
     * Recursively deletes all files in the directory specified by
     * <code>rootLocation</code> and <code>outLocation</code>
     * using {@link FileSystemUtils}.
     */
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
        FileSystemUtils.deleteRecursively(outLocation.toFile());
    }
}
