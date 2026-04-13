package org.example.breakoutdrop.Services.JWT;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.Entities.User;
import org.example.breakoutdrop.Errors.ClientHTTP.Unauthorized401;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor

public class JwtUtils {

    private final PasswordEncoder passwordEncoder;

    private String secretCode = "shkGGkdofMvXXpbXvdMKwldFsuMhDwWAcZoSMvYkZTLZuPydlKoudRGNsCRyjOnK";

    public SecretKey getSigningKey() {
        byte[] keyBytes = secretCode.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(secretCode.getBytes(StandardCharsets.UTF_8));

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    public String generateToken(User user) {
        log.info("Создание JWT токена: {}", user);
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("roles", user.getRoles())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + 1000 * 60 * 60 * 24 * 7))
                .signWith(getSigningKey())
                .compact();
    }

    public String getEmailFromToken(String token) {
        SecretKey key = getSigningKey();

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        log.info("Проверка валидации JWT токена");
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            log.info("Токен успешно прошёл проверку");

            return true;
        } catch (Exception e) {
            log.error("У токена поврежденна цифровая подпись или его срок действия истёк. (VT - 401 / 498)");
            return false;
        }
    }

}
