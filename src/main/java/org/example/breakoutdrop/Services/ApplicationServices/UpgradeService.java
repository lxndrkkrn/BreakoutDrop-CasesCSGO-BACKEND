package org.example.breakoutdrop.Services.ApplicationServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.DTOs.Create.CreateInventoryDTO;
import org.example.breakoutdrop.DTOs.OpeningUpgradeDTO;
import org.example.breakoutdrop.Entities.Inventory;
import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Entities.SystemWallet;
import org.example.breakoutdrop.Entities.User;
import org.example.breakoutdrop.Enums.TransactionType;
import org.example.breakoutdrop.Errors.ServerHTTP.ServiceUnavailable503;
import org.example.breakoutdrop.Repositories.SystemWalletRepository;
import org.example.breakoutdrop.Services.DomainServices.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
@RequiredArgsConstructor

public class UpgradeService {

    private final CaseService caseService;
    private final UserService userService;
    private final SkinService skinService;
    private final InventoryService inventoryService;

    private final SystemWalletRepository systemWalletRepository;

    private final TransactionService transactionService;

    private static final BigDecimal MAX_UPGRADE_CHANCE = new BigDecimal("0.75");
    private static final BigDecimal MIN_UPGRADE_CHANCE = new BigDecimal("0.01");

    @Transactional
    public void upgradeSkin(OpeningUpgradeDTO openingUpgradeDTO) {
        log.info("Попытка апгрейднуть скин");
        try {
            SystemWallet wallet = systemWalletRepository.findWithLock().orElseThrow(() -> new ServiceUnavailable503("Нет доступного сейфа"));
            BigDecimal prizePool = wallet.getPrizePool();

            User user = userService.findUserById(openingUpgradeDTO.userId());
            Skin suppliedSkin = skinService.findSkinById(openingUpgradeDTO.suppliedSkin());
            Skin wonSkin = skinService.findSkinById(openingUpgradeDTO.wonSkin());

            Inventory inventoryItem = inventoryService.findInventoryByUserAndSkin(user, suppliedSkin);

            if (suppliedSkin.getId().equals(wonSkin.getId()) || suppliedSkin.equals(wonSkin)) {
                throw new IllegalArgumentException("Вы не можете апгрейднуть скин до самого себя");
            }

            if (!inventoryItem.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("У вас нет такого скина");
            }

            BigDecimal priceSuppliedSkin = suppliedSkin.getPrice();
            BigDecimal priceWonSkin = wonSkin.getPrice();

            BigDecimal winningPercentage = priceSuppliedSkin.divide(priceWonSkin, 4, RoundingMode.HALF_UP);

            if (winningPercentage.compareTo(MAX_UPGRADE_CHANCE) > 0) {
                throw new IllegalArgumentException("Апгрейд невозможен: шанс выигрыша не должен превышать 75%");
            } else if (winningPercentage.compareTo(MIN_UPGRADE_CHANCE) < 0) {
                throw new IllegalArgumentException("Апгрейд невозможен: шанс выигрыша не должен быть меньше 1%");
            }

            inventoryService.deleteInventoryById(inventoryItem.getId());

            if (winningUpgrade(winningPercentage, wonSkin, wallet)) {

                CreateInventoryDTO createInventoryDTO = new CreateInventoryDTO(user.getId(), wonSkin.getId());
                inventoryService.createInventory(createInventoryDTO);

                transactionService.createWinTransaction(user, wonSkin.getPrice(), TransactionType.UPGRADING);

                log.info("Апгрейд успешный, скин добавлен в инвентарь");

            } else {
                log.info("Апгрейд неудачен, скин потерян");
            }

            log.info("Апгрейд успешно прошёл");
        } catch (Exception e) {
            log.error("Ошибка при апгрейдинге скина");
            throw e;
        }
    }

    private boolean winningUpgrade(BigDecimal winningPercentage, Skin wonSkin, SystemWallet wallet) {

        if (wonSkin.getPrice().compareTo(wallet.getPrizePool()) >= 0) {
            return false;
        }

        BigDecimal chanceThreshold = new BigDecimal(winningPercentage.toString());
        BigDecimal randomShot = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble());

        if (randomShot.compareTo(chanceThreshold) <= 0) {
            return true;
        } else {
            return false;
        }
    }

}
