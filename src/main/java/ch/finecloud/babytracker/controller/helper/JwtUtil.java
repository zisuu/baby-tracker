package ch.finecloud.babytracker.controller.helper;

import ch.finecloud.babytracker.entities.UserAccount;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;

@Component
public final class JwtUtil {

    private static JwtUtil INSTANCE;
    private static final String ISSUER = "ch.finecloud.baby-tracker.app";
    private static final String USERNAME = "username";
//    private static final String NAME = "name";
    private static final long TOKEN_VALIDITY_IN_MILLIS = 3600000L;
    private static Algorithm algorithm;
    private static JWTVerifier verifier;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private JwtUtil() {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        algorithm = Algorithm.HMAC256(key);
        verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
    }

    public synchronized static JwtUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JwtUtil();
        }
        return INSTANCE;
    }

    /**
     * Creates a JWT token for the given userAccount
     *
     * @param userAccount the userAccount to create the token for
     * @return the token
     */
    public String createJWT(UserAccount userAccount) {
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(String.valueOf(userAccount.getId()))
                .withClaim(USERNAME, userAccount.getUsername())
//                .withClaim(NAME, (userAccount.getFirstName() + ", " + userAccount.getLastName()))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_VALIDITY_IN_MILLIS))
                .sign(algorithm);
    }

    /**
     * Validates the given token and returns the userAccount
     *
     * @param jwtToken the token to validate
     * @return the decoded JWT
     * @throws JWTVerificationException if the token is invalid
     */
    public DecodedJWT verifyJWT(String jwtToken) {
        try {
            return verifier.verify(jwtToken);
        } catch (JWTVerificationException e) {
            logger.error("JWT verification failed: " + e.getMessage(), "decoded token: " + jwtToken);
        }
        return null;
    }

    /**
     * Checks if the given token is expired
     *
     * @param decodedJWT
     * @return true if the token is expired, false otherwise
     */
    public static boolean isJWTExpired(DecodedJWT decodedJWT) {
        Date expiresAt = decodedJWT.getExpiresAt();
        boolean isExpired = expiresAt.getTime() < System.currentTimeMillis();
        if (isExpired) {
            logger.error("JWT expired, decoded jwt: " + decodedJWT);
        }
        return isExpired;
    }

    public String getClaim(DecodedJWT decodedJWT, String claimName) {
        Claim claim = decodedJWT.getClaim(claimName);
        return claim != null ? claim.asString() : null;
    }
}
