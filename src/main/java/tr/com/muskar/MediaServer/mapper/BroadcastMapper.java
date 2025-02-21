package tr.com.muskar.MediaServer.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.com.muskar.MediaServer.entity.Broadcast;
import tr.com.muskar.MediaServer.entity.broadcast.BroadcastDto;

@Component
@RequiredArgsConstructor
public class BroadcastMapper {

    public void updateBroadcastWithDto(Broadcast broadcast, BroadcastDto broadcastDto) {
        broadcast.setBroadcastDescription(broadcastDto.streamDescription());
        broadcast.setBroadcastName(broadcastDto.streamName());
        broadcast.setSourceUrl(broadcastDto.streamUrl());
    }
}
