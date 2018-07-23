package com.martinhaus.lecture_recorder.common;

import com.martinhaus.lecture_recorder.model.Recording;
import com.martinhaus.lecture_recorder.services.RecordingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Recorder {

    final private
    RecordingService recordingService;

    @Value("${spring.recording.script.path}")
    private String recordingScriptPath;

    @Autowired
    public Recorder(RecordingService recordingService) {
        this.recordingService = recordingService;
    }

    @Async
    public void runRecordingScript(Recording recording) {
        // Load recording script
        ProcessBuilder pb = new ProcessBuilder(recordingScriptPath);

        try {
            // Start the process
            Process p = pb.start();

            // Set pid and active flag
            recording.setPid(p.pid());
            recording.setActive(true);
            // Update recording in DB
            recordingService.saveRecording(recording);
            p.waitFor();

            if (p.exitValue() == 0) {
                recording.setFinished(true);
                recording.setActive(false);
                recordingService.saveRecording(recording);
                //TODO logger
            }
            else {
                recording.setError(true);
                recordingService.saveRecording(recording);
                //TODO logger
            }
        } catch (IOException | InterruptedException e) {
            //TODO logger
            e.printStackTrace();
        }
    }


}
