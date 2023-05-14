package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.BabyCSVRecord;
import ch.finecloud.babytracker.model.EventCSVRecord;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
public class BabyCsvServiceImpl implements BabyCsvService {
    @Override
    public List<BabyCSVRecord> convertCSV(File csvFile) {
        try {
            List<BabyCSVRecord> babyCSVRecords = new CsvToBeanBuilder<BabyCSVRecord>(new FileReader(csvFile))
                    .withType(BabyCSVRecord.class)
                    .build().parse();
            return babyCSVRecords;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }
    }
}
