package com.martinhaus.lecture_recorder.services;

import com.martinhaus.lecture_recorder.model.Room;
import com.martinhaus.lecture_recorder.repositories.RoomRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final TimetableService timetableService;

    @Autowired
    public RoomService(RoomRepository roomRepository, TimetableService timetableService) {
        this.roomRepository = roomRepository;
        this.timetableService = timetableService;
    }

    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    public void saveRoom(Room room) throws IOException {
        roomRepository.save(room);
        if (room.getTimetableId() != null) {
            timetableService.loadLatestTimetable(room);
        }
    }

    public Room getRoomById(long id) throws NotFoundException {
        Room room = roomRepository.findById(id);

        if (room == null) {
            throw new NotFoundException("Room not found");
        }

        return room;
    }
}
