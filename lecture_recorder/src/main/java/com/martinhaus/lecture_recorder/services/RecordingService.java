package com.martinhaus.lecture_recorder.services;

import com.martinhaus.lecture_recorder.model.Recording;
import com.martinhaus.lecture_recorder.repositories.RecordingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordingService {

    final private
    RecordingRepository recordingRepository;

    @Autowired
    public RecordingService(RecordingRepository recordingRepository) {
        this.recordingRepository = recordingRepository;
    }

    public void scheduleRecording() {

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
}
