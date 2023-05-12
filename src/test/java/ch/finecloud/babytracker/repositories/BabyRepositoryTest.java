package ch.finecloud.babytracker.repositories;

import ch.finecloud.babytracker.entities.Baby;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BabyRepositoryTest {

    @Autowired
    BabyRepository babyRepository;

    @Test
    void testSaveBaby() {
        Baby savedBaby = babyRepository.save(Baby.builder()
                .name("testBaby").build());
        assertThat(savedBaby).isNotNull();
        assertThat(savedBaby.getId()).isNotNull();
    }
}