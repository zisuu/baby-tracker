package ch.finecloud.babytracker.repositories;

import ch.finecloud.babytracker.entities.Baby;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BabyRepositoryTest {

    @Autowired
    BabyRepository babyRepository;

    @Test
    void testBabyNameTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            Baby savedBaby = babyRepository.save(Baby.builder()
                    .name("TestBaby23482347982748923748923784723894782937482374723847238479").build());

            babyRepository.flush();
        });
    }

    @Test
    void testSaveBaby() {
        Baby savedBaby = babyRepository.save(Baby.builder()
                .name("testBaby").build());

        babyRepository.flush();
        assertThat(savedBaby).isNotNull();
        assertThat(savedBaby.getId()).isNotNull();
    }

}