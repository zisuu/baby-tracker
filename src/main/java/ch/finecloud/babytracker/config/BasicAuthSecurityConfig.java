//package ch.finecloud.babytracker.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class BasicAuthSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests()
//                .anyRequest()
//                .authenticated()
//                .and().httpBasic(Customizer.withDefaults())
//                .csrf().ignoringRequestMatchers("/api/**");
//        return http.build();
//    }
//}