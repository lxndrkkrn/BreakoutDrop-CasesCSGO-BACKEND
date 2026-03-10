package org.example.breakoutdrop.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.example.breakoutdrop.Enums.SkinsRarity;
import org.example.breakoutdrop.Enums.WeaponType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "skins")

public class Skin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    private WeaponType type;

    @NotNull
    @Column(nullable = false)
    @Positive
    private BigDecimal price;

    @NotNull
    private SkinsRarity rarity;

    @NotNull
    private Double chance = 0d;

    @ManyToMany
    @JoinTable(name = "skin_cases", joinColumns = @JoinColumn(name = "skin_id"), inverseJoinColumns = @JoinColumn(name = "case_id"))
    private List<Case> cases = new ArrayList<>();

}
