package com.martinhaus.lecture_recorder.services;

import com.martinhaus.lecture_recorder.model.Room;
import com.martinhaus.lecture_recorder.repositories.RoomRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    public void saveRoom(Room room) {
        roomRepository.save(room);
    }

    public Room getRoomById(long id) throws NotFoundException {
        Room room = roomRepository.findById(id);

        if (room == null) {
            throw new NotFoundException("Room not found");
        }

        return room;
    }
}
