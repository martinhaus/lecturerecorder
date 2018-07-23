package com.martinhaus.lecture_recorder.common;

import com.martinhaus.lecture_recorder.model.Recording;
import com.martinhaus.lecture_recorder.services.RecordingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RecordingScheduler {

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
        System.out.println(toRecord.toString());
        for (Recording recording: toRecord) {
            recorder.runRecordingScript(recording);
        }
    }
}
