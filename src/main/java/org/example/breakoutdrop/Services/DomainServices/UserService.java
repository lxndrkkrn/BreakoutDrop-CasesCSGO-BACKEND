package org.example.breakoutdrop.Services.DomainServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.DTOs.Create.CreateUserDTO;
import org.example.breakoutdrop.Entities.Role;
import org.example.breakoutdrop.Entities.User;
import org.example.breakoutdrop.Errors.Client.NegativeBalance;
import org.example.breakoutdrop.Errors.ClientHTTP.NotFound404;
import org.example.breakoutdrop.Repositories.RoleRepository;
import org.example.breakoutdrop.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Попытка входа пользователя: {}", username);

        org.example.breakoutdrop.Entities.User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));

        String[] roles = user.getRoles().stream()
                .map(role -> role.getName())
                .toArray(String[]::new);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getName())
                .password(user.getPassword())
                .authorities(roles)
                .build();
    }

    @Transactional
    public User createUser(CreateUserDTO createUserDTO) {
        log.info("Попытка создания пользователя");
        try {
            User user = new User();

            user.setName(createUserDTO.name());
            user.setEmail(createUserDTO.email());
            user.setPassword(passwordEncoder.encode(createUserDTO.password()));
            user.setTradeURL(createUserDTO.tradeURL());

            if (createUserDTO.roles() == null || createUserDTO.roles().isEmpty()) {

                Role defaultRole = roleRepository.findById(1L)
                        .orElseThrow(() -> new NotFound404("Дефолтная роль не найдена"));
                user.setRoles(Set.of(defaultRole));

            } else {

                Set<Role> userRoles = createUserDTO.roles().stream()
                        .map(roleId -> roleRepository.findById(roleId)
                                .orElseThrow(() -> new NotFound404("Роль по id в БД не найдена: " + roleId)))
                        .collect(Collectors.toSet());
                user.setRoles(userRoles);

            }

            userRepository.save(user);

            log.info("Пользователь успешно создан");
            return user;
        } catch (Exception e) {
            log.error("Ошибка при создании пользователя");
            throw e;
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Попытка удаления пользователя");
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));

            userRepository.deleteById(id);

            log.info("Пользователь успешно удалён");
        } catch (Exception e) {
            log.error("Ошибка при удалении пользователя");
            throw e;
        }
    }

    @Transactional
    public void setBalanceToUser(Long id, BigDecimal newBalance) {
        log.info("Попытка установки баланса пользователю");
        try {
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                log.error("Ошибка при установке баланса");
                throw new NotFound404("");
            }
            User user = userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));

            user.setBalance(newBalance);

            userRepository.save(user);

            log.info("Баланс успешно установлен");
        } catch (Exception e) {
            log.error("Ошибка при установке баланса");
            throw e;
        }
    }

    @Transactional()
    public void addBalanceToUser(Long id, BigDecimal deltaBalance) {
        log.info("Попытка добавления баланся пользователю");
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));
            BigDecimal userBalance = user.getBalance();

            user.setBalance(userBalance.add(deltaBalance));

            userRepository.save(user);

            log.info("Баланс успешно добавлен");
        } catch (Exception e) {
            log.error("Ошибка при добавлении баланса");
            throw e;
        }
    }

    @Transactional()
    public void takeBalanceToUser(Long id, BigDecimal deltaBalance) {
        log.info("Попытка уменьшения баланся пользователю");
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));
            BigDecimal userBalance = user.getBalance();

            if (userBalance.compareTo(deltaBalance) < 0) {
                throw new NegativeBalance("Недостаточно средств на балансе / Баланс не может быть отрицательным");
            }

            user.setBalance(userBalance.subtract(deltaBalance));

            userRepository.save(user);

            log.info("Баланс успешно уменьшен");
        } catch (Exception e) {
            log.error("Ошибка при уменьшении баланса");
            throw e;
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void changeTradeURL(Long id, String newTradeURL) {
        log.info("Попытка изменения трейд-ссылки пользователю");
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));

            user.setTradeURL(newTradeURL);

            userRepository.save(user);

            log.info("Трейд-ссылка успешно изменена");
        } catch (Exception e) {
            log.error("Ошибка при изменении трейд-ссылки");
            throw e;
        }
    }

    @Transactional
    public void changePassword(Long id, String newPassword) {
        log.info("Попытка изменения пароля пользователю");
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));

            user.setPassword(passwordEncoder.encode(newPassword));

            userRepository.save(user);

            log.info("Пароль успешно изменен");
        } catch (Exception e) {
            log.error("Ошибка при изменении пароля");
            throw e;
        }
    }

    @Transactional
    public void changeEmail(Long id, String newEmail) {
        log.info("Попытка изменения email пользователю");
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));

            user.setEmail(newEmail);

            userRepository.save(user);

            log.info("Email успешно изменен");
        } catch (Exception e) {
            log.error("Ошибка при изменении email");
            throw e;
        }
    }

    @Transactional
    public User addRole(Long userId, Long roleId) {
        log.info("Попытка добавления роли пользователю");
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Юзер не найден"));

            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Роль не найдена"));

            user.getRoles().add(role);
            userRepository.save(user);

            log.info("Роль успешно добавлена");
            return user;
        } catch (Exception e) {
            log.error("Ошибка при добавлении роли");
            throw e;
        }
    }

    @Transactional
    public User deleteRole(Long userId, Long roleId) {
        log.info("Попытка удаления роли пользователю");
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Юзер не найден"));

            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Роль не найдена"));

            user.getRoles().remove(role);
            userRepository.save(user);

            log.info("Роль успешно удалена");
            return user;
        } catch (Exception e) {
            log.error("Ошибка при удалении роли");
            throw e;
        }
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFound404("Пользователь не найден"));
    }

}