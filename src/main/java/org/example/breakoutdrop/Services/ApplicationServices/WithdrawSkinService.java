package org.example.breakoutdrop.Services.ApplicationServices;

import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.DTOs.SellSkinDTO;
import org.example.breakoutdrop.Entities.Inventory;
import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Entities.User;
import org.example.breakoutdrop.Services.DomainServices.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j

public class WithdrawSkinService {

    private final CaseService caseService;
    private final UserService userService;
    private final SkinService skinService;
    private final InventoryService inventoryService;

    private final TransactionService transactionService;

    public WithdrawSkinService(CaseService caseService, UserService userService, SkinService skinService, InventoryService inventoryService, TransactionService transactionService) {
        this.caseService = caseService;
        this.userService = userService;
        this.skinService = skinService;
        this.inventoryService = inventoryService;
        this.transactionService = transactionService;
    }

    @Transactional
    public void withdrawSkin(SellSkinDTO sellSkinDTO) {
        log.info("Попытка вывода скина");
        try {
            User user = userService.findUserById(sellSkinDTO.userId());
            Skin skin = skinService.findSkinById(sellSkinDTO.skinId());
            Inventory inventory = inventoryService.findInventoryByUserAndSkin(user, skin);

            if (!inventory.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("У вас нет такого скина");
            }
            if (user.getTradeURL().isEmpty()) {
                throw new IllegalArgumentException("У вас не указана трейд-ссылка");
            }

            // more if's ...

            // logic ...

            log.info("Успешный вывод скина: Скин: {}; Пользователь: {}", skin, user);
        } catch (Exception e) {
            log.error("Ошибка при выводе скина");
            throw e;
        }
    }

}
