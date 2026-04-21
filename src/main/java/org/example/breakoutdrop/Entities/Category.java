package org.example.breakoutdrop.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.breakoutdrop.Enums.CategoryType;

import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
@Getter
@Setter

public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime localDateTime = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    private Case caseEntity;

}
