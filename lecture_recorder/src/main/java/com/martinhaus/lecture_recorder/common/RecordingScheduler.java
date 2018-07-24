package com.martinhaus.lecture_recorder.common;

import com.martinhaus.lecture_recorder.model.Recording;
import com.martinhaus.lecture_recorder.services.RecordingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class RecordingScheduler {

    private static final Logger logger = LogManager.getLogger(RecordingScheduler.class);

    private final
    RecordingService recordingService;

    private final
    Recorder recorder;

    @Autowired
    public RecordingScheduler(RecordingService recordingService, Recorder recorder) {
        this.recordingService = recordingService;
        this.recorder = recorder;
    }

    /**
     * Runs every minute
     */
    @Scheduled(cron = "0 * * * * *")
    public void checkSchedules() {
        // Get now and ignore miliseconds
        LocalDateTime now = LocalDateTime.now().withNano(0).withSecond(0    );
        List<Recording> toRecord = recordingService.getScheduledRecordings(now);

        for (Recording recording: toRecord) {
            try {
                recorder.runRecordingScript(recording);
            } catch (IOException e) {
                logger.error("Failed to load recording script ", e);
            } catch (InterruptedException e) {
                logger.error("Recording process was interupted ", e);
            }
        }
    }
}
