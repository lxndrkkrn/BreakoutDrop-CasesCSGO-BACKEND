package org.example.breakoutdrop.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record CreateUserDTO(
        @NotNull String name,
        @NotNull String password,
        @NotNull @Email String email,
        @NotNull @URL String tradeURL
) { }
