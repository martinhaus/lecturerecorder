package com.martinhaus.lecture_recorder.services;

import com.martinhaus.lecture_recorder.common.MultiPartFileSender;
import com.martinhaus.lecture_recorder.model.Recording;
import com.martinhaus.lecture_recorder.model.RecordingDownload;
import com.martinhaus.lecture_recorder.repositories.RecordingDownloadRepository;
import com.martinhaus.lecture_recorder.repositories.RecordingRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class RecordingService {

    @Value("${spring.recording.data.location}")
    private String outputDir;

    private final
    RecordingRepository recordingRepository;
    private final RecordingDownloadRepository recordingDownloadRepository;

    @Autowired
    public RecordingService(RecordingRepository recordingRepository, RecordingDownloadRepository recordingDownloadRepository) {
        this.recordingRepository = recordingRepository;
        this.recordingDownloadRepository = recordingDownloadRepository;
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

    public Recording saveRecording(Recording recording) {
        recordingRepository.save(recording);
        return  recording;
    }

    public List<Recording> getScheduledRecordings(LocalDateTime scheduledStart) {
        return recordingRepository.findAllByStartTime(scheduledStart);
    }

    public File getRecordingFile(long id) {
        Recording recording = this.getRecording(id);
        return new File(outputDir + recording.getFileName());
    }

    private void killActiveRecording(long pid) throws IOException {
        Runtime rt = Runtime.getRuntime();
        rt.exec("kill -9 " + pid);
    }

    public void deleteRecording(long id) throws IOException {
        Recording recording = recordingRepository.findById(id);

        if (recording.isActive()) {
            killActiveRecording(recording.getPid());
        }
        deleteRecordingFile(recording);
        recordingRepository.delete(recording);
    }

    private void deleteRecordingFile(Recording recording) throws IOException {
        String path = outputDir + recording.getFileName();
        Files.deleteIfExists(Paths.get(path));

    }

    public Recording getRecordingByDownloadUUID(UUID uuid) {
        RecordingDownload recordingDownload = recordingDownloadRepository.findByUuid(uuid.toString());
        return recordingDownload.getRecording();
    }

    public String createUniqueDownloadLink(Long id) {
        Recording recording = getRecording(id);
        RecordingDownload recordingDownload = new RecordingDownload(recording);
        recordingDownloadRepository.save(recordingDownload);
        return recordingDownload.getUuid();
    }

    public void sendRecordingFile(Long id, HttpServletRequest request, HttpServletResponse response) {
        File file = getRecordingFile(id);
        Path path = Paths.get(file.getAbsolutePath());

        try {
            MultiPartFileSender.fromPath(path)
                    .with(request)
                    .with(response)
                    .serveResource();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }
}
