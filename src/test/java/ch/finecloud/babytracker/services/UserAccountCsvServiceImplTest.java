package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.model.UserAccountCSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserAccountCsvServiceImplTest {
    UserCsvService userCsvService = new UserCsvServiceImpl();

    @Test
    void convertCSV() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/users.csv");
        List<UserAccountCSVRecord> userAccountCSVRecords = userCsvService.convertCSV(file);
        System.out.println(userAccountCSVRecords.size());
        assertThat(userAccountCSVRecords).isNotEmpty();
    }
}