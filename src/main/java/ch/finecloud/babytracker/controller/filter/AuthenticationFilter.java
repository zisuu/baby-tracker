package ch.finecloud.babytracker.controller.filter;

import ch.finecloud.babytracker.controller.helper.JwtUtil;
import ch.finecloud.babytracker.entities.UserAccount;
import ch.finecloud.babytracker.repositories.UserAccountRepository;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_SCHEME_BASIC = "Basic";
    private static final String AUTH_SCHEME_BEARER = "Bearer";

    private final UserAccountRepository userAccountRepository;

    public AuthenticationFilter(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // allow options call to request all allowed http-methods (https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/OPTIONS)
        if (request.getMethod().equals("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }
        try {
            UserAccount userAccount = getUserAccount(request);
            request.setAttribute("userAccount", userAccount);
        } catch (AuthException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(ex.getMessage());
            return;
        }
        chain.doFilter(request, response);
    }

    private UserAccount getUserAccount(HttpServletRequest request) throws AuthException {
        //Step-1: Get the HTTP Authorization header from the request
        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader == null) {
            throw new AuthException("AuthorizationHeader is missing, use Bearer token or Basic Auth");
        }
        //Step-2: Extract the token from the Authorization header
        String[] tokens = authHeader.split(" ");
        //Step-3.1: Check if the Authorization header contains Basic Auth to request a Token
        if (tokens[0].equals(AUTH_SCHEME_BASIC)) {
            byte[] decodedBytes = Base64.getDecoder().decode(tokens[1]);
            return validateLogin(new String(decodedBytes, StandardCharsets.UTF_8).split(":", 2));
            //Step-3.2: Check if the Authorization header contains the Token
        } else if (tokens[0].equals(AUTH_SCHEME_BEARER)) {
            //Step-4: Validate the token. If invalid, set UNAUTHORIZED response status
            DecodedJWT decodedJWT = JwtUtil.getInstance().verifyJWT(tokens[1]);
            if (decodedJWT == null || JwtUtil.isJWTExpired(decodedJWT)) {
                throw new AuthException("Invalid or expired token!");
            }
            String username = JwtUtil.getInstance().getClaim(decodedJWT, "username");
            Optional<UserAccount> person = userAccountRepository.findUserAccountByUsername(username);
            if (person.isPresent()) return person.get();
        }
        throw new AuthException("Authentication failed, please check your AuthorizationHeader. Use Bearer token or Basic Auth");
    }

    /**
     * Validate the login with the given username and password
     *
     * @param credentials username and password
     * @return the person if the login was successful
     * @throws AuthException if the login was not successful
     */
    private UserAccount validateLogin(String[] credentials) throws AuthException {
        //expect 0=username, 1=password
        if (credentials[0].isEmpty()) {
            throw new AuthException("Basic AuthorizationHeader is incorrect, username is missing");
        } else if (credentials[1].isEmpty()) {
            throw new AuthException("Basic AuthorizationHeader is incorrect, password is missing");
        }
        Optional<UserAccount> person = userAccountRepository.findUserAccountByUsernameAndPassword(credentials[0], credentials[1]);
        if (person.isPresent()) {
            return person.get();
        } else {
            throw new AuthException("Basic Authorization failed, invalid credentials!");
        }
    }
}
