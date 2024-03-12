package api.functionality.process;

public interface ProcessingService {

    /**
     * Checks if data can be processed.
     *
     * @return true if data can be processed, false otherwise.
     */
    boolean isReady();

    /**
     * Instruments data by using the provided functions of
     * the ProRunVis library.
     */
    void instrument();

    /**
     * Traces code by using provided functions of the
     * ProRunVis library.
     */
    void trace();

    /**
     * Processes the traced data by using provided
     * functions of the ProRunVis library.
     */
    void process();

    /**
     * Converts a processed trace to a JSON representation.
     *
     * @return A String containing the JSON representation.
     */
    String toJSON();
}
