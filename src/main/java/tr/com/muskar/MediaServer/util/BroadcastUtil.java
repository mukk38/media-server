package tr.com.muskar.MediaServer.util;

import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import lombok.experimental.UtilityClass;
import tr.com.muskar.MediaServer.entity.Broadcast;
import tr.com.muskar.MediaServer.entity.BroadcastGroup;
import tr.com.muskar.MediaServer.entity.enums.BroadcastType;

import java.nio.file.Paths;

@UtilityClass
public class BroadcastUtil {

    public String createTaskName(BroadcastGroup broadcastGroup, Broadcast broadcast, BroadcastType type) {
        return broadcastGroup.getBroadcastGroupName() + "-" + broadcast.getBroadcastName() + "-" + type.name();
    }

    public String getOutputPathName(BroadcastGroup broadcastGroup, Broadcast broadcast, BroadcastType type) {
        String outputBasePath = "/var/www/html/" + broadcastGroup.getBroadcastGroupName() + "/" + broadcast.getBroadcastName() + "/" + type.name();
        switch (type) {
            case HLS:
                outputBasePath = outputBasePath + "/index.m3u8";
            case DASH:
                outputBasePath = outputBasePath + "/index.mpd";
        }
        return outputBasePath;
    }

    public FFmpeg createFfmpegAccordingToStreamSourceurl(String ffmpegPath, String streamSourceUrl) {
        return FFmpeg.atPath(Paths.get(ffmpegPath))
                .addArgument("-i").addArgument(streamSourceUrl)
                .addArgument("-c:v").addArgument("libx264").addArgument("-preset").addArgument("veryfast")
                .addArgument("-crf").addArgument("23").addArgument("-c:a").addArgument("aac").addArgument("-b:a").addArgument("128k");
    }

    public void updateFfmpegWithSourceType(FFmpeg ffmpeg, String streamSourceUrl) {
        if (streamSourceUrl.startsWith("rtsp://")) {
            ffmpeg.addArguments("-rtsp_transport", "tcp").addArguments("-timeout", "5000000");
        }
        if (streamSourceUrl.startsWith("rtmp://") || streamSourceUrl.startsWith("rtmps://")) {
            ffmpeg.addArguments("-reconnect", "1").addArguments("-reconnect_streamed", "1").addArguments("-reconnect_delay_max", "5");
        }
    }

}
