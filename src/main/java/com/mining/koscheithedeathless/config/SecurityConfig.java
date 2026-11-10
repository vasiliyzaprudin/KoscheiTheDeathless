package com.mining.koscheithedeathless.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // отключаем CSRF для тестов
                .authorizeHttpRequests(auth -> auth
                        // Разрешаем статику и API без логина
                        .requestMatchers("/js/**", "/css/**", "/api/**", "/index.html", "/").permitAll()
                        // ВРЕМЕННО: всё остальное тоже открыто (для тестов)
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form.disable()) // отключаем форму логина
                .httpBasic(basic -> basic.disable()); // отключаем Basic Auth
        return http.build();
    }
}