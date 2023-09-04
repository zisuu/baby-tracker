package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.controller.NotFoundException;
import ch.finecloud.babytracker.model.BabyCSVRecord;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
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
            for (BabyCSVRecord babyCSVRecord : babyCSVRecords) {
                parseCSVRecord(babyCSVRecord);
            }
            return babyCSVRecords;
        } catch (FileNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    private void parseCSVRecord(BabyCSVRecord babyCSVRecord) {
        try {
            LocalDate date = LocalDate.parse(babyCSVRecord.getBirthday(), dateFormatter);
            babyCSVRecord.setBirthday(String.valueOf(date));
        } catch (DateTimeParseException e) {
            // Handle the parsing error, e.g., log it or skip the babyCSVRecord
            log.debug("Error parsing date for babyCSVRecord: " + babyCSVRecord.getBirthday());
        }
    }
}
