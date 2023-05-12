package ch.finecloud.babytracker.bootstrap;

import ch.finecloud.babytracker.repositories.BabyRepository;
import ch.finecloud.babytracker.repositories.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class BootstrapDataTest {

    @Autowired
    BabyRepository babyRepository;

    @Autowired
    EventRepository eventRepository;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(babyRepository, eventRepository);
    }

    @Test
    void Testrun() throws Exception {
        bootstrapData.run(null);
        assertThat(babyRepository.count()).isEqualTo(3);
        assertThat(eventRepository.count()).isEqualTo(3);
    }
}