package com.martinhaus.lecture_recorder.repositories;

import com.martinhaus.lecture_recorder.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
    List<Room> findAll();
    Room findById(long id);
}
