package ch.finecloud.babytracker.bootstrap;

import ch.finecloud.babytracker.repositories.BabyRepository;
import ch.finecloud.babytracker.repositories.EventRepository;
import ch.finecloud.babytracker.services.BabyCsvService;
import ch.finecloud.babytracker.services.BabyCsvServiceImpl;
import ch.finecloud.babytracker.services.EventCsvService;
import ch.finecloud.babytracker.services.EventCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Import({EventCsvServiceImpl.class, BabyCsvServiceImpl.class})
class BootstrapDataTest {

    @Autowired
    BabyRepository babyRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventCsvService eventCsvService;

    @Autowired
    BabyCsvService babyCsvService;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(babyRepository, eventRepository, eventCsvService, babyCsvService);
    }

    @Test
    void Testrun() throws Exception {
        bootstrapData.run(null);
        assertThat(babyRepository.count()).isEqualTo(13);
        assertThat(eventRepository.count()).isEqualTo(7);
    }
}