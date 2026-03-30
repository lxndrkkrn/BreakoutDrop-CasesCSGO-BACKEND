package org.example.breakoutdrop.Services.ApplicationServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.DTOs.Create.CreateInventoryDTO;
import org.example.breakoutdrop.DTOs.Balance.Open.OpeningContractDTO;
import org.example.breakoutdrop.Entities.Inventory;
import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Entities.SystemWallet;
import org.example.breakoutdrop.Entities.User;
import org.example.breakoutdrop.Enums.TransactionType;
import org.example.breakoutdrop.Errors.Client.InvalidValue;
import org.example.breakoutdrop.Errors.Server.ImpossibleContract;
import org.example.breakoutdrop.Errors.ServerHTTP.ServiceUnavailable503;
import org.example.breakoutdrop.Repositories.SystemWalletRepository;
import org.example.breakoutdrop.Services.DomainServices.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;
@Service
@Slf4j
@RequiredArgsConstructor
public class UseOfTheContractService {

    private final UserService userService;
    private final SkinService skinService;
    private final InventoryService inventoryService;
    private final TransactionService transactionService;
    private final SystemWalletRepository systemWalletRepository;

    private final BigDecimal coefficient = new BigDecimal("2.5");
    private final BigDecimal minPriceSum = new BigDecimal("30");
    private final BigDecimal maxPriceSum = new BigDecimal("50000");

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public Long useOfTheContract(OpeningContractDTO openingContractDTO) {
        log.info("Попытка создания контракта для пользователя {}", openingContractDTO.userId());

        try {
            // 1. Блокируем кошелек для безопасного расчета пула
            SystemWallet wallet = systemWalletRepository.findWithLock()
                    .orElseThrow(() -> new ServiceUnavailable503("Сервис временно недоступен (нет сейфа)"));

            // 2. Получаем КОНКРЕТНЫЕ предметы из инвентаря
            List<Inventory> usedInventories = inventoryService.findAllById(openingContractDTO.inventoryIds());

            // 3. Валидация владения и количества
            if (usedInventories.size() != openingContractDTO.inventoryIds().size()) {
                throw new IllegalArgumentException("Некоторые предметы не найдены в базе данных");
            }

            boolean allOwned = usedInventories.stream()
                    .allMatch(inv -> inv.getUser().getId().equals(openingContractDTO.userId()));

            if (!allOwned) {
                throw new IllegalArgumentException("Один или несколько предметов вам не принадлежат");
            }

            int itemsCount = usedInventories.size();
            if (itemsCount < 3 || itemsCount > 10) {
                throw new IllegalArgumentException("Контракт доступен только для 3-10 предметов (сейчас: " + itemsCount + ")");
            }

            // 4. Расчет стоимости контракта на основе реальных скинов из инвентаря
            List<Skin> inputSkins = usedInventories.stream().map(Inventory::getSkin).toList();
            BigDecimal priceSum = getPriceSum(inputSkins);

            if (priceSum.compareTo(minPriceSum) < 0 || priceSum.compareTo(maxPriceSum) > 0) {
                throw new InvalidValue("Суммарная цена контракта должна быть от 30 до 50 000 руб.");
            }

            // 5. Определение границ выигрыша
            BigDecimal minPrice = priceSum.divide(coefficient, 2, RoundingMode.HALF_UP);
            BigDecimal maxPrice = priceSum.multiply(coefficient);

            // Если в сейфе денег меньше, чем максимальный выигрыш, ограничиваем его сейфом
            if (wallet.getPrizePool().compareTo(maxPrice) < 0) {
                maxPrice = wallet.getPrizePool();
            }

            if (maxPrice.compareTo(minPrice) < 0) {
                throw new ImpossibleContract("В системе недостаточно средств для обеспечения этого контракта");
            }

            // 6. Выбор выигрышного скина
            Skin wonSkin = getRandomSkin(minPrice, maxPrice);

            // 7. Проведение транзакций и обновление инвентаря
            User user = userService.findUserById(openingContractDTO.userId());

            // Списываем из сейфа стоимость выигрыша
            wallet.setPrizePool(wallet.getPrizePool().subtract(wonSkin.getPrice()));

            // Удаляем старые вещи, добавляем новую
            inventoryService.deleteAll(usedInventories);
            inventoryService.createInventory(new CreateInventoryDTO(user.getId(), wonSkin.getId()));

            // Логируем выигрыш в транзакции
            transactionService.createWinTransaction(user, wonSkin.getPrice(), TransactionType.CONTRACT);

            log.info("Контракт завершен. Юзер: {}, Выпал: {} ({} руб)", user.getId(), wonSkin.getName(), wonSkin.getPrice());
            return wonSkin.getId();

        } catch (Exception e) {
            log.error("Ошибка контракта: {}", e.getMessage());
            throw e;
        }
    }

    private BigDecimal getPriceSum(List<Skin> skins) {
        return skins.stream()
                .map(Skin::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Skin getRandomSkin(BigDecimal minPrice, BigDecimal maxPrice) {
        BigDecimal randomMultiplier = BigDecimal.valueOf(secureRandom.nextDouble());
        BigDecimal priceDiff = maxPrice.subtract(minPrice);
        BigDecimal targetPrice = minPrice.add(priceDiff.multiply(randomMultiplier));

        return skinService.findSkinByClosestPrice(targetPrice);
    }
}
