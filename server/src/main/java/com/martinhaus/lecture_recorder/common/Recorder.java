package com.martinhaus.lecture_recorder.common;

import com.martinhaus.lecture_recorder.model.Recording;
import com.martinhaus.lecture_recorder.services.RecordingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Recorder {

    private static final Logger logger = LogManager.getLogger(Recorder.class);

    final private
    RecordingService recordingService;

    @Value("${spring.recording.script.path}")
    private String recordingScriptPath;

    @Value("${spring.recording.data.location}")
    private String outputDir;

    @Value("${spring.recording.container}")
    private String recordingContainer;


    @Autowired
    public Recorder(RecordingService recordingService) {
        this.recordingService = recordingService;
    }

    @Async
    public void runRecordingScript(Recording recording) throws IOException, InterruptedException {

        //Load params for recording script
        String cameraIpAdress = recording.getRoom().getIpAdress();
        String audioSource = recording.getRoom().getAudioSource();
        String outputFileName = String.format("%s_%s_%s.%s",
                                recording.getStartTime(),
                recording.getEndTime(),
                recording.getTitle(),
                recordingContainer);
        String outputFilePath = outputDir + outputFileName;

        ProcessBuilder pb = new ProcessBuilder(recordingScriptPath, cameraIpAdress, audioSource, outputFilePath);

        // Start the process
        Process p = pb.start();

        // Set pid and active flag
        recording.setPid(p.pid());
        recording.setActive(true);
        recording.setFileName(outputFileName);
        // Update recording in DB
        recordingService.saveRecording(recording);

        logger.info("Started recording of {} scheduled at {}", recording.getTitle(), recording.getStartTime());

        // Wait for the process to end
        p.waitFor();

        // Process finished succesfully
        if (p.exitValue() == 0) {
            recording.setFinished(true);
            recording.setActive(false);
            recordingService.saveRecording(recording);

            logger.info("Succesfully finished recording of {} starting at {}, ending at {}, located at {}",
                    recording.getTitle(),
                    recording.getStartTime(),
                    recording.getEndTime(),
                    recording.getFileName());
        // There was an error during recording
        } else {
            recording.setError(true);
            recordingService.saveRecording(recording);
            logger.info("Error during recording of {} starting at {}, ending at {}",
                    recording.getTitle(),
                    recording.getStartTime(),
                    recording.getEndTime());
        }

    }


}
