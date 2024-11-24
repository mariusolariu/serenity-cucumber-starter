package unittests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ItineraryService;
import service.TimeTable;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("When finding the next train departure times")
class WhenFindingNextDepatureTimes {

    private LocalTime at(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("H:mm"));
    }

    private TimeTable createTimetable(List<LocalTime> departures) {
        return new TimeTable() {
            @Override
            public List<String> findLinesThrough(String from,
                                                 String to) {
                return List.of("T1");
            }

            @Override
            public List<LocalTime> getDepartures(String line, String from) {
                return departures;
            }
        };

    }

    @Test
    @DisplayName("we should get the first train after the requested time")
    void tripWithOneScheduledTime() {
        List<LocalTime> departures = List.of(at("8:10"), at("8:15"), at("8:35"), at("8:30"));
        TimeTable timeTable = createTimetable(departures);

        // Given
        ItineraryService itineraryService = new ItineraryService(timeTable);

        // When
        List<LocalTime> proposedDepartures
                = itineraryService.findNextDepartures(LocalTime.of(8, 25),"Hornsby", "Central");

        // Then
        assertThat(proposedDepartures).containsExactly(LocalTime.of(8,30), LocalTime.of(8, 35));
    }
}

