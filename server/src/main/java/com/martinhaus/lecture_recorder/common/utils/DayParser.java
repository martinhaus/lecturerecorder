package com.martinhaus.lecture_recorder.common.utils;

class DayParser {
    static Short parseDayString(String day) {
        switch (day) {
            case "Po": return 0;
            case "Ut": return 1;
            case "St": return 2;
            case "Št": return 3;
            case "Pi": return 4;
            case "So": return 5;
            case "Ne": return 6;
            default: return null;
        }
    }
}
