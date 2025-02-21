package tr.com.muskar.MediaServer.entity;


import lombok.Data;

@Data
public class BroadcastGroupSettings {


    private boolean isHlsEnabled = true;
    private int hlsListSize = 5;
    private int hlsTime = 5;

    private boolean isDashEnabled = true;
    private int dashSegmentDuration = 6;
    private double dashFragmentDuration = 0.5;
    private int dashWindowSize = 5;
    private int dashExtraWindowSize = 5;
}
