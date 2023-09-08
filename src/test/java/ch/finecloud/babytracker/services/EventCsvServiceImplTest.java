package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.EventCSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EventCsvServiceImplTest {
    EventCsvService eventCsvService = new EventCsvServiceImpl();

    @Test
    void convertCSV() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/events.csv");
        List<EventCSVRecord> eventCSVRecords = eventCsvService.convertCSV(file);
        System.out.println(eventCSVRecords.size());
        assertThat(eventCSVRecords).isNotEmpty();
    }
}