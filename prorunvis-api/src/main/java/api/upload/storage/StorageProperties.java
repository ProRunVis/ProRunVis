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
    private String inLocation = "resources/in";

    /**
     * The location to use as storage for temporary output files.
     */
    private String outLocation = "resources/out";

    /**
     * Gets the {@code inLocation} field of this property object.
     *
     * @return The String representing the relative path to the
     * inLocation.
     */
    public String getLocation() {
        return this.inLocation;
    }

    /**
     * Sets the {@code inLocation} field of this property element.
     *
     * @param location The String to set as inLocation.
     */
    public void setLocation(final String location) {
        this.inLocation = location;
    }

    /**
     * Gets the {@code outLocation} field of this property object.
     *
     * @return The String representing the relative path to the
     * outLocation.
     */
    public String getOutLocation() {
        return this.outLocation;
    }

    /**
     * Sets the {@code outLocation} field of this property element.
     *
     * @param location The String to set as outLocation.
     */
    public void setOutLocation(final String location) {
        this.outLocation = location;
    }
}
