package org.example.breakoutdrop.Services.ApplicationServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.DTOs.Balance.P2pAddBalanceDTO;
import org.example.breakoutdrop.DTOs.Balance.Sell.SellAllSkinsDTO;
import org.example.breakoutdrop.DTOs.Balance.Sell.SellSkinDTO;
import org.example.breakoutdrop.Entities.Inventory;
import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Entities.User;
import org.example.breakoutdrop.Services.DomainServices.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor

public class SalesService {

    private final CaseService caseService;
    private final UserService userService;
    private final SkinService skinService;
    private final InventoryService inventoryService;
    private final ReplenishmentOfBalanceService replenishmentOfBalanceService;

    private final TransactionService transactionService;

    @Transactional
    public void sellSkin(SellSkinDTO sellSkinDTO) {
        log.info("Попытка продажи скина");
        try {
            User user = userService.findUserById(sellSkinDTO.userId());
            Skin skin = skinService.findSkinById(sellSkinDTO.skinId());
            Inventory inventory = inventoryService.findInventoryByUserAndSkin(user, skin);

            if (!inventory.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("У вас нет такого скина");
            }

            BigDecimal price = skin.getPrice();

            inventoryService.deleteInventory(inventory);
            userService.addBalanceToUser(user.getId(), price);

            log.info("Успешная продажа скина: Скин: {}; Пользователь: {}", skin, user);
        } catch (Exception e) {
            log.error("Ошибка при продаже скина");
            throw e;
        }
    }

    @Transactional
    public void sellAllSkins(SellAllSkinsDTO sellAllSkinsDTO) {
        log.info("Попытка продажи всех скинов");
        try {
            User user = userService.findUserById(sellAllSkinsDTO.userId());
            List<Inventory> inventories = inventoryService.findAllByUser(user);
            List<Skin> skins = inventories.stream().map(Inventory::getSkin).toList();
            List<BigDecimal> prices = skins.stream().map(Skin::getPrice).toList();

            if (inventories.size() != skins.size()) {
                throw new IllegalArgumentException("У вас нет таких скинов");
            }

            BigDecimal priceAll = prices.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

            P2pAddBalanceDTO addBalance = new P2pAddBalanceDTO(
                    priceAll.multiply(new BigDecimal("1.2")),
                    ""
            );

            inventoryService.deleteAll(inventories);
            replenishmentOfBalanceService.p2pAddBalance(user.getId(), addBalance);

            log.info("Успешная продажа скинов: Скины: {}; Пользователь: {}", skins, user);
        } catch (Exception e) {
            log.error("Ошибка при продаже скинов");
            throw e;
        }
    }

}
