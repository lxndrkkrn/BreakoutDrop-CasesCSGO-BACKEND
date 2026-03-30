package org.example.breakoutdrop.DTOs.Balance;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.breakoutdrop.Entities.PromoCode;

import java.math.BigDecimal;

public record P2pAddBalanceDTO(
        @NotNull @Positive BigDecimal deltaBalance,
        String promoCode
) { }
