package ch.finecloud.babytracker.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCSVRecord {
    @CsvBindByName
    private int row;
    @CsvBindByName
    private EventType eventType;
    @CsvBindByName
    private String startDate;
    @CsvBindByName
    private String endDate;
}
