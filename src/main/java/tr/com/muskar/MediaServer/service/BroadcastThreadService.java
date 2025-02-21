package tr.com.muskar.MediaServer.service;

import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tr.com.muskar.MediaServer.entity.Broadcast;
import tr.com.muskar.MediaServer.entity.BroadcastGroup;
import tr.com.muskar.MediaServer.entity.enums.BroadcastType;
import tr.com.muskar.MediaServer.exception.BroadcastAlreadyStreamingException;
import tr.com.muskar.MediaServer.listener.FfmpegEventListener;
import tr.com.muskar.MediaServer.model.FailedFutureRecord;
import tr.com.muskar.MediaServer.model.StreamInfo;
import tr.com.muskar.MediaServer.util.BroadcastUtil;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
public class BroadcastThreadService {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<String, Future<?>> streamTasks;
    private final Map<String, FailedFutureRecord> failedStreamTask;
    private final FfmpegEventListener ffmpegEventListener;
    @Value("${ffmpeg_path}")
    private String ffmpegPath;

    public void startBroadcast(BroadcastGroup broadcastGroup, Broadcast broadcast, BroadcastType type) {
        String outputPath = BroadcastUtil.getOutputPathName(broadcastGroup, broadcast, type);
        String taskName = BroadcastUtil.createTaskName(broadcastGroup, broadcast, type);
        if (streamTasks.containsKey(taskName)) {
            throw new BroadcastAlreadyStreamingException(taskName);
        }
        Future<?> future = executorService.submit(() -> processStream(broadcastGroup, broadcast, type, outputPath));
        streamTasks.put(taskName, future);

    }

    public void stopBroadcast(BroadcastGroup broadcastGroup, Broadcast broadcast, BroadcastType type) {
        String taskName = BroadcastUtil.createTaskName(broadcastGroup, broadcast, type);
        Future<?> future = streamTasks.get(taskName);
        if (future != null) {
            future.cancel(true);
            streamTasks.remove(taskName);
        } else {
            throw new BroadcastAlreadyStreamingException(taskName);
        }
    }

    private void processStream(BroadcastGroup broadcastGroup, Broadcast broadcast, BroadcastType type, String outputPath) {
        FFmpeg ffmpeg = BroadcastUtil.createFfmpegAccordingToStreamSourceurl(ffmpegPath, broadcast.getSourceUrl());
        BroadcastUtil.updateFfmpegWithSourceType(ffmpeg, broadcast.getSourceUrl());
        switch (type) {
            case HLS -> {
                ffmpeg
                        .addArguments("-f", "hls")
                        .addArguments("-hls_time", String.valueOf(broadcastGroup.getHlsTime()))
                        .addArguments("-hls_list_size", String.valueOf(broadcastGroup.getHlsListSize()))
                        .addArguments("-hls_flags", "delete_segments")
                        .setOutputListener(line -> {
                            if (line.contains("Connection timed out")) {
                                ffmpegEventListener.connectionTimeOutError(broadcastGroup, broadcast, type);
                                stopBroadcast(broadcastGroup, broadcast, type);
                                FailedFutureRecord failedFutureRecord = failedStreamTask.get(BroadcastUtil.createTaskName(broadcastGroup, broadcast, type));
                                if (Objects.nonNull(failedFutureRecord)) {
                                    failedStreamTask.put(BroadcastUtil.createTaskName(broadcastGroup, broadcast, type), new FailedFutureRecord(broadcastGroup, broadcast, type, LocalDateTime.now(), (failedFutureRecord.retryCount()) + 1));
                                } else {
                                    failedStreamTask.put(BroadcastUtil.createTaskName(broadcastGroup, broadcast, type), new FailedFutureRecord(broadcastGroup, broadcast, type, LocalDateTime.now(), 0));
                                }

                            }
                            if (line.contains("Input/output error")) {
                                ffmpegEventListener.inputOutputError(broadcastGroup, broadcast, type);
                                stopBroadcast(broadcastGroup, broadcast, type);
                                FailedFutureRecord failedFutureRecord = failedStreamTask.get(BroadcastUtil.createTaskName(broadcastGroup, broadcast, type));
                                if (Objects.nonNull(failedFutureRecord)) {
                                    failedStreamTask.put(BroadcastUtil.createTaskName(broadcastGroup, broadcast, type), new FailedFutureRecord(broadcastGroup, broadcast, type, LocalDateTime.now(), (failedFutureRecord.retryCount()) + 1));
                                } else {
                                    failedStreamTask.put(BroadcastUtil.createTaskName(broadcastGroup, broadcast, type), new FailedFutureRecord(broadcastGroup, broadcast, type, LocalDateTime.now(), 0));
                                }
                            }
                        })
                        .setProgressListener(progress -> {
                            double bitrate = progress.getBitrate();
                            if (bitrate == 0) {
                                ffmpegEventListener.broadcastStop(broadcastGroup, broadcast, type);
                            } else {
                                ffmpegEventListener.sendStreamInfo(new StreamInfo(bitrate, progress.getFps(), progress.getSpeed()));
                            }
                        })
                        .addArgument(outputPath + "/index.m3u8");
            }
            case DASH -> {
                ffmpeg.addArguments("-f", "dash")
                        .addArguments("-seg_duration", String.valueOf(broadcastGroup.getDashSegmentDuration()))
                        .addArguments("-frag_duration", String.valueOf(broadcastGroup.getDashFragmentDuration()))
                        .addArguments("-window_size", String.valueOf(broadcastGroup.getDashWindowSize()))
                        .addArguments("-extra_window_size", String.valueOf(broadcastGroup.getDashExtraWindowSize()))
                        .addArgument(outputPath + "/index.mpd");
            }
        }
        ffmpeg.execute();
    }
}
