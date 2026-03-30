package org.example.breakoutdrop.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity

public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)

//                .authorizeHttpRequests(auth -> auth
//                        // 1. Публичные страницы (регистрация, статика, главная)
//                        .requestMatchers("/", "/case/**", "/upgrade/**", "/contract/**").permitAll()
//
//                        .requestMatchers("/register").anonymous()
//
//                        // 2. Страницы только для админов
//                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "SERVICE")
//
//                        // 3. Страницы для пользователей с ролью USER или ADMIN
//                        .requestMatchers("/profile/**").hasAnyRole("USER", "YT", "MODER", "ADMIN", "SERVICE")
//
//                        // 4. Все остальное — только после логина
//                        .anyRequest().authenticated()
//                )
//                .formLogin(login -> login
//                        .loginPage("/login")             // Твой эндпоинт для страницы логина
//                        .defaultSuccessUrl("/")          // Куда редиректить после успешного входа
//                        .permitAll()                     // Разрешить всем доступ к форме логина
//                )
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/")           // Куда отправить после выхода
//                        .permitAll()
//                )
                ;

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
