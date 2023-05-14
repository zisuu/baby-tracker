package ch.finecloud.babytracker.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BabyCSVRecord {
    @CsvBindByName
    private int row;
    @CsvBindByName
    private String name;
}
