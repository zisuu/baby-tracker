package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.services.UserAccountService;
import ch.finecloud.babytracker.services.UserAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AuthenticationControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    UserAccountService userAccountService;

    UserAccountService userAccountServiceImpl;

    public static final String USERNAME = "userAccount1@example.com";
    public static final String PASSWORD = "password1";


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userAccountServiceImpl = new UserAccountServiceImpl();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType("application/json")
                .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testRegisterFailNoUsername() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType("application/json")
                .content("{\"email\":\"\",\"password\":\"password\"}"));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].email").value("must not be blank"));
    }

    @Test
    void testRegisterFailNoPassword() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType("application/json")
                .content("{\"email\":\"test\",\"password\":\"\"}"));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].password").value("must not be blank"));
    }

    @Test
    void testAuthenticationBadCredentials() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                .contentType("application/json")
                .content("{\"email\":\"test\",\"password\":\"test\"}"));

        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    void testAuthenticationGoodCredentials() throws Exception {
        String requestBody = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", USERNAME, PASSWORD);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                .contentType("application/json")
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}
