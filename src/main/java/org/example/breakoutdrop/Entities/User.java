package org.example.breakoutdrop.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;

    @NotNull
    @Column(nullable = false)
    @Email(message = "Некоректный Email")
    private String email;

    @NotNull
    @Column(nullable = false)
    @URL(message = "Некоректный URL")
    private String tradeURL;

    @NotNull
    @Column(nullable = false)
    @Positive(message = "Баланс не может быть отрицательным")
    private BigDecimal balance = new BigDecimal(0);

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Inventory> inventory;

}
