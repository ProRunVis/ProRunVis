package api.upload.storage;

import jakarta.servlet.http.Part;

public interface StorageService {

    void init();

    void store(Part part);

    void deleteAll();
}
