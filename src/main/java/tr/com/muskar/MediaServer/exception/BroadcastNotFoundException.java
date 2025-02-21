package tr.com.muskar.MediaServer.exception;

public class BroadcastNotFoundException extends RuntimeException {
    public BroadcastNotFoundException(String message) {
        super(message);
    }
}
