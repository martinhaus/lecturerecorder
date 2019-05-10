package com.martinhaus.lecture_recorder.controllers;

import com.martinhaus.lecture_recorder.model.Recording;
import com.martinhaus.lecture_recorder.services.RecordingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecordingController.class)
@ExtendWith(SpringExtension.class)
class RecordingControllerTest {

    @MockBean
    private RecordingService recordingService;

    @Autowired
    private MockMvc mvc;

    /**
     * Unit testing for Recording Rest Controller
     * Product requirement: G-3 Storing recordings
     */
    @Test
    void getAllRecordings() throws Exception {
        given(recordingService.getAllRecordings()).willReturn(new ArrayList<Recording>());
        this.mvc.perform(get("/recordings")).andExpect(status().isOk());
    }

    /**
     * Unit testing for Recording Rest Controller
     * Product requirement: G-3 Storing recordings
     */
    @Test
    void getRecording_returnNormalRecording() throws Exception {
        given(recordingService.getRecording(anyLong())).willReturn(new Recording());
        this.mvc.perform(get("/recording/1")).andExpect(status().isOk());
    }

    /**
     * Unit testing for Recording Rest Controller
     * Test case for 404 not found
     * Product requirement: G-3 Storing recordings
     */
    @Test
    void getRecording_returnNull() throws Exception {
        given(recordingService.getRecording(anyLong())).willReturn(null);
        this.mvc.perform(get("/recording/1")).andExpect(status().is4xxClientError());
    }

    /**
     * Unit testing for Recording Rest Controller
     * Test case when recording file is not available
     * Product requirement: G-3 Storing recordings
     */
    @Test
    void deleteRecording_recordingNotAvailable() throws Exception {
        doThrow(new IOException()).when(recordingService).deleteRecording(anyLong());
        this.mvc.perform(get("/recording/1/delete")).andExpect(status().is5xxServerError());
    }
}