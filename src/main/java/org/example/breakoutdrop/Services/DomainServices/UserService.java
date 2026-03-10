package org.example.breakoutdrop.Services.DomainServices;

import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.DTOs.Create.CreateUserDTO;
import org.example.breakoutdrop.Entities.User;
import org.example.breakoutdrop.Errors.Client.NegativeBalance;
import org.example.breakoutdrop.Errors.ClientHTTP.NotFound404;
import org.example.breakoutdrop.Repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void createUser(CreateUserDTO createUserDTO) {
        log.info("Попытка создания пользователя");
        try {
            User user = new User();

            user.setName(createUserDTO.name());
            user.setEmail(createUserDTO.email());
            user.setPassword(createUserDTO.password());
            user.setTradeURL(createUserDTO.tradeURL());

            userRepository.save(user);

            log.info("Пользователь успешно создан");
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

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void addBalanceToUser(Long id, BigDecimal deltaBalance) {
        log.info("Попытка пополнения баланся пользователю");
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new NotFound404("Пользователь не найден"));
            BigDecimal userBalance = user.getBalance();

            user.setBalance(userBalance.add(deltaBalance));

            userRepository.save(user);

            log.info("Баланс успешно пополнен");
        } catch (Exception e) {
            log.error("Ошибка при пополнении баланса");
            throw e;
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
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

            user.setPassword(newPassword);

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

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFound404("Кейс не найден"));
    }

}