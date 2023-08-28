package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.model.UserAccountDTO;
import ch.finecloud.babytracker.services.UserAccountService;
import ch.finecloud.babytracker.services.UserAccountServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
class UserAccountControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserAccountService userAccountService;

    UserAccountService userAccountServiceImpl;

    @Captor
    ArgumentCaptor<UserAccountDTO> userArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    public static final String USERNAME = "userAccount1@example.com";
    public static final String PASSWORD = "password1";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userAccountServiceImpl = new UserAccountServiceImpl();
    }

    private String getJwtToken() throws Exception {
        String requestBody = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", USERNAME, PASSWORD);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        return contentAsString.substring(8, contentAsString.length() - 2);
    }

    @Test
    void testCreateUserNullEmail() throws Exception {
        UserAccountDTO userAccountDTO = UserAccountDTO.builder()
                .email(null)
                .password("password1")
                .role("USER")
                .build();
        given(userAccountService.saveNewUser(any(UserAccountDTO.class))).willReturn(userAccountServiceImpl.listUsers(null, 1, 25).getContent().get(1));
        MvcResult MvcResult = mockMvc.perform(post(UserAccountController.BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAccountDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(2)))
                .andReturn();

        System.out.println(MvcResult.getResponse().getContentAsString());
    }

    @Test
    void testCreateUserBlankEmail() throws Exception {
        UserAccountDTO userAccountDTO = UserAccountDTO.builder()
                .email("")
                .password("password1")
                .role("USER")
                .build();
        given(userAccountService.saveNewUser(any(UserAccountDTO.class))).willReturn(userAccountServiceImpl.listUsers("", 1, 25).getContent().get(1));
        MvcResult MvcResult = mockMvc.perform(post(UserAccountController.BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAccountDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(MvcResult.getResponse().getContentAsString());
    }

    @Test
    void testUpdateUserAccountBlankPassword() throws Exception {
        UserAccountDTO testUserAccountDTO = userAccountServiceImpl.listUsers(null, 1, 25).getContent().get(0);
        testUserAccountDTO.setPassword("");
        given(userAccountService.updateUserById(any(UUID.class), any(UserAccountDTO.class))).willReturn(Optional.of(testUserAccountDTO));
        MvcResult MvcResult = mockMvc.perform(put(UserAccountController.BASE_URL_ID, testUserAccountDTO.getId())
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserAccountDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();
        System.out.println(MvcResult.getResponse().getContentAsString());
    }

    @Test
    void testUpdateUserAccountNullPassword() throws Exception {
        UserAccountDTO testUserAccountDTO = userAccountServiceImpl.listUsers(null, 1, 25).getContent().get(0);
        testUserAccountDTO.setPassword(null);
        given(userAccountService.updateUserById(any(UUID.class), any(UserAccountDTO.class))).willReturn(Optional.of(testUserAccountDTO));
        MvcResult MvcResult = mockMvc.perform(put(UserAccountController.BASE_URL_ID, testUserAccountDTO.getId())
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserAccountDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(2)))
                .andReturn();
        System.out.println(MvcResult.getResponse().getContentAsString());
    }

    @Test
    void testUpdateUserAccountNoEmail() throws Exception {
        UserAccountDTO testUserAccountDTO = userAccountServiceImpl.listUsers(null, 1, 25).getContent().get(0);
        testUserAccountDTO.setEmail("test");
        given(userAccountService.updateUserById(any(UUID.class), any(UserAccountDTO.class))).willReturn(Optional.of(testUserAccountDTO));
        MvcResult MvcResult = mockMvc.perform(put(UserAccountController.BASE_URL_ID, testUserAccountDTO.getId())
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserAccountDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();
        System.out.println(MvcResult.getResponse().getContentAsString());
    }

    @Test
    void testPatchUserAccount() throws Exception {
        UserAccountDTO testUserAccountDTO = userAccountServiceImpl.listUsers(null, 1, 25).getContent().get(0);
        Map<String, Object> userAccountMap = new HashMap<>();
        userAccountMap.put("password", "New Passsoword");
        mockMvc.perform(patch("/api/v1/users/" + testUserAccountDTO.getId())
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAccountMap)))
                .andExpect(status().isNoContent());
        verify(userAccountService).patchUserById(uuidArgumentCaptor.capture(), userArgumentCaptor.capture());
        assertThat(testUserAccountDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(userAccountMap.get("password")).isEqualTo(userArgumentCaptor.getValue().getPassword());
    }

    @Test
    void testDeleteUserAccount() throws Exception {
        UserAccountDTO testUserAccountDTO = userAccountServiceImpl.listUsers(null, 1, 25).getContent().get(0);
        given(userAccountService.deleteById(any())).willReturn(true);
        mockMvc.perform(delete("/api/v1/users/" + testUserAccountDTO.getId())
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(userAccountService).deleteById(uuidArgumentCaptor.capture());
        assertThat(testUserAccountDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateUserAccount() throws Exception {
        UserAccountDTO testUserAccountDTO = userAccountServiceImpl.listUsers(null, 1, 25).getContent().get(0);
        given(userAccountService.updateUserById(any(UUID.class), any(UserAccountDTO.class))).willReturn(Optional.of(testUserAccountDTO));
        mockMvc.perform(put("/api/v1/users/" + testUserAccountDTO.getId())
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserAccountDTO)))
                .andExpect(status().isNoContent());
        verify(userAccountService).updateUserById(any(UUID.class), any(UserAccountDTO.class));
    }

    @Test
    void testCreateNewUserAccount() throws Exception {
        UserAccountDTO testUserAccountDTO = userAccountServiceImpl.listUsers(null, 1, 25).getContent().get(0);
        testUserAccountDTO.setId(null);
        testUserAccountDTO.setVersion(null);
        given(userAccountService.saveNewUser(any(UserAccountDTO.class))).willReturn(userAccountServiceImpl.listUsers(null, 1, 25).getContent().get(1));
        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserAccountDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testListBabies() throws Exception {
        given(userAccountService.listUsers(any(), any(), any())).willReturn(userAccountServiceImpl.listUsers(null, null, null));
        mockMvc.perform(get("/api/v1/users")
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", Is.is(3)));
    }

    @Test
    void getUserAccountById() throws Exception {
        UserAccountDTO testUserAccountDTO = userAccountServiceImpl.listUsers(null, 1, 25).getContent().get(0);
        given(userAccountService.getUserById(testUserAccountDTO.getId())).willReturn(Optional.of(testUserAccountDTO));
        mockMvc.perform(get("/api/v1/users/" + testUserAccountDTO.getId())
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testUserAccountDTO.getId().toString())))
                .andExpect(jsonPath("$.email", is(testUserAccountDTO.getEmail())));
    }

    @Test
    void getUserAccountByIdNotFound() throws Exception {
        given(userAccountService.getUserById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(UserAccountController.BASE_URL_ID, UUID.randomUUID())
                        .header("Authorization", "Bearer " + getJwtToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}