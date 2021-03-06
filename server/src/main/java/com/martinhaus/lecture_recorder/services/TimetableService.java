package com.martinhaus.lecture_recorder.services;

import com.martinhaus.lecture_recorder.common.utils.TimetableParser;
import com.martinhaus.lecture_recorder.model.Room;
import com.martinhaus.lecture_recorder.model.timetables.Lesson;
import com.martinhaus.lecture_recorder.model.timetables.RoomOption;
import com.martinhaus.lecture_recorder.model.timetables.Timetable;
import com.martinhaus.lecture_recorder.repositories.RoomRepository;
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

    private final RoomRepository roomRepository;

    private final
    LessonService lessonService;

    @Autowired
    public TimetableService(TimetableRepository timetableRepository, LessonService lessonService, RoomRepository roomRepository) {
        this.timetableRepository = timetableRepository;
        this.lessonService = lessonService;
        this.roomRepository = roomRepository;
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

    protected String getLatestTermId() throws IOException {
        Document doc = Jsoup.connect(timetableFiitsUrl).get();

        Element termTable = doc.getElementsByTag("table").first();
        int rowCount = termTable.getElementsByTag("tr").size();

        Element lastRow = termTable.getElementsByTag("tr").last();

        while (lastRow.getElementsByTag("td").get(2).text().contains("doktorandské štúdiá")) {
            rowCount--;
            lastRow = termTable.getElementsByTag("tr").get(rowCount - 1);
        }

        Element latestTerm = lastRow.getElementsByAttributeValue("type", "checkbox").last();
        return latestTerm.val();
    }

    public Document getWholeUnparsedTimetable(String timetableId) throws IOException {
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

    public Timetable parseTimetableDoc(Document timetableDocument) {

        List<Lesson> lessons = new ArrayList<>();
        int begin = TimetableParser.getStartHourFromTimetableHeader(timetableDocument);


        //Get starting time in colspan, 12 colspan == 1 hour
        int start = TimetableParser.hourToColspan(begin);

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
                        Lesson lesson = null;
                        if (!day.equals("") && start != 0) {
                            lesson = TimetableParser.parseLessonEntry(dataElement, start, day);
                            start += Integer.valueOf(dataElement.attr("colspan"));
                        }

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
            start = TimetableParser.hourToColspan(begin);;
        }

        return Timetable.builder().lessonsList(lessons).build();
    }

    void loadLatestTimetable(Room room) throws IOException {
        Timetable timetable = parseTimetableDoc(getWholeUnparsedTimetable(room.getTimetableId()));
        timetable.setRoom(room);
        timetableRepository.save(timetable);
    }

    public Timetable getByRoom(Room room) {
        return timetableRepository.findByRoom(room);
    }

    public Timetable getByRoomId(long id) {
        Room room = roomRepository.findById(id);
        return timetableRepository.findByRoom(room);
    }

    private void updateTimetable(Timetable timetable) throws IOException {
        String timetableId = timetable.getRoom().getTimetableId();
        Timetable t = parseTimetableDoc(getWholeUnparsedTimetable(timetableId));
        timetable.getLessonsList().clear();
        timetable.getLessonsList().addAll(t.getLessonsList());
        timetable.setTermTitle(t.getTermTitle());
        timetableRepository.save(timetable);
    }

    public void updateAllTimetables() throws IOException {
        List<Timetable> allTimetables = getAllTimetables();

        // Search all timetables for match with available ones
        for (Timetable timetable:allTimetables) {
            updateTimetable(timetable);
        }
    }

    private List<Timetable> getAllTimetables() {
        return  (List<Timetable>) timetableRepository.findAll();
    }
}
