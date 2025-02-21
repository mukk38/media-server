package tr.com.muskar.MediaServer.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tr.com.muskar.MediaServer.entity.Broadcast;
import tr.com.muskar.MediaServer.entity.BroadcastGroup;
import tr.com.muskar.MediaServer.entity.enums.BroadcastType;
import tr.com.muskar.MediaServer.listener.FfmpegEventListener;
import tr.com.muskar.MediaServer.model.StreamInfo;
import tr.com.muskar.MediaServer.websocket.message.WebsocketMessage;
import tr.com.muskar.MediaServer.websocket.model.SessionData;
import tr.com.muskar.MediaServer.websocket.model.WebsocketNotifyMessage;
import tr.com.muskar.MediaServer.websocket.model.enums.StreamStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketHandler extends TextWebSocketHandler implements FfmpegEventListener {

    private final Map<String, SessionData> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Yeni bir session websockete bağlandı: " + session.getId());

    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            WebsocketMessage webSocketMessage = objectMapper.readValue(message.getPayload(), WebsocketMessage.class);
            System.out.println(webSocketMessage.username() + " kullanıcısı bağlandı");
            sessions.put(session.getId(), new SessionData(session, webSocketMessage.username()));
        } catch (Exception e) {
            System.err.println(e.getMessage() + " error when parsin web socket message");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
    }

    @Override
    public void connectionTimeOutError(BroadcastGroup broadcastGroup, Broadcast broadcast, BroadcastType type) {
        WebsocketNotifyMessage websocketNotifyMessage = new WebsocketNotifyMessage(broadcastGroup.getBroadcastGroupName(), broadcast.getBroadcastName(), type, StreamStatus.CONNECTION_TIME_OUT_ERROR);
        sendMessage(websocketNotifyMessage);
    }

    @Override
    public void inputOutputError(BroadcastGroup broadcastGroup, Broadcast broadcast, BroadcastType type) {
        WebsocketNotifyMessage websocketNotifyMessage = new WebsocketNotifyMessage(broadcastGroup.getBroadcastGroupName(), broadcast.getBroadcastName(), type, StreamStatus.INPUT_OUTPUT_ERROR);
        sendMessage(websocketNotifyMessage);
    }

    @Override
    public void broadcastStop(BroadcastGroup broadcastGroup, Broadcast broadcast, BroadcastType type) {
        WebsocketNotifyMessage websocketNotifyMessage = new WebsocketNotifyMessage(broadcastGroup.getBroadcastGroupName(), broadcast.getBroadcastName(), type, StreamStatus.BROADCAST_STOP);
        sendMessage(websocketNotifyMessage);
    }

    @Override
    public void sendStreamInfo(StreamInfo streamInfo) {
        sendMessage(streamInfo);
    }

    private void sendMessage(Object websocketNotifyMessage) {
        try {
            String message = objectMapper.writeValueAsString(websocketNotifyMessage);
            for (SessionData session : sessions.values()) {
                session.sendMessage(message);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage() + " when deserializing WebsocketNotifyMessage");
        }
    }
}
