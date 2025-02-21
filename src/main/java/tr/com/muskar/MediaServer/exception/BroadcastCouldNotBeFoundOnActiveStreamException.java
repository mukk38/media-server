package tr.com.muskar.MediaServer.exception;

public class BroadcastCouldNotBeFoundOnActiveStreamException extends RuntimeException {

    public BroadcastCouldNotBeFoundOnActiveStreamException(String message) {
        super(message);
    }
}
