package org.example.breakoutdrop.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.breakoutdrop.Entities.Case;
import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Enums.SkinsRarity;
import org.example.breakoutdrop.Enums.WeaponType;

import java.math.BigDecimal;
import java.util.List;

public record CreateSkinDTO(
        @NotNull String name,
        @NotNull WeaponType weaponType,
        @NotNull @Positive BigDecimal price,
        @NotNull SkinsRarity rarity,
        @NotNull Double chance,
        List<Long> caseIds
) { }
