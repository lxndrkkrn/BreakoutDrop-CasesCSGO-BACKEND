package org.example.breakoutdrop.DTOs.Create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.example.breakoutdrop.Entities.Role;
import org.example.breakoutdrop.Enums.UserRole;
import org.hibernate.validator.constraints.URL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record CreateUserDTO(
        @NotNull String name,
        @NotNull String password,
        @NotNull @Email String email,
        @NotNull @URL String tradeURL,
        Set<Long> roles
) {
    public CreateUserDTO() {
        this("", "", "", "", new HashSet<>() {});
    }
}
