package api.functionality.process;

import org.springframework.stereotype.Service;

@Service
public class SingleRunProcessingService implements ProcessingService{
    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void trace() {

    }

    @Override
    public void process() {

    }

    @Override
    public String toJSON() {
        return null;
    }
}
