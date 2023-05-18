package ch.finecloud.babytracker.repositories;

import ch.finecloud.babytracker.bootstrap.BootstrapData;
import ch.finecloud.babytracker.entities.UserAccount;
import ch.finecloud.babytracker.services.BabyCsvServiceImpl;
import ch.finecloud.babytracker.services.EventCsvServiceImpl;
import ch.finecloud.babytracker.services.UserCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({BootstrapData.class, UserCsvServiceImpl.class})
class UserAccountAccountRepositoryTest {

    @Autowired
    UserAccountRepository userAccountRepository;

    @Test
    void testUserNameTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            UserAccount savedUserAccount = userAccountRepository.save(UserAccount.builder()
                    .username("TestUser23482347982748923748923784723894782937482374723847238479")
                    .password("testPassword")
                    .build());

            userAccountRepository.flush();
        });
    }

    @Test
    void testGetUserByName() {
        Page<UserAccount> list = userAccountRepository.findAllByUsernameIsLikeIgnoreCase("%tickets%", null);
        assertThat(list.getContent().size()).isEqualTo(1);
    }

    @Test
    void testSaveUser() {
        UserAccount savedUserAccount = userAccountRepository.save(UserAccount.builder()
                .username("testUser")
                .password("testpassword")
                .build());

        userAccountRepository.flush();
        assertThat(savedUserAccount).isNotNull();
        assertThat(savedUserAccount.getId()).isNotNull();
    }

}