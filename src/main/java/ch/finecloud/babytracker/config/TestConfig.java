package ch.finecloud.babytracker.config;

import ch.finecloud.babytracker.controller.helper.JwtUtil;
import ch.finecloud.babytracker.mappers.BabyMapper;
import ch.finecloud.babytracker.mappers.BabyMapperImpl;
import ch.finecloud.babytracker.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//@Configuration
public class TestConfig {

    @Bean
    public PasswordEncoder passwordEncoder2() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public BabyMapper babyMapper() {
        return new BabyMapperImpl();
    }

    @Bean
    public UserAccountUserDetailsService userDetailsService2() {
        return new UserAccountUserDetailsService();
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }
}
