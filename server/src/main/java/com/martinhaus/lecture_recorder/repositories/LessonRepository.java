package com.martinhaus.lecture_recorder.repositories;

import com.martinhaus.lecture_recorder.model.timetables.Lesson;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, Long> {
}
