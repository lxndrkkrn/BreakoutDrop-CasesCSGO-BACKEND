package org.example.breakoutdrop.DTOs.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginDTO(
        @NotNull @Email(message = "Некоректный Email") String email,
        @NotNull @Size(min = 6, message = "Пароль должен быть не менее 6 символов") String password
) { }
