package api;

import api.upload.storage.StorageProperties;
import api.upload.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class ProRunVisAPI {

    /**
     * Starts a {@link SpringApplication} on localhost.
     * @param args the argument list for the program.
     */
    public static void main(final String[] args) {
        SpringApplication.run(ProRunVisAPI.class, args);
    }

    /**
     * Automated initialization for a {@link StorageService} used
     * by this {@link SpringApplication}.
     * @param storageService the {@link StorageService} to
     *                       initialize.
     * @return a function calling the <code>deleteAll()</code>
     *         and <code>init()</code> methods of the storageService
     */
    @Bean
    CommandLineRunner init(final StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
