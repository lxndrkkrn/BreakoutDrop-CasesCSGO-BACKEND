package org.example.breakoutdrop.DTOs.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record RegisterDTO(
        @NotNull String name,
        @NotNull @Size(min = 6, message = "Пароль должен быть не менее 6 символов") String password,
        @NotNull @Email(message = "Некоректный Email") String email,
        @NotNull @URL(message = "Некоректный URL") String tradeURL
) {
}
