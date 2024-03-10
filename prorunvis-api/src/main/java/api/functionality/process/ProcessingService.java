package api.functionality.process;

public interface ProcessingService {

    boolean isReady();

    void instrument();

    void trace();

    void process();

    String toJSON();
}
