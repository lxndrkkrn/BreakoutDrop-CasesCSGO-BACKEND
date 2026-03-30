package org.example.breakoutdrop.Services.ApplicationServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.DTOs.Balance.P2pAddBalanceDTO;
import org.example.breakoutdrop.Entities.PromoCode;
import org.example.breakoutdrop.Entities.User;
import org.example.breakoutdrop.Services.DomainServices.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor

public class ReplenishmentOfBalanceService {

    private final CaseService caseService;
    private final UserService userService;
    private final SkinService skinService;
    private final InventoryService inventoryService;
    private final TransactionService transactionService;
    private final PromoCodeService promoCodeService;

    @Transactional
    public void p2pAddBalance(Long id, P2pAddBalanceDTO p2pAddBalanceDTO) {
        BigDecimal deltaBalance = p2pAddBalanceDTO.deltaBalance();
        BigDecimal factor = BigDecimal.ONE;

        if (p2pAddBalanceDTO.promoCode() != null && !p2pAddBalanceDTO.promoCode().isBlank()) {
            try {
                PromoCode promo = promoCodeService.findFirstByName(p2pAddBalanceDTO.promoCode());
                if (promo != null) {
                    factor = promo.getFactor();
                }
            } catch (Exception e) {
                log.warn("Промокод '{}' не найден или неактивен, игнорируем", p2pAddBalanceDTO.promoCode());
            }
        }

        transactionService.processDeposit(id, deltaBalance);
        userService.addBalanceToUser(id, deltaBalance.multiply(factor));
    }


}
