package com.martinhaus.lecture_recorder.common.utils;

import java.util.Calendar;
import java.util.Date;

class TimeParser {
    static Date getDateFromHour(int hour) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }
}

