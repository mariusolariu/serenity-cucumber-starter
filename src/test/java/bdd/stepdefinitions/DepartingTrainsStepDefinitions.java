package bdd.stepdefinitions;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import service.ItineraryService;
import service.TimeTable;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class DepartingTrainsStepDefinitions {
    List<LocalTime> proposedDepartures;
    ItineraryService itineraryService ;

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

    @Given("the {line} train to {station} leaves {station} at {time}")
    public void theTrainLeavesAt(String line, String to, String from, List<LocalTime> departureTimes){
        TimeTable timetable = createTimetable(List.of(LocalTime.of(8, 20)));
        itineraryService = new ItineraryService(timetable);
    }

    @When("Travis wants to travel from {} to {} at {time}")
    public void travel(String from, String to, LocalTime departureTime) {
        proposedDepartures = itineraryService.findNextDepartures(departureTime, from, to);

    }

    @Then("he should be told about the trains at: {times}")
    public void shouldBeToldAboutTheTrainsAt(List<LocalTime> expectedTimes){
        assertThat(proposedDepartures).isEqualTo(expectedTimes);
    }

    @ParameterType(".*")
    public LocalTime time(String timeValue) {
        return LocalTime.parse(timeValue, DateTimeFormatter.ofPattern("H:mm"));
    }

    @ParameterType(".*")
    public List<LocalTime> times(String timeValue) {
        return Stream.of(timeValue.split(","))
                .map(String::trim)
                .map(this::time)
                .collect(Collectors.toList());
    }
}
