package org.example.breakoutdrop.Services.ApplicationServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.DTOs.Create.CreateInventoryDTO;
import org.example.breakoutdrop.DTOs.Balance.Open.OpeningCaseDTO;
import org.example.breakoutdrop.Entities.Case;
import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Entities.SystemWallet;
import org.example.breakoutdrop.Entities.User;
import org.example.breakoutdrop.Enums.TransactionType;
import org.example.breakoutdrop.Errors.Client.NegativeBalance;
import org.example.breakoutdrop.Errors.Server.CaseIsEmpty;
import org.example.breakoutdrop.Errors.ServerHTTP.ServiceUnavailable503;
import org.example.breakoutdrop.Repositories.SystemWalletRepository;
import org.example.breakoutdrop.Services.DomainServices.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor

public class OpenCaseService {

    private final CaseService caseService;
    private final UserService userService;
    private final SkinService skinService;
    private final InventoryService inventoryService;

    private final SystemWalletRepository systemWalletRepository;

    private final TransactionService transactionService;

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public String userOpeningCase(OpeningCaseDTO openingCaseDTO) {
        log.info("Попытка открытия кейса");
        try {
            User user = userService.findUserById(openingCaseDTO.userId());
            Case openingCase = caseService.findCaseById(openingCaseDTO.caseId());

            BigDecimal oldBalance = user.getBalance();

            if (oldBalance.compareTo(openingCase.getPrice()) <= 0) {
                throw new NegativeBalance("Недостаточно средств");
            }

            takeBalance(openingCaseDTO.userId(), openingCase.getPrice());
            Skin wonSkin = skinCalculation(openingCase);

            CreateInventoryDTO createInventoryDTO = new CreateInventoryDTO(
                    openingCaseDTO.userId(),
                    wonSkin.getId()
            );

            transactionService.createWinTransaction(user, wonSkin.getPrice(), TransactionType.CASE_OPENING);
            inventoryService.createInventory(createInventoryDTO);
            //transactionService.createTransactionLog(openingCaseDTO.userId(), openingCaseDTO.caseId(), wonSkin.getId(), oldBalance, );
            log.info("Кейс успешно открыт. Выпавший предмет: {}", wonSkin);
            return wonSkin.getName();
        } catch (Exception e) {
            log.error("Ошибка при открытии кейса");
            throw e;
        }
    }

    private Skin skinCalculation(Case openingCase) {
        SystemWallet wallet = systemWalletRepository.findWithLock()
                .orElseThrow(() -> new ServiceUnavailable503("Нет доступного сейфа"));

        BigDecimal prizePool = wallet.getPrizePool();
        List<Skin> fullSkinList = openingCase.getSkinList();

        List<Skin> affordableSkins = fullSkinList.stream()
                .filter(skin -> skin.getPrice().compareTo(prizePool) <= 0)
                .toList();

        if (affordableSkins.isEmpty()) {
            log.warn("Сейф пуст (баланс: {}). Нет доступных скинов в кейсе {}", prizePool, openingCase.getName());
            throw new ServiceUnavailable503("Извините, в призовом фонде недостаточно средств");
        }

        double totalChance = affordableSkins.stream().mapToDouble(skinService::getChanceSkinBySkin).sum();
        double randomValue = totalChance * secureRandom.nextDouble();

        double currentSum = 0;
        Skin selectedSkin = null;

        for (Skin skin : affordableSkins) {
            currentSum += skinService.getChanceSkinBySkin(skin);
            if (randomValue <= currentSum) {
                selectedSkin = skin;
                break;
            }
        }

        if (selectedSkin == null) {
            selectedSkin = affordableSkins.get(affordableSkins.size() - 1);
        }

        BigDecimal newWallet = new BigDecimal(wallet.getPrizePool().subtract(selectedSkin.getPrice()).toString());

        log.info("Скин выбран: {}. Цена: {}. Остаток в сейфе: {}", selectedSkin.getName(), selectedSkin.getPrice(), wallet.getPrizePool());

        return selectedSkin;
    }


    private void takeBalance(Long id, BigDecimal deltaBalance) {
        userService.takeBalanceToUser(id, deltaBalance);
    }

}
