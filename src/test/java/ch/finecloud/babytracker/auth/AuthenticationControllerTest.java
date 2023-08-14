package ch.finecloud.babytracker.auth;

import ch.finecloud.babytracker.model.UserAccountDTO;
import ch.finecloud.babytracker.services.UserAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ch.finecloud.babytracker.services.UserAccountService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
    public void testRegisterSuccess() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType("application/json")
                .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void testRegisterFailNoUsername() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType("application/json")
                .content("{\"email\":\"\",\"password\":\"password\"}"));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].email").value("must not be blank"));
    }

    @Test
    public void testRegisterFailNoPassword() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType("application/json")
                .content("{\"email\":\"test\",\"password\":\"\"}"));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].password").value("must not be blank"));
    }

    @Test
    public void testAuthenticationBadCredentials() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                .contentType("application/json")
                .content("{\"email\":\"test\",\"password\":\"test\"}"));

        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    public void testAuthenticationGoodCredentials() throws Exception {
        UserAccountDTO testUserAccountDTO = userAccountServiceImpl.listUsers(null, 1, 25).getContent().get(0);
        System.out.println(testUserAccountDTO.toString());
        String requestBody = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", USERNAME, PASSWORD);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                .contentType("application/json")
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}
