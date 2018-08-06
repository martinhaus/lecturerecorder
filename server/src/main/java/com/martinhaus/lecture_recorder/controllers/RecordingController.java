package com.martinhaus.lecture_recorder.controllers;

import com.martinhaus.lecture_recorder.common.MultiPartFileSender;
import com.martinhaus.lecture_recorder.model.Recording;
import com.martinhaus.lecture_recorder.services.RecordingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        recordingService.saveRecording(recording);
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

    @RequestMapping(path = "/recording/{id}/download", method = RequestMethod.GET)
    public void downloadRecording(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response) {

        File file = recordingService.getRecordingFile(id);
        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(MediaType.parseMediaType("video/mp4"));
        respHeaders.setContentLength(file.length());
        respHeaders.setContentDispositionFormData("attachment", file.getName());
        Path path = Paths.get(file.getAbsolutePath());
        try {
            MultiPartFileSender.fromPath(path)
                    .with(request)
                    .with(response)
                    .serveResource();
        } catch (Exception e) {

        }
    }
}


