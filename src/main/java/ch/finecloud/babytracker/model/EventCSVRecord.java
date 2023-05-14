package ch.finecloud.babytracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.opencsv.bean.CsvBindByName;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCSVRecord {
    @CsvBindByName
    private int row;
    @CsvBindByName
    private EventType eventType;
}
