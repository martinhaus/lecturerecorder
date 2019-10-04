package com.martinhaus.lecture_recorder.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinhaus.lecture_recorder.model.timetables.Lesson;
import com.martinhaus.lecture_recorder.model.timetables.RoomOption;
import com.martinhaus.lecture_recorder.model.timetables.Timetable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TimetableServiceTest {

    @Value("classpath:unparsed_timetable.html")
    Resource unparsedTimetableResource;

    @Value("classpath:timetableObject.txt")
    Resource parsedTimetableResource;

    Document unparsedTimetable;

    @BeforeAll
    void getWholeUnparsedTimetableSetup() throws IOException {
        unparsedTimetable = Jsoup.parse(unparsedTimetableResource.getFile(),"UTF-8");
    }

    /**
    * Unit testing for TimetableService - parsing AIS timetable
    * Test case - providing valid HTML
    * Product requirement: FR-8 Timetable parsing
    */
    /*@Test
    void parseTimetableDoc(@Autowired TimetableService timetableService) throws IOException {
        Timetable timetable = timetableService.parseTimetableDoc(unparsedTimetable);
        ObjectMapper mapper = new ObjectMapper();

        Timetable parsedTimetable = mapper.readValue(parsedTimetableResource.getFile(), Timetable.class);

        for (Lesson lesson: timetable.getLessonsList()) {
            lesson.setId(null);
        }

        for (Lesson lesson: parsedTimetable.getLessonsList()) {
            lesson.setId(null);
        }

        assertEquals(timetable.getLessonsList(), parsedTimetable.getLessonsList());
    }*/

    /**
     * Testing integration between DB and TimetableService
     * Requirement: G-6 Recording lectures from timetables
     */
    @Test
    void getAvailableTimetables(@Autowired TimetableService timetableService) throws IOException {
        List<RoomOption> roomOptionList = timetableService.getAvailableTimetables();
        assertFalse(roomOptionList.isEmpty());
    }

    /**
     * Testing integration between DB and TimetableService
     * Requirement: G-6 Recording lectures from timetables
     */
    @Test
    void getWholeUnparsedTimetable(@Autowired TimetableService timetableService) throws IOException {
        Document unparsedTimetable = timetableService.getWholeUnparsedTimetable("2474");
        assertNotNull(unparsedTimetable);
    }

    /**
     * Testing integration between DB and TimetableService
     * Requirement: G-6 Recording lectures from timetables
     */
    @Test
    void getLatestTermId(@Autowired TimetableService timetableService) throws IOException {
        String id = timetableService.getLatestTermId();
        assertNotNull(id);
    }
}
