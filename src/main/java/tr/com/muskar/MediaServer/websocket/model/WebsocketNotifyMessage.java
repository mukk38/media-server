package tr.com.muskar.MediaServer.websocket.model;

import tr.com.muskar.MediaServer.entity.enums.BroadcastType;
import tr.com.muskar.MediaServer.websocket.model.enums.StreamStatus;

public record WebsocketNotifyMessage(String broadcastGroupName, String broadcastName, BroadcastType broadcastType,
                                     StreamStatus streamStatus) {
}
