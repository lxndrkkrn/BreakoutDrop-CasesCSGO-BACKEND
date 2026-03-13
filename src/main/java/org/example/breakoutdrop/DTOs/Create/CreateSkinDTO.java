package org.example.breakoutdrop.DTOs.Create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.breakoutdrop.Enums.SkinsRarity;
import org.example.breakoutdrop.Enums.WeaponType;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.List;

public record CreateSkinDTO(
        @NotNull String name,
        @NotNull WeaponType weaponType,
        @NotNull @Positive BigDecimal price,
        @NotNull SkinsRarity rarity,
        @NotNull Double chance,
        @NotNull @URL String skinPicture,
        List<Long> caseIds
) { }
