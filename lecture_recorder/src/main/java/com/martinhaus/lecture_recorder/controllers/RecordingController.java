package com.martinhaus.lecture_recorder.controllers;

import com.martinhaus.lecture_recorder.model.Recording;
import com.martinhaus.lecture_recorder.services.RecordingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RecordingController {

    final private
    RecordingService recordingService;

    @Autowired
    public RecordingController(RecordingService recordingService) {
        this.recordingService = recordingService;
    }

    @RequestMapping(value = "/recording/schedule", method = POST)
    public ResponseEntity scheduleRecording(@RequestBody Recording recording) {
        recordingService.scheduleRecording(recording);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/recordings", method = GET)
    public ResponseEntity getAllRecordings() {
        return new ResponseEntity<>(recordingService.getAllRecordings() ,HttpStatus.OK);
    }

    @RequestMapping(value = "/recording/{id}", method = GET)
    public ResponseEntity getRecording(@PathVariable("id") long id) {
        return new ResponseEntity<>(recordingService.getRecording(id), HttpStatus.OK);
    }
}


