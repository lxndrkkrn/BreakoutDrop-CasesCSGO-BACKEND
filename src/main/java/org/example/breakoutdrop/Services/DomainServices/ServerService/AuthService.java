package org.example.breakoutdrop.Services.DomainServices.ServerService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.IToken;
import org.example.breakoutdrop.DTOs.Auth.LoginDTO;
import org.example.breakoutdrop.DTOs.Auth.RegisterDTO;
import org.example.breakoutdrop.DTOs.Create.CreateUserDTO;
import org.example.breakoutdrop.Entities.User;
import org.example.breakoutdrop.Repositories.UserRepository;
import org.example.breakoutdrop.Services.DomainServices.UserService;
import org.example.breakoutdrop.Services.JWT.JwtFilter;
import org.example.breakoutdrop.Services.JWT.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor

public class AuthService {

    private final UserService userService;
    private final JwtFilter jwtFilter;
    private final JwtUtils jwtUtils;

    @Transactional
    public ResponseEntity<?> register(RegisterDTO registerDTO, HttpServletResponse response) {

        Set<Long> roles = new HashSet<>();
        roles.add(1L);

        CreateUserDTO createUserDTO = new CreateUserDTO(
                registerDTO.name(),
                registerDTO.password(),
                registerDTO.email(),
                registerDTO.tradeURL(),
                roles
        );

        User newUser = userService.createUser(createUserDTO);

        String token = jwtUtils.generateToken(newUser);

        ResponseCookie cookie = ResponseCookie.from("jwt_token", token)
                .httpOnly(true)
                .secure(false)   // Для разработки на localhost ставим false
                .path("/")
                .maxAge(604800)
                .sameSite("Lax") // Важно для локальной работы
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Success");
    }

    public ResponseEntity<?> login(LoginDTO loginDTO, HttpServletResponse response) {

        User user = userService.findUserByEmail(loginDTO.email());

        String token = jwtUtils.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("jwt_token", token)
                .httpOnly(true)
                .secure(false)   // Для разработки на localhost ставим false
                .path("/")
                .maxAge(604800)
                .sameSite("Lax") // Важно для локальной работы
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Success");
    }

    public ResponseEntity<?> logout(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("jwt_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Logged out");
    }

}
