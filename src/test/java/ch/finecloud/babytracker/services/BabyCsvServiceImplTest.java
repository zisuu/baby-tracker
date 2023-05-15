package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.BabyCSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BabyCsvServiceImplTest {
    BabyCsvService babyCsvService = new BabyCsvServiceImpl();

    @Test
    void convertCSV() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/babies.csv");
        List<BabyCSVRecord> babyCSVRecords = babyCsvService.convertCSV(file);
        System.out.println(babyCSVRecords.size());
        assertThat(babyCSVRecords.size()).isGreaterThan(0);
    }
}