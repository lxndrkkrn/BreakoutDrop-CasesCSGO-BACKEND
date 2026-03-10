package org.example.breakoutdrop.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "transactions")

public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long userId;

    @NotNull
    @Column(nullable = false)
    private Long caseId;

    @NotNull
    @Column(nullable = false)
    private Long wonSkinId;

    @NotNull
    @Column(nullable = false)
    private BigDecimal oldBalance;

    @NotNull
    @Column(nullable = false)
    private BigDecimal newBalance;

    @NotNull
    @Column(nullable = false)
    private BigDecimal deltaBalance;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime localDateTime = LocalDateTime.now();

}
