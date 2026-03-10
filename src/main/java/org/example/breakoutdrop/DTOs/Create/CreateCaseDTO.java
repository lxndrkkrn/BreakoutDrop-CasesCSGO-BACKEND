package org.example.breakoutdrop.DTOs.Create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record CreateCaseDTO(
    @NotNull String name,
    @NotNull @Positive BigDecimal price,
    List<Long> skinIds
) { }
