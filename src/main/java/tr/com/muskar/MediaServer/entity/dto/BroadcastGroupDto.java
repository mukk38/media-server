package tr.com.muskar.MediaServer.entity.dto;

import tr.com.muskar.MediaServer.entity.BroadcastGroup;

public record BroadcastGroupDto(String id, String broadcastGroupName, String broadcastGroupDescription) {

    public BroadcastGroupDto(BroadcastGroup broadcastGroup) {
        this(broadcastGroup.getId().toString(), broadcastGroup.getBroadcastGroupName(), broadcastGroup.getBroadcastGroupDescription());
    }
}
