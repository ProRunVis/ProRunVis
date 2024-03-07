package api.functionality.process;

public interface ProcessingService {

    boolean isReady();

    void trace();

    void process();

    String toJSON();
}
