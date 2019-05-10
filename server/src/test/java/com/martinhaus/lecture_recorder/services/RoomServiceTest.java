package com.martinhaus.lecture_recorder.services;

import com.martinhaus.lecture_recorder.model.Room;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class RoomServiceTest {

    @Autowired
    RoomService roomService;

    /**
     * Testing integration between DB and RoomService
     * Requirement: G-3 Storing recordings
     */
    @ParameterizedTest
    @CsvSource({
            "Aula Magna, 776",
            "Aula Magna 2, 797"
    })
    void RoomServiceSelectionById(String expected, Long id) throws NotFoundException {
        assertEquals(expected, roomService.getRoomById(id).getName());
    }

    /**
     * Testing integration between DB and RoomService
     * Requirement: G-3 Storing recordings
     */
    @Test
    void RoomServiceGetAll() {
        List rooms = roomService.getAll();
        assertEquals(2, rooms.size());
    }

    /**
     * Testing integration between DB and RoomService
     * Requirement: G-3 Storing recordings
     */
    @ParameterizedTest
    @CsvSource({
            "Aula Minor, minor, 192.168.0.1",
            "Test,,",
    })
    void RoomServiceInsertionDeletionTest(String name, String audioSource, String cameraIpAddress) {
        Room room = new Room(name, audioSource, cameraIpAddress);
        try {
            roomService.saveRoom(room);
            roomService.deleteRoom(room.getId());
        } catch (IOException e) {
            fail();
        }
    }
}