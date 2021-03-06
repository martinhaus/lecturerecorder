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
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

@Component
public class Recorder {

    private static final Logger logger = LogManager.getLogger(Recorder.class);

    private final
    RecordingService recordingService;

    @Value("${spring.recording.script.path}")
    private String recordingScriptPath;

    @Value("${spring.recording.data.location}")
    private String outputDir;

    @Value("${spring.recording.container}")
    private String recordingContainer;

    @Value("${spring.recording.api}")
    private String recordingAPI;

    @Value("${spring.recording.timeout}")
    private int timeout;

    private final EmailService emailService;

    @Autowired
    public Recorder(RecordingService recordingService, EmailService emailService) {
        this.recordingService = recordingService;
        this.emailService = emailService;
    }

    @Async
    public void runRecordingScript(Recording recording) throws IOException, InterruptedException {

        //Load params for recording script
        String cameraIpAdress = recording.getRoom().getIpAddress();
        String audioSource = recording.getRoom().getAudioSource();
        String outputFileName = String.format("%s_%s_%s_%s.%s",
                                recording.getStartTime(),
                recording.getEndTime(),
                recording.getRoom().getName(),
                recording.getTitle(),
                recordingContainer);
        String outputFilePath = outputDir + outputFileName;

        long duration = ChronoUnit.MILLIS.between(recording.getStartTime(), recording.getEndTime());
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz);
        String time = df.format(new Date(duration));

        int attemptsCount = 0;
        Process p;
        //Restart recording if process fails
        do{
            ProcessBuilder pb = new ProcessBuilder(recordingScriptPath, cameraIpAdress, audioSource, outputFilePath, time);
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
            // Start the process
            p = pb.start();

            // Set pid and active flag
            recording.setPid(p.pid());
            recording.setActive(true);
            recording.setFileName(outputFileName);
            // Update recording in DB
            recordingService.saveRecording(recording);

            logger.info("Started recording of {} scheduled at {}", recording.getTitle(), recording.getStartTime());

            // Wait for the process to end
            p.waitFor();

            attemptsCount++;
        }
        while(Duration.between(recording.getStartTime(), LocalDateTime.now()).toMinutes() <= timeout && p.exitValue() != 0 );

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

            if (recording.getEmail() != null) {
                String link = String.format("%sdownload/%s", recordingAPI, recordingService.createUniqueDownloadLink(recording.getId()));
                String subject = String.format("Your recording %s from %s - %s",
                        recording.getTitle(),
                        recording.getRoom().getName(),
                        recording.getStartTime());
                String msg = String.format("Hello, your recording %s from %s recorded at %s finished successfully.%nIt can be found at %s.",
                        recording.getTitle(),
                        recording.getRoom().getName(),
                        recording.getStartTime(),
                        link);
                if(attemptsCount > 1)
                {
                    msg = msg + " However, the recording could not start at specified time and started few minutes later.";
                }
                emailService.sendMessage(recording.getEmail(),subject, msg);
            }

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
