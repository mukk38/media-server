package tr.com.muskar.MediaServer.websocket.model;

import lombok.Data;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Data

public class SessionData {

    private WebSocketSession session;
    private String userName;
    private Lock sessionLock;
    private final static int TRY_LOCK_TIMEOUT_FOR_SEND = 5;

    public SessionData(WebSocketSession session, String userName) {
        this.session = session;
        this.userName = userName;
        this.sessionLock = new ReentrantLock();
    }

    public void sendMessage(String message) {
        if (session == null || !session.isOpen()) {

            return;
        }
        try {
            if (sessionLock.tryLock(TRY_LOCK_TIMEOUT_FOR_SEND, TimeUnit.MILLISECONDS)) {
                try {
                    session.sendMessage(new TextMessage(message));
                } finally {
                    sessionLock.unlock();
                }
            } else {
                System.out.println("Failed to acquire lock for sending WebSocket message.");
            }
        } catch (Exception e) {
            System.err.println("Error sending WebSocket message: " + e.getMessage());
        }
    }

}
