package api.upload.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * A property element to define a storage configuration.
 */
@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * The location where newly uploaded files will be stored.
     */
    private String location = "resources/in";

    /**
     * Gets the {@code location} field of this property object.
     * @return The String representing the relative path to the
     *         location.
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Sets the {@code location} field of this property element.
     * @param location The String to set as location.
     */
    public void setLocation(final String location) {
        this.location = location;
    }
}
