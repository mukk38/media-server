package tr.com.muskar.MediaServer.entity.broadcast;

public record BroadcastDto(String streamName, String streamDescription, String streamUrl, String streamId,
                           String broadcastGroupId) {
}
