package com.martinhaus.lecture_recorder.controllers;

import com.martinhaus.lecture_recorder.services.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TimetableController {

    private final TimetableService timetableService;

    @Autowired
    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @RequestMapping(value = "/timetables/available", method = RequestMethod.GET)
    public ResponseEntity<java.util.List<com.martinhaus.lecture_recorder.model.timetables.RoomOption>> getAvailableTimetables() throws IOException {
        return new ResponseEntity<>(timetableService.getAvailableTimetables(), HttpStatus.OK);
    }
}
