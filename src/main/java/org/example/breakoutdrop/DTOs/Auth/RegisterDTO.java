package org.example.breakoutdrop.DTOs.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record RegisterDTO(
        @JsonProperty("name") @NotNull String name,
        @JsonProperty("password") @NotNull @Size(min = 6) String password,
        @JsonProperty("email") @NotNull @Email String email,
        @JsonProperty("tradeURL") @NotNull @URL String tradeURL
) {}