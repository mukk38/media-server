package tr.com.muskar.MediaServer.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tr.com.muskar.MediaServer.entity.BroadcastDash.BroadcastDash;
import tr.com.muskar.MediaServer.entity.BroadcastHls.BroadcastHls;
import tr.com.muskar.MediaServer.entity.broadcast.BroadcastDto;

import java.util.Objects;

@Document
@Data
public class Broadcast {

    @Id
    private ObjectId id;
    private String sourceUrl;
    private String broadcastName;
    private String broadcastDescription;
    private BroadcastHls broadcastHls;
    private BroadcastDash broadcastDash;
    private boolean isDashOpened = false;

    public Broadcast(BroadcastDto broadcastDto) {
        this.id = Objects.nonNull(broadcastDto.streamId()) ? new ObjectId(broadcastDto.streamId()) : new ObjectId();
        this.sourceUrl = broadcastDto.streamUrl();
        this.broadcastName = broadcastDto.streamName();
        this.broadcastDescription = broadcastDto.streamDescription();
    }
}
