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

    public static void main(String[] args){
        SpringApplication.run(ProRunVisAPI.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService){
        return (args) ->{
            storageService.deleteAll();
            storageService.init();
        };
    }
}
