package org.example.breakoutdrop.Services.ApplicationServices;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.DTOs.Create.CreateCaseDTO;
import org.example.breakoutdrop.DTOs.Create.CreateInventoryDTO;
import org.example.breakoutdrop.DTOs.OpeningCaseDTO;
import org.example.breakoutdrop.Entities.Case;
import org.example.breakoutdrop.Entities.Inventory;
import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Entities.User;
import org.example.breakoutdrop.Errors.ClientHTTP.NotFound404;
import org.example.breakoutdrop.Errors.Server.CaseIsEmpty;
import org.example.breakoutdrop.Services.DomainServices.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
@Slf4j

public class OpenCaseService {

    private final CaseService caseService;
    private final UserService userService;
    private final SkinService skinService;
    private final InventoryService inventoryService;

    private final TransactionService transactionService;

    public OpenCaseService(CaseService caseService, UserService userService, SkinService skinService, InventoryService inventoryService, TransactionService transactionService) {
        this.caseService = caseService;
        this.userService = userService;
        this.skinService = skinService;
        this.inventoryService = inventoryService;
        this.transactionService = transactionService;
    }

    @Transactional
    public void userOpeningCase(OpeningCaseDTO openingCaseDTO) {
        log.info("Попытка открытия кейса");
        try {
            User user = userService.findUserById(openingCaseDTO.userId());
            Case openingCase = caseService.findCaseById(openingCaseDTO.caseId());

            BigDecimal oldBalance = user.getBalance();

            takeBalance(openingCaseDTO.userId(), openingCase.getPrice());
            Skin wonSkin = skinCalculation(openingCase);

            CreateInventoryDTO createInventoryDTO = new CreateInventoryDTO(
                    openingCaseDTO.userId(),
                    wonSkin.getId()
            );

            inventoryService.createInventory(createInventoryDTO);

            //transactionService.createTransactionLog(openingCaseDTO.userId(), openingCaseDTO.caseId(), wonSkin.getId(), oldBalance, );
            log.info("Кейс успешно открыт. Выпавший предмет: {}", wonSkin);
        } catch (Exception e) {
            log.error("Ошибка при открытии кейса");
            throw e;
        }
    }

    private void takeBalance(Long id, BigDecimal deltaBalance) {
        userService.takeBalanceToUser(id, deltaBalance);
    }

    private Skin skinCalculation(Case openingCase) {
        log.info("Попытка выбора скина из кейса");
        try {

            List<Skin> skinList = openingCase.getSkinList();

            if (skinList.isEmpty()) {
                throw new CaseIsEmpty("Кейс '" + openingCase.getName() + "' пуст! Добавьте скины.");
            }

            double totalChance = skinList.stream().mapToDouble(skinService::getChanceSkinBySkin).sum();
            double randomValue = new Random().nextDouble() * totalChance;

            double currentSum = 0;
            for (Skin skin : skinList) {
                currentSum += skinService.getChanceSkinBySkin(skin).doubleValue();
                if (randomValue <= currentSum) {
                    return skin; // "Успешный" скин - передаётся наверх
                }
            }
            log.info("Скин выбран и передан");
            return skinList.getLast(); // "Сломанный" скин - передаётся наверх

        } catch (Exception e) {
            log.error("Ошибка при выборе скина из кейса");
            throw e;
        }
    }

}
