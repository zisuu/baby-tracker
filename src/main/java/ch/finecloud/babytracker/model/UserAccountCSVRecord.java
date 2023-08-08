package ch.finecloud.babytracker.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountCSVRecord {
    @CsvBindByName
    private int row;
    @CsvBindByName
    private String email;
    @CsvBindByName
    private String password;

}
