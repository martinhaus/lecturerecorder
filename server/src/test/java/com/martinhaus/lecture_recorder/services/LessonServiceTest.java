package com.martinhaus.lecture_recorder.services;

import com.martinhaus.lecture_recorder.model.timetables.Lesson;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class LessonServiceTest {

    @Autowired
    LessonService lessonService;


    /**
     * Testing integration between LessonService and DB
     * Product requirement: G-6 Recording lectures from timetables
     */
    @ParameterizedTest
    @CsvSource({
            "test, test, 0",
            "test, test, 5"
    })
    void saveLesson(String title, String teacher, short dayOfWeek) {
        Lesson lesson = Lesson.builder()
                .title(title)
                .dayOfWeek(dayOfWeek)
                .teacher(teacher)
                .startTime(new Date())
                .endTime(new Date())
                .build();

        lessonService.saveLesson(lesson);
    }
}