package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.controller.NotFoundException;
import ch.finecloud.babytracker.model.UserAccountCSVRecord;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
public class UserCsvServiceImpl implements UserCsvService {
    @Override
    public List<UserAccountCSVRecord> convertCSV(File csvFile) {
        try {
            return new CsvToBeanBuilder<UserAccountCSVRecord>(new FileReader(csvFile))
                    .withType(UserAccountCSVRecord.class)
                    .build().parse();
        } catch (FileNotFoundException e) {
            throw new NotFoundException(e);
        }
    }
}
