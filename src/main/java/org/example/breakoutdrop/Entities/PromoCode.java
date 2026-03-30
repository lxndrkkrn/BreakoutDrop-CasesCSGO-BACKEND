package org.example.breakoutdrop.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promoCodes")
@Getter
@Setter

public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String code;

    @NotNull
    @Column(nullable = false)
    @Positive(message = "Промо-код должен давать бонус")
    private BigDecimal factor;

    @NotNull
    @Column(nullable = false)
    private BigDecimal name;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime localDateTime = LocalDateTime.now();

}
