package ch.finecloud.babytracker.services;

import ch.finecloud.babytracker.config.JwtService;
import ch.finecloud.babytracker.entities.Role;
import ch.finecloud.babytracker.entities.UserAccount;
import ch.finecloud.babytracker.model.AuthenticationRequest;
import ch.finecloud.babytracker.model.AuthenticationResponse;
import ch.finecloud.babytracker.model.RegisterRequest;
import ch.finecloud.babytracker.repositories.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var userAccount = UserAccount.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(userAccount);
        var jwtToken = jwtService.generateToken(userAccount);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var userAccount = repository.findUserAccountByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("user not found"));
        var jwtToken = jwtService.generateToken(userAccount);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public boolean checkIfUserExists(String email) {
        return repository.findUserAccountByEmail(email).isPresent();
    }
}
