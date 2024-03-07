package api.functionality.process;

public class ProcessingException extends RuntimeException{

    public ProcessingException (final String message){
        super(message);
    }

    public ProcessingException (final String message, final Throwable cause){
        super(message, cause);
    }
}
