package ch.finecloud.babytracker.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
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

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
}
