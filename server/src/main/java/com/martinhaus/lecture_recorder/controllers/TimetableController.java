package com.martinhaus.lecture_recorder.controllers;

import com.martinhaus.lecture_recorder.model.timetables.Timetable;
import com.martinhaus.lecture_recorder.services.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TimetableController {

    private final TimetableService timetableService;

    @Autowired
    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping(value = "/timetables/available")
    public ResponseEntity<java.util.List<com.martinhaus.lecture_recorder.model.timetables.RoomOption>> getAvailableTimetables() throws IOException {
        return new ResponseEntity<>(timetableService.getAvailableTimetables(), HttpStatus.OK);
    }

    @GetMapping(value = "/timetables/update")
    public ResponseEntity updateAllTimetables() throws IOException {
        timetableService.updateAllTimetables();
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/room/{id}/timetable")
    public ResponseEntity<com.martinhaus.lecture_recorder.model.timetables.Timetable> getByRoomId(@PathVariable("id") Long id) {
        Timetable timetable = timetableService.getByRoomId(id);
        if (timetable == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(timetable, HttpStatus.OK);
    }
}
