package org.example.breakoutdrop.Services.ApplicationServices;

import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.DTOs.Create.CreateInventoryDTO;
import org.example.breakoutdrop.DTOs.OpeningContractDTO;
import org.example.breakoutdrop.Entities.Inventory;
import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Entities.User;
import org.example.breakoutdrop.Errors.Client.InvalidValue;
import org.example.breakoutdrop.Services.DomainServices.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Slf4j

public class UseOfTheContractService {

    private final CaseService caseService;
    private final UserService userService;
    private final SkinService skinService;
    private final InventoryService inventoryService;

    private final TransactionService transactionService;

    public UseOfTheContractService(CaseService caseService, UserService userService, SkinService skinService, InventoryService inventoryService, TransactionService transactionService) {
        this.caseService = caseService;
        this.userService = userService;
        this.skinService = skinService;
        this.inventoryService = inventoryService;
        this.transactionService = transactionService;
    }

//    private final BigDecimal maxCoefficient = new BigDecimal("2.5");
//    private final BigDecimal minCoefficient = new BigDecimal("2.5");
    private final BigDecimal coefficient = new BigDecimal("2.5");

    private final BigDecimal minPriceSum = new BigDecimal("30");
    private final BigDecimal maxPriceSum = new BigDecimal("50000");

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public void useOfTheContractService(OpeningContractDTO openingContractDTO) {
        log.info("Попытка сделать контракт");
        try {

            User user = userService.findUserById(openingContractDTO.userId());
            List<Skin> skins = skinService.findListSkinById(openingContractDTO.skinId());

            List<Inventory> usedInventories = inventoryService.findInventoryListBySkin(user, new ArrayList<>(skins));

            if (usedInventories.size() != skins.size()) {
                throw new IllegalArgumentException("У вас нет некоторых скинов");
            }

            if (skins.size() < 4) {
                throw new IllegalArgumentException("Контракт можно создать минимум из 3-х скинов");
            } else if (skins.size() > 10) {
                throw new IllegalArgumentException("Контракт можно создать максимум из 10-х скинов");
            }

            BigDecimal priceSum = getPriceSum(skins);
            if (priceSum.compareTo(minPriceSum) <= 0 || priceSum.compareTo(maxPriceSum) >= 0) {
                throw new InvalidValue("Цена контракта должна быть более 30р и менее 50000р");
            }

            BigDecimal maxPrice = priceSum.multiply(coefficient);
            BigDecimal minPrice = priceSum.divide(coefficient, 0, RoundingMode.HALF_UP);

            Skin wonSkin = getRandomSkin(maxPrice, minPrice);

            CreateInventoryDTO createInventoryDTO = new CreateInventoryDTO(user.getId(), wonSkin.getId());

            inventoryService.deleteAll(usedInventories);
            inventoryService.createInventory(createInventoryDTO);

            log.info("Контракт успешно создан, выпавший скин: {}; забранные скины: {}", wonSkin, skins);
        } catch (Exception e) {
            log.error("Ошибка при создании контракта: {}", openingContractDTO);
            throw e;
        }
    }

    private BigDecimal getPriceSum(List<Skin> usedSkins) {

        return usedSkins.stream()
                .map(Skin::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    private Skin getRandomSkin(BigDecimal maxPrice, BigDecimal minPrice) {

        double randomMultiplier = secureRandom.nextDouble();

        BigDecimal priceDiff = maxPrice.subtract(minPrice);
        BigDecimal randomPrice = minPrice.add(priceDiff.multiply(BigDecimal.valueOf(randomMultiplier)));

        return skinService.findSkinByClosestPrice(randomPrice);
    }


}
