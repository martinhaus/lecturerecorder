package com.martinhaus.lecture_recorder.services;

import com.martinhaus.lecture_recorder.model.timetables.Lesson;
import com.martinhaus.lecture_recorder.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;

    @Autowired
    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public void saveLesson(Lesson lesson) {
        lessonRepository.save(lesson);
    }
}
