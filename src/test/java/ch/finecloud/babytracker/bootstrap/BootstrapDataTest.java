package ch.finecloud.babytracker.bootstrap;

import ch.finecloud.babytracker.repositories.BabyRepository;
import ch.finecloud.babytracker.repositories.EventRepository;
import ch.finecloud.babytracker.repositories.UserAccountRepository;
import ch.finecloud.babytracker.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import({EventCsvServiceImpl.class, BabyCsvServiceImpl.class, UserCsvServiceImpl.class})
class BootstrapDataTest {

    @Autowired
    BabyRepository babyRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    EventCsvService eventCsvService;

    @Autowired
    BabyCsvService babyCsvService;

    @Autowired
    UserCsvService userCsvService;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(userAccountRepository, babyRepository, eventRepository, userCsvService, eventCsvService, babyCsvService);
    }

    @Test
    void Testrun() throws Exception {
        bootstrapData.run(null);
        assertThat(babyRepository.count()).isEqualTo(10);
        assertThat(eventRepository.count()).isEqualTo(7);
        assertThat(userAccountRepository.count()).isEqualTo(100);
    }
}