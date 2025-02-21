package tr.com.muskar.MediaServer.exception;

public class BroadcastAlreadyStreamingException extends RuntimeException {

    public BroadcastAlreadyStreamingException(String message) {
        super(message);
    }
}
