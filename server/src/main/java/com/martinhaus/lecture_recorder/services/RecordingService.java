package com.martinhaus.lecture_recorder.services;

import com.martinhaus.lecture_recorder.model.Recording;
import com.martinhaus.lecture_recorder.repositories.RecordingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecordingService {

    @Value("${spring.recording.data.location}")
    private String outputDir;

    final private
    RecordingRepository recordingRepository;

    @Autowired
    public RecordingService(RecordingRepository recordingRepository) {
        this.recordingRepository = recordingRepository;
    }

    /**
     * Get all recordings
     * @return List of recordings
     */
    public List<Recording> getAllRecordings() {
        return recordingRepository.findAll();
    }

    /**
     * Get details of one recording
     * @return Recording
     */
    public Recording getRecording(long id) {
        return recordingRepository.findById(id);
    }

    public void saveRecording(Recording recording) {
        recordingRepository.save(recording);
    }

    public List<Recording> getScheduledRecordings(LocalDateTime scheduledStart) {
        return recordingRepository.findAllByStartTime(scheduledStart);
    }

    public File getRecordingFile(long id) {
        Recording recording = this.getRecording(id);
        File file = new File(outputDir + recording.getFileName());
        return file;
    }

    private void killActiveRecording(long pid) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("kill -9 " + Long.toString(pid));
    }

    public void deleteRecording(long id) throws IOException {
        Recording recording = recordingRepository.findById(id);

        if (recording.isActive()) {
            killActiveRecording(recording.getPid());
        }

        recordingRepository.delete(recording);
    }
}
