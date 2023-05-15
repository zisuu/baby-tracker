package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.entities.UserAccount;
import ch.finecloud.babytracker.mappers.UserAccountMapper;
import ch.finecloud.babytracker.model.UserAccountDTO;
import ch.finecloud.babytracker.repositories.UserAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserAccountControllerIT {

    @Autowired
    UserAccountController userAccountController;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    UserAccountMapper userAccountMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testListUserAccountByUsername() throws Exception {
        mockMvc.perform(get(UserAccountController.BASE_URL).queryParam("username", "univ"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", CoreMatchers.is(1)));
    }

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> userAccountController.deleteById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void deleteByIdFound() {
        UserAccount testUserAccount = userAccountRepository.findAll().get(0);
        ResponseEntity responseEntity = userAccountController.deleteById(testUserAccount.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(userAccountRepository.findById(testUserAccount.getId()).isEmpty());
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> userAccountController.updateById(UUID.randomUUID(), UserAccountDTO.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    void updateExistingUserAccount() {
        UserAccount testUserAccount = userAccountRepository.findAll().get(0);
        UserAccountDTO userAccountDTO = userAccountMapper.userToUserDto(testUserAccount);
        userAccountDTO.setId(null);
        userAccountDTO.setVersion(null);
        userAccountDTO.setPassword("Updated UserAccount PW");
        ResponseEntity responseEntity = userAccountController.updateById(testUserAccount.getId(), userAccountDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        UserAccount updatedUserAccount = userAccountRepository.findById(testUserAccount.getId()).get();
        assertThat(updatedUserAccount.getPassword()).isEqualTo("Updated UserAccount PW");
    }

    @Rollback
    @Transactional
    @Test
    void saveNewUserAccountTest() {
        UserAccountDTO userAccountDTO = UserAccountDTO.builder()
                .username("New UserAccount")
                .password("New UserAccount")
                .build();
        ResponseEntity responseEntity = userAccountController.handlePost(userAccountDTO);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);
        UserAccount userAccount = userAccountRepository.findById(savedUUID).get();
        assertThat(userAccount).isNotNull();
    }

    @Test
    void testBabyByIdNotFound() {
        assertThrows(NotFoundException.class, () -> userAccountController.getUserById(UUID.randomUUID()));
    }

    @Test
    void testGetById() {
        UserAccount testUserAccount = userAccountRepository.findAll().get(0);
        UserAccountDTO userAccountDTO = userAccountController.getUserById(testUserAccount.getId());
        assertThat(userAccountDTO).isNotNull();
    }

    @Test
    void testListUserAccounts() {
        Page<UserAccountDTO> userAccountDTOList = userAccountController.listUsers(null, 1, 25);
        assertThat(userAccountDTOList.getContent().size()).isEqualTo(25);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        userAccountRepository.deleteAll();
        Page<UserAccountDTO> userAccountDTOList = userAccountController.listUsers(null, 1, 25);
        assertThat(userAccountDTOList.getContent().size()).isEqualTo(0);
    }
}

