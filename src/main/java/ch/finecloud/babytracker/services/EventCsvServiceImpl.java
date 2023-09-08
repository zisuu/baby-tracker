package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.EventCSVRecord;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
public class EventCsvServiceImpl implements EventCsvService {
    @Override
    public List<EventCSVRecord> convertCSV(File csvFile) {
        try {
            return new CsvToBeanBuilder<EventCSVRecord>(new FileReader(csvFile))
                    .withType(EventCSVRecord.class)
                    .build().parse();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
