package com.martinhaus.lecture_recorder.controllers;

import com.martinhaus.lecture_recorder.services.RecordingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class RecordingController {

    final private
    RecordingService recordingService;

    @Autowired
    public RecordingController(RecordingService recordingService) {
        this.recordingService = recordingService;
    }

    @RequestMapping(value = "/recording/schedule", method = GET)
    public ResponseEntity scheduleRecording() {
        return null;
    }

    @RequestMapping(value = "/recordings", method = GET)
    public ResponseEntity getAllRecordings() {
        return new ResponseEntity<>(recordingService.getAllRecordings() ,HttpStatus.OK);
    }
}


