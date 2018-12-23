package com.martinhaus.lecture_recorder.services;

import com.martinhaus.lecture_recorder.common.utils.TimetableParser;
import com.martinhaus.lecture_recorder.model.Room;
import com.martinhaus.lecture_recorder.model.timetables.Lesson;
import com.martinhaus.lecture_recorder.model.timetables.RoomOption;
import com.martinhaus.lecture_recorder.model.timetables.Timetable;
import com.martinhaus.lecture_recorder.repositories.TimetableRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimetableService {

    @Value("${timetables.fiit.base-url}")
    private String timetableFiitsUrl;

    @Value("${timetables.ais.base-url}")
    private String timetablesAisUrl;

    private final
    TimetableRepository timetableRepository;

    private final
    LessonService lessonService;

    @Autowired
    public TimetableService(TimetableRepository timetableRepository, LessonService lessonService) {
        this.timetableRepository = timetableRepository;
        this.lessonService = lessonService;
    }

    /**
     * Find all available timetables
     * @return List of room names and ids
     * @throws IOException
     */
    public List<RoomOption> getAvailableTimetables() throws IOException {

        String lastTermId = getLatestTermId();

        Document doc = Jsoup.connect(timetablesAisUrl).data("rozvrh", lastTermId).post();
        Elements rooms = doc.select("select[name=\"mistnost\"]").select("option");

        List<RoomOption> roomOptions = new ArrayList<>();

        for (Element room: rooms) {
            roomOptions.add(new RoomOption(Short.valueOf(room.val()), room.text()));
        }

        // Remove --all rooms-- option
        roomOptions.remove(0);

        return roomOptions;
    }

    private String getLatestTermId() throws IOException {
        Document doc = Jsoup.connect(timetableFiitsUrl).get();
        Element latestTerm = doc.getElementsByAttributeValue("type", "checkbox").last();
        return latestTerm.val();
    }

    private Document getWholeUnparsedTimetable(String timetableId) throws IOException {
        return Jsoup.connect(timetablesAisUrl)
                .data("mistnost", timetableId)
                .data("rozvrh", getLatestTermId())
                .data("den", "0")
                .data("f", "70")
                .data("format", "html")
                .data("garant", "0")
                .data("lang", "sk")
                .data("predmet", "0")
                .data("rocnik", "0")
                .data("skupina", "0")
                .data("studijni_zpet", "0")
                .data("ucitel", "0")
                .data("ustav", "0")
                .data("z", "0")
                .data("zobraz", "Zobraziť")
                .post();
    }

    private Timetable parseTimetableDoc(Document timetableDocument) {

        List<Lesson> lessons = new ArrayList<>();
        int begin = TimetableParser.getStartHourFromTimetableHeader(timetableDocument);

        //Updates start time as a start of the new day
        int start = begin;

        //Extract just first table in HTML - it has the timetable
        Element tableElement = timetableDocument.getElementsByTag("table").get(0);

        //For each row in the table
        for (Element rowElement: tableElement.getElementsByTag("tr")) {
            String day = "";
            for (Element dataElement: rowElement.getElementsByTag("td")) {
                if ( (dataElement.className().equals("zahlavi")) && (dataElement.hasText()) ) {
                    day = dataElement.text();
                    //Eliminate dividing lines in the table
                }
                else if (!dataElement.className().equals("odsazena")) {
                    //Block larger than one hour
                    if ( !dataElement.attributes().get("colspan").isEmpty()) {
                        Lesson lesson = TimetableParser.parseLessonEntry(dataElement, start, day);
                        if (lesson != null) {
                            lessonService.saveLesson(lesson);
                            lessons.add(lesson);
                        }
                        //Empty gap
                    } else if(!(dataElement.className().contains("zahlavi"))) {
                        start ++;
                    }
                 }
            }
            start = begin;
        }

        return Timetable.builder().lessonsList(lessons).build();
    }

    void loadLatestTimetable(Room room) throws IOException {
        Timetable timetable = parseTimetableDoc(getWholeUnparsedTimetable(room.getTimetableId()));
        timetable.setRoom(room);
        timetableRepository.save(timetable);
    }

}
