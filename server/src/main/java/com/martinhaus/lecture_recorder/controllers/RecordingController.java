package com.martinhaus.lecture_recorder.controllers;

import com.martinhaus.lecture_recorder.model.Recording;
import com.martinhaus.lecture_recorder.services.RecordingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RestController
public class RecordingController {

    private static final Logger logger = LogManager.getLogger(RecordingController.class);

    private final
    RecordingService recordingService;

    @Autowired
    public RecordingController(RecordingService recordingService) {
        this.recordingService = recordingService;
    }

    @PostMapping(value = "/recording/schedule")
    public ResponseEntity scheduleRecording(@RequestBody Recording recording) {
        recordingService.saveRecording(recording);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/recordings")
    public ResponseEntity<java.util.List<Recording>> getAllRecordings() {
        return new ResponseEntity<>(recordingService.getAllRecordings() ,HttpStatus.OK);
    }

    @GetMapping(value = "/recording/{id}")
    public ResponseEntity<Recording> getRecording(@PathVariable("id") long id) {
        return new ResponseEntity<>(recordingService.getRecording(id), HttpStatus.OK);
    }

    @GetMapping(path = "/recording/{id}/download")
    public void downloadRecording(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response) {
        recordingService.sendRecordingFile(id, request, response);
    }

    @RequestMapping(path= "/recording/{id}/delete")
    public ResponseEntity deleteRecording(@PathVariable("id") long id) {
        try {
            recordingService.deleteRecording(id);
        } catch (IOException e) {
            logger.error("Error while deleting recording", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(path = "/recording/{id}/create_download_link")
    public ResponseEntity<String> createUniqueDownloadLink(@PathVariable("id") Long id) {
        return new ResponseEntity<>(recordingService.createUniqueDownloadLink(id), HttpStatus.OK);
    }

    @GetMapping(path = "/download/{uuid}")
    public void downloadRecording(@PathVariable("uuid") UUID uuid, HttpServletRequest request, HttpServletResponse response) {
        Recording recording = recordingService.getRecordingByDownloadUUID(uuid);
        recordingService.sendRecordingFile(recording.getId(), request, response);
    }
}


