package org.example.breakoutdrop.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.example.breakoutdrop.Entities.Role;
import org.hibernate.validator.constraints.URL;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")

public class User implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

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
    @PositiveOrZero(message = "Баланс не может быть отрицательным")
    private BigDecimal balance = new BigDecimal(0);

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Inventory> inventory;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
