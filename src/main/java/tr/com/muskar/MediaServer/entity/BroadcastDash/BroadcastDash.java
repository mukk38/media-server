package tr.com.muskar.MediaServer.entity.BroadcastDash;

import lombok.Data;
import tr.com.muskar.MediaServer.entity.enums.BroadcastStatus;

@Data
public class BroadcastDash {

    private boolean dashIsOpened;
    private BroadcastStatus dashStatus;
}
