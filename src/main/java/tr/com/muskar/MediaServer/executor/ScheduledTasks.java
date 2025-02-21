package tr.com.muskar.MediaServer.executor;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tr.com.muskar.MediaServer.model.FailedFutureRecord;
import tr.com.muskar.MediaServer.service.BroadcastThreadService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final BroadcastThreadService broadcastThreadService;
    private final Map<String, FailedFutureRecord> failedStreamTask;

    @Scheduled(initialDelay = 2, fixedDelay = 2, timeUnit = TimeUnit.MINUTES)
    public void checkFailedStreamTask() {
        for (Map.Entry<String, FailedFutureRecord> failedFutureRecordEntry : failedStreamTask.entrySet()) {
            long minutesSinceLastAttempt = Duration.between(failedFutureRecordEntry.getValue().localDateTime(), LocalDateTime.now()).toMinutes();
            int retryCount = failedFutureRecordEntry.getValue().retryCount();

            long requiredWaitTime = retryCount * 10;
            if (retryCount == 0 || minutesSinceLastAttempt >= requiredWaitTime) {
                System.out.println("Yayını tekrar başlatıyorum: " + failedFutureRecordEntry.getValue().broadcast().getBroadcastName());
                broadcastThreadService.startBroadcast(failedFutureRecordEntry.getValue().broadcastGroup(), failedFutureRecordEntry.getValue().broadcast(), failedFutureRecordEntry.getValue().type());
            }
        }
    }
}
