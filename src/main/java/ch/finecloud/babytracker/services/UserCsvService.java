package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.UserAccountCSVRecord;

import java.io.File;
import java.util.List;

public interface UserCsvService {
    List<UserAccountCSVRecord> convertCSV(File csvFile);
}
