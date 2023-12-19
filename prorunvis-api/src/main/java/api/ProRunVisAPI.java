package api;

import api.upload.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class ProRunVisAPI {

    public static void main(String[] args){
        SpringApplication.run(ProRunVisAPI.class, args);
    }
}
