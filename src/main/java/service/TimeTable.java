package service;

import java.time.LocalTime;
import java.util.List;

public interface TimeTable {
    List<String> findLinesThrough(String from, String to);
    List<LocalTime> getDepartures(String lineName, String from);
}
