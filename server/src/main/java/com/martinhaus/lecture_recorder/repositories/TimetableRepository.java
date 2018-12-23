package com.martinhaus.lecture_recorder.repositories;

import com.martinhaus.lecture_recorder.model.timetables.Timetable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableRepository extends CrudRepository<Timetable, Long> {
}
