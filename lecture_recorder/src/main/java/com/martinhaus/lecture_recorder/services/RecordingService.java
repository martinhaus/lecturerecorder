package com.martinhaus.lecture_recorder.services;

import com.martinhaus.lecture_recorder.model.Recording;
import com.martinhaus.lecture_recorder.repositories.RecordingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecordingService {

    final private
    RecordingRepository recordingRepository;

    @Autowired
    public RecordingService(RecordingRepository recordingRepository) {
        this.recordingRepository = recordingRepository;
    }

    /**
     * Get all recordings
     * @return
     */
    public List<Recording> getAllRecordings() {
        return recordingRepository.findAll();
    }

    /**
     * Get details of one recording
     * @return
     */
    public Recording getRecording(long id) {
        return recordingRepository.findById(id);
    }

    public void scheduleRecording(Recording recording) {
        recordingRepository.save(recording);
    }

    /**
     * Runs every minute
     */
    @Scheduled(cron = "0 * * * * *")
    public void checkSchedules() {
        // Get now and ignore miliseconds
        LocalDateTime now = LocalDateTime.now().withNano(0);
        List<Recording> toRecord = recordingRepository.findAllByStartTime(now);
        System.out.println(toRecord.toString());
    }
}
