package ch.finecloud.babytracker.config;

import ch.finecloud.babytracker.mappers.BabyMapper;
import ch.finecloud.babytracker.mappers.BabyMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // You can use any PasswordEncoder implementation here
    }

    @Bean
    public BabyMapper babyMapper() {
        return new BabyMapperImpl();
    }


}
