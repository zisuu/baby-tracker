package ch.finecloud.babytracker.repositories;

import ch.finecloud.babytracker.bootstrap.BootstrapData;
import ch.finecloud.babytracker.config.TestConfig;
import ch.finecloud.babytracker.entities.UserAccount;
import ch.finecloud.babytracker.services.UserCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({BootstrapData.class, UserCsvServiceImpl.class, TestConfig.class})
class UserAccountAccountRepositoryTest {

    @Autowired
    UserAccountRepository userAccountRepository;

    @Test
    void testUserNameTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            UserAccount savedUserAccount = userAccountRepository.save(UserAccount.builder()
                    .email("TestUser23482347982748923748923784723894782937482374723847238479@example.com")
                    .password("testPassword")
                    .build());

            userAccountRepository.flush();
        });
    }

    @Test
    void testGetUserByName() {
        Page<UserAccount> list = userAccountRepository.findAllByEmailIsLikeIgnoreCase("%augustus_harrb7@francis.xfv%", null);
        assertThat(list.getContent().size()).isEqualTo(1);
    }

    @Test
    void testSaveUser() {
        UserAccount savedUserAccount = userAccountRepository.save(UserAccount.builder()
                .email("testUser")
                .password("testpassword")
                .build());

        userAccountRepository.flush();
        assertThat(savedUserAccount).isNotNull();
        assertThat(savedUserAccount.getId()).isNotNull();
    }

    @Test
    void testDeleteUser() {
        UserAccount savedUserAccount = userAccountRepository.save(UserAccount.builder()
                .email("testUser2")
                .password("testpassword2")
                .build());

        userAccountRepository.flush();
        assertThat(savedUserAccount).isNotNull();
        assertThat(savedUserAccount.getId()).isNotNull();
        userAccountRepository.delete(savedUserAccount);
        userAccountRepository.flush();
        assertThat(userAccountRepository.findById(savedUserAccount.getId())).isEmpty();
    }

}