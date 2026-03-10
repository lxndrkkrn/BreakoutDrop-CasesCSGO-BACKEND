package org.example.breakoutdrop.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "inventories")

public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Кто владелец

    @ManyToOne
    @JoinColumn(name = "skin_id")
    private Skin skin; // Какой именно это скин (ссылка на шаблон)

    @NotNull
    @Column(nullable = false)
    private LocalDateTime droppedAt = LocalDateTime.now();

}
