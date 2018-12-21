package com.martinhaus.lecture_recorder.services;

import com.martinhaus.lecture_recorder.model.timetables.RoomOption;
import com.martinhaus.lecture_recorder.model.timetables.Timetable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

    /**
     * Find all available timetables
     * @return List of room names and ids
     * @throws IOException
     */
    public List<RoomOption> getAvailableTimetables() throws IOException {
        Document doc = Jsoup.connect(timetableFiitsUrl).get();

        // Get id of last term
        Element latest_term = doc.getElementsByAttributeValue("type", "checkbox").last();
        String lastTermId = latest_term.val();

        doc = Jsoup.connect(timetablesAisUrl).data("rozvrh", lastTermId).post();
        Elements rooms = doc.select("select[name=\"mistnost\"]").select("option");

        List<RoomOption> roomOptions = new ArrayList<>();

        for (Element room: rooms) {
            roomOptions.add(new RoomOption(Short.valueOf(room.val()), room.text()));
        }

        // Remove --all rooms-- option
        roomOptions.remove(0);

        return roomOptions;
    }

    public Timetable loadLatestTimetable()  {




        return null;
    }

}
