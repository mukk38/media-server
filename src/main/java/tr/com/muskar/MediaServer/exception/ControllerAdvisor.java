package tr.com.muskar.MediaServer.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {
    private static final String TIMESTAMP_LABEL = "timestamp";
    private static final String MESSAGE_LABEL = "message";


    @ExceptionHandler(BroadcastGroupNotFoundException.class)
    public ResponseEntity<Object> broadcastGroupNotFoundException(BroadcastGroupNotFoundException exception, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP_LABEL, LocalDateTime.now());
        body.put(MESSAGE_LABEL, "Broadcast Group Not Found  id :" + exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BroadcastNotFoundException.class)
    public ResponseEntity<Object> broadcastNotFoundException(BroadcastNotFoundException exception, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP_LABEL, LocalDateTime.now());
        body.put(MESSAGE_LABEL, "Broadcast  Not Found  id :" + exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BroadcastAlreadyStreamingException.class)
    public ResponseEntity<Object> broadcastAlreadyStreamingNotFoundException(BroadcastAlreadyStreamingException exception, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP_LABEL, LocalDateTime.now());
        body.put(MESSAGE_LABEL, "Broadcast Already  Streaming :" + exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
