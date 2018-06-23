package com.martinhaus.lecture_recorder.services;

import com.martinhaus.lecture_recorder.model.Room;
import com.martinhaus.lecture_recorder.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<Room> getAll() {
        return roomRepository.findAll();
    }
}
