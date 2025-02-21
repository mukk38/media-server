package tr.com.muskar.MediaServer.listener;

import tr.com.muskar.MediaServer.entity.Broadcast;
import tr.com.muskar.MediaServer.entity.BroadcastGroup;
import tr.com.muskar.MediaServer.entity.enums.BroadcastType;
import tr.com.muskar.MediaServer.model.StreamInfo;

public interface FfmpegEventListener {

    void connectionTimeOutError(BroadcastGroup broadcastGroup, Broadcast broadcast, BroadcastType type);

    void inputOutputError(BroadcastGroup broadcastGroup, Broadcast broadcast, BroadcastType type);

    void broadcastStop(BroadcastGroup broadcastGroup, Broadcast broadcast, BroadcastType type);

    void sendStreamInfo(StreamInfo streamInfo);
}
