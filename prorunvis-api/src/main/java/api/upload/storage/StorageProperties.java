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

    private String outLocation = "resources/out";

    /**
     * Gets the {@code location} field of this property object.
     * @return The String representing the relative path to the
     *         location.
     */
    public String getLocation() {
        return this.inLocation;
    }

    /**
     * Sets the {@code location} field of this property element.
     * @param location The String to set as location.
     */
    public void setLocation(final String location) {
        this.inLocation = location;
    }

    public String getOutLocation(){
        return this.outLocation;
    }

    public void setOutLocation(final String location){
        this.outLocation = location;
    }
}
