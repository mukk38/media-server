package tr.com.muskar.MediaServer.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tr.com.muskar.MediaServer.entity.dto.BroadcastGroupDto;

import java.util.List;

@Data
@Document
public class BroadcastGroup {

    @Id
    private ObjectId id;
    private String broadcastGroupName;
    private String broadcastGroupDescription;


    private int hlsListSize = 5;
    private int hlsTime = 5;


    private int dashSegmentDuration = 6;
    private double dashFragmentDuration = 0.5;
    private int dashWindowSize = 5;
    private int dashExtraWindowSize = 5;

    private List<Broadcast> broadcastList;

    public BroadcastGroup(BroadcastGroupDto broadcastGroupDto) {
        this.id = new ObjectId(broadcastGroupDto.id());
        this.broadcastGroupName = broadcastGroupDto.broadcastGroupName();
        this.broadcastGroupDescription = broadcastGroupDto.broadcastGroupDescription();
    }

    public void updateSettings(BroadcastGroupSettings broadcastGroupSettings) {

        this.hlsListSize = broadcastGroupSettings.getHlsListSize();
        this.hlsTime = broadcastGroupSettings.getHlsTime();
        this.dashSegmentDuration = broadcastGroupSettings.getDashSegmentDuration();
        this.dashFragmentDuration = broadcastGroupSettings.getDashFragmentDuration();
        this.dashWindowSize = broadcastGroupSettings.getDashWindowSize();
        this.dashExtraWindowSize = broadcastGroupSettings.getDashExtraWindowSize();
    }
}
