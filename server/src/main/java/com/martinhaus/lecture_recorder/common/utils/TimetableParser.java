package com.martinhaus.lecture_recorder.common.utils;

import com.martinhaus.lecture_recorder.model.timetables.Lesson;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TimetableParser {

    public static int getStartHourFromTimetableHeader(Document doc) {
        int begin = 0;

        //Find all hours in the timetable
        boolean valid = false;

        for (Element element : doc.select("th[class=\"zahlavi\"]")) {
            for (Element hourElement: element.getElementsByTag("small")) {
                //Excludes column with no time atribute
                if(!hourElement.text().equals("Deň")) {
                    //Dump DOM object to string and adds it to the array
                    begin = Integer.valueOf((hourElement.text().substring(0,1)));
                    valid = true;
                    break;
                }
            }
            //If valid time was found break the main loop
            if(valid) {
                break;
            }
        }
        return begin;
    }

    public static Lesson parseLessonEntry(Element dataElement, int start, String day) {
        //Mark block start and finish time
        int lessonStart = start;
        int lessonEnd = start += Integer.valueOf(dataElement.attr("colspan"));
        String lessonName = "";
        String teacher = "";
        String room = "";
        //Valid lesson entry
        if(dataElement.className().contains("rozvrh-cvic") || dataElement.className().contains("rozvrh-pred")) {
            int counter = 0;

            for (Element anchor : dataElement.getElementsByTag("a")) {
                counter ++;
                //Lesson room
                if( counter == 1) {
                    room = anchor.text();
                    room = room.replace(" (BA-MD-FIIT)","");
                }

                //Lesson name
                if ( counter == 2 ) {
                    lessonName = anchor.text();
                } //Teacher
                else if ( counter == 3 ) {
                    teacher = anchor.text();
                }
            }

            //Creates new lessson object and adds it to the array
            return Lesson.builder()
                    .title(lessonName)
                    .startTime(TimeParser.getDateFromHour(lessonStart))
                    .endTime(TimeParser.getDateFromHour(lessonEnd))
                    .teacher(teacher)
                    .dayOfWeek(DayParser.parseDayString(day))
                    .build();
        }
        return null;
    }

}
