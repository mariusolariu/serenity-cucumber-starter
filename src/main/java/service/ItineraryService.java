package service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItineraryService {
    private TimeTable timeTable;

    public ItineraryService(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    public List<LocalTime> findNextDepartures(LocalTime departureTime, String from, String to){
        List<String> linesThrough = timeTable.findLinesThrough(from, to);
        List<LocalTime> departures = timeTable.getDepartures(linesThrough.get(0), from);

        departures = departures.stream().sorted().collect(Collectors.toList());
        List<LocalTime> nextDepartures = new ArrayList<>();
        for (LocalTime departure : departures) {
            if (departure.isAfter(departureTime)) {
                nextDepartures.add(departure);
            }
        }

        return nextDepartures;
    }
}
