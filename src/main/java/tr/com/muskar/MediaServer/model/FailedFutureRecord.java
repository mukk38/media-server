package tr.com.muskar.MediaServer.model;

import tr.com.muskar.MediaServer.entity.Broadcast;
import tr.com.muskar.MediaServer.entity.BroadcastGroup;
import tr.com.muskar.MediaServer.entity.enums.BroadcastType;

import java.time.LocalDateTime;

public record FailedFutureRecord(BroadcastGroup broadcastGroup, Broadcast broadcast, BroadcastType type,
                                 LocalDateTime localDateTime, int retryCount) {
}
