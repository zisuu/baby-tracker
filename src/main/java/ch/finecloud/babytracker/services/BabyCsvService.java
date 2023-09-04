package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.BabyCSVRecord;

import java.io.File;
import java.util.List;

public interface BabyCsvService {
    List<BabyCSVRecord> convertCSV(File csvFile);
}
