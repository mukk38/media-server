package tr.com.muskar.MediaServer.entity.BroadcastHls;

import lombok.Data;
import tr.com.muskar.MediaServer.entity.enums.BroadcastStatus;

@Data
public class BroadcastHls {

    private boolean hlsIsOpened;
    private BroadcastStatus hlsStatus;
    private Integer hlsSpeed;
    private Double hlsBitrate;
}
