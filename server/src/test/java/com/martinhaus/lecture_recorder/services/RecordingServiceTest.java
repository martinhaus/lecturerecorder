package com.martinhaus.lecture_recorder.services;

import com.martinhaus.lecture_recorder.model.Recording;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class RecordingServiceTest {

    @Autowired
    RecordingService recordingService;

    /**
     * Testing integration between RecordingService and DB
     * G-3 Storing recordings
     */
    @Test
    void getAllRecordings() {
        List recordings = recordingService.getAllRecordings();
        assertEquals(2, recordings.size());
    }

    /* Testing integration between RecordingService and DB
     * G-3 Storing recordings
     */
    @ParameterizedTest
    @CsvSource({
            "test, 924",
            "test, 926",
    })
    void getRecording(String expectedTitle, Long id) {
        Recording recording = recordingService.getRecording(id);
        assertEquals(expectedTitle, recording.getTitle());
    }
}