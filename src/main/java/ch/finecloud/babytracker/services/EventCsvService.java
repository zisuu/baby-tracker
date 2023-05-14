package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.EventCSVRecord;

import java.io.File;
import java.util.List;

public interface EventCsvService {
    List<EventCSVRecord> convertCSV(File csvFile);
}
