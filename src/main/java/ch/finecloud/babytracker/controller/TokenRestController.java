package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.controller.helper.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

/**
 * Servlet implementation class TokenRestController
 */
@Slf4j
//@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
@RestController
public class TokenRestController {

    //    public static final String BASE_URL = "/api/v1/token";
    @Autowired
    private JwtUtil JWTUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public String authenticateAndGetToken(HttpServletRequest request) {
        String username = null;
        String password = null;

        // Retrieve Basic Auth credentials from the request header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] parts = credentials.split(":", 2);
            if (parts.length == 2) {
                username = parts[0];
                password = parts[1];
            }
        }

        if (username != null && password != null) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            if (authentication.isAuthenticated()) {
                return JWTUtil.generateToken(username);
            }
        }

        throw new UsernameNotFoundException("Invalid user request!");
    }

}
