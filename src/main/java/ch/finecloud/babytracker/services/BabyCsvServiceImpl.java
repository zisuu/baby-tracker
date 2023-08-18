package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.BabyCSVRecord;
import ch.finecloud.babytracker.model.EventCSVRecord;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class BabyCsvServiceImpl implements BabyCsvService {
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    @Override
    public List<BabyCSVRecord> convertCSV(File csvFile) {
        try {
            List<BabyCSVRecord> babyCSVRecords = new CsvToBeanBuilder<BabyCSVRecord>(new FileReader(csvFile))
                    .withType(BabyCSVRecord.class)
                    .build().parse();
            // Process the records and update date fields using the correct formatter
            for (BabyCSVRecord record : babyCSVRecords) {
                try {
                    LocalDate date = LocalDate.parse(record.getBirthday(), dateFormatter);
                    record.setBirthday(String.valueOf(date));
                } catch (DateTimeParseException e) {
                    // Handle the parsing error, e.g., log it or skip the record
                    System.err.println("Error parsing date for record: " + record.getBirthday());
                }
            }
            return babyCSVRecords;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }
    }
}
