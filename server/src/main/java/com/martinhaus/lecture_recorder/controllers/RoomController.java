package com.martinhaus.lecture_recorder.controllers;

import com.martinhaus.lecture_recorder.model.Room;
import com.martinhaus.lecture_recorder.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @RequestMapping(value = "/rooms", method = GET)
    public ResponseEntity getAllRooms() {
        return new ResponseEntity<>(roomService.getAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/rooms/add", method = POST)
    public ResponseEntity addRoom(@RequestBody Room room) {
        roomService.saveRoom(room);
        return new ResponseEntity(HttpStatus.OK);
    }
}
