package ch.finecloud.babytracker.repositories;

import ch.finecloud.babytracker.bootstrap.BootstrapData;
import ch.finecloud.babytracker.entities.Baby;
import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.entities.UserAccount;
import ch.finecloud.babytracker.services.BabyCsvServiceImpl;
import ch.finecloud.babytracker.services.EventCsvServiceImpl;
import ch.finecloud.babytracker.services.UserCsvServiceImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({BootstrapData.class, UserCsvServiceImpl.class})
class BabyRepositoryTest {

    @Autowired
    BabyRepository babyRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    Event testEvent;
    UserAccount testUserAccount;

    @BeforeEach
    void setUp() {
        testEvent = eventRepository.findAll().get(0);
        testUserAccount = userAccountRepository.findAll().get(0);
    }

    @Transactional
    @Test
    void testBabyEvent() {
        Baby baby = Baby.builder()
                .name("testBaby")
                .userAccount(testUserAccount)
                .build();

        Baby savedBaby = babyRepository.save(baby);

        System.out.println(savedBaby.getUserAccount().getUsername());
    }

}