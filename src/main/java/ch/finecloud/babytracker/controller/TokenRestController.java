package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.controller.helper.JwtUtil;
import ch.finecloud.babytracker.entities.UserAccount;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Servlet implementation class TokenRestController
 */
@Slf4j
//@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
@RestController
public class TokenRestController{

//    public static final String BASE_URL = "/api/v1/token";

    @PostMapping("/")
    public void getToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserAccount userAccount = (UserAccount) request.getAttribute("userAccount");
        String token = JwtUtil.getInstance().createJWT(userAccount);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/plain");
        response.getWriter().print(token);
    }

}
