package com.martinhaus.lecture_recorder.controllers;

import com.martinhaus.lecture_recorder.common.EmailService;
import com.martinhaus.lecture_recorder.model.Room;
import com.martinhaus.lecture_recorder.services.RoomService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class RoomController {

    private final RoomService roomService;
    private final EmailService emailService;

    @Autowired
    public RoomController(RoomService roomService, EmailService emailService) {
        this.roomService = roomService;
        this.emailService = emailService;
    }

    @GetMapping(value = "/rooms")
    public ResponseEntity<java.util.List<Room>> getAllRooms() {
        return new ResponseEntity<>(roomService.getAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/rooms/add")
    public ResponseEntity addRoom(@RequestBody Room room) throws IOException {
        roomService.saveRoom(room);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/room/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable long id) throws NotFoundException {
        return new ResponseEntity<>(roomService.getRoomById(id), HttpStatus.OK);
    }
}
