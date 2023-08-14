package ch.finecloud.babytracker.repositories;

import ch.finecloud.babytracker.bootstrap.BootstrapData;
import ch.finecloud.babytracker.config.TestConfig;
import ch.finecloud.babytracker.entities.Baby;
import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.entities.UserAccount;
import ch.finecloud.babytracker.services.UserCsvServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({BootstrapData.class, UserCsvServiceImpl.class, TestConfig.class})
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
    void testAddBaby() {
        Baby baby = Baby.builder()
                .name("testBaby")
                .build();
        baby.setUserAccount(testUserAccount);
        Baby savedBaby = babyRepository.save(baby);
        babyRepository.flush();

        assertNotNull(savedBaby);
        assertNotNull(savedBaby.getId());

        Baby fetchedBaby = babyRepository.getById(savedBaby.getId());

        assertNotNull(fetchedBaby);
        assertNotNull(fetchedBaby.getId());
        assertNotNull(fetchedBaby.getUserAccount());
    }

    @Transactional
    @Test
    void testDeleteBaby() {
        Baby baby = Baby.builder()
                .name("testBaby")
                .build();
        baby.setUserAccount(testUserAccount);
        Baby savedBaby = babyRepository.save(baby);
        babyRepository.flush();

        assertNotNull(savedBaby);
        assertNotNull(savedBaby.getId());

        Baby fetchedBaby = babyRepository.findById(savedBaby.getId()).get();

        assertNotNull(fetchedBaby);
        assertNotNull(fetchedBaby.getId());
        assertNotNull(fetchedBaby.getUserAccount());

        babyRepository.delete(fetchedBaby);
        babyRepository.flush();
        Baby deletedBaby = babyRepository.findById(savedBaby.getId()).orElse(null);
        assertNull(deletedBaby);
    }

}