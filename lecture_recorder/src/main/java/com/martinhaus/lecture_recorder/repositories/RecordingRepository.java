package com.martinhaus.lecture_recorder.repositories;

import com.martinhaus.lecture_recorder.model.Recording;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordingRepository extends CrudRepository<Recording, Long> {
    List<Recording> findAll();
    Recording findById(long id);
}
