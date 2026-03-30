package org.example.breakoutdrop.Services.DomainServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.Entities.SystemWallet;
import org.example.breakoutdrop.Entities.Transaction;
import org.example.breakoutdrop.Entities.User;
import org.example.breakoutdrop.Enums.TransactionType;
import org.example.breakoutdrop.Errors.ServerHTTP.ServiceUnavailable503;
import org.example.breakoutdrop.Repositories.SystemWalletRepository;
import org.example.breakoutdrop.Repositories.TransactionRepository;
import org.example.breakoutdrop.Repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor

public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final SystemWalletRepository systemWalletRepository;

    @Transactional
    public void processDeposit(Long userId, BigDecimal amount) {
        User user = userService.findUserById(userId);
        SystemWallet wallet = systemWalletRepository.findWithLock().orElseThrow(() -> new ServiceUnavailable503("Нет доступных сейфов"));

        BigDecimal companyProfit = amount.multiply(new BigDecimal("0.20"));
        BigDecimal userNetDeposit = amount.subtract(companyProfit);

        createTransaction(user, amount, TransactionType.DEPOSIT);
        createTransaction(user, companyProfit.negate(), TransactionType.SERVICE_FEE);

        wallet.setTotalProfit(wallet.getTotalProfit().add(companyProfit));
        wallet.setPrizePool(wallet.getPrizePool().add(userNetDeposit));

        log.info("Депозит: Юзер {} +{}, Доход казино +{}, Пул +{}",
                userId, userNetDeposit, companyProfit, userNetDeposit);
    }

    @Transactional
    public void createTransaction(User user, BigDecimal amount, TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setUser(user);

        transactionRepository.save(transaction);
    }

    @Transactional
    public void createWinTransaction(User user, BigDecimal amount, TransactionType transactionType) {

        SystemWallet wallet = systemWalletRepository.findWithLock().orElseThrow(() -> new ServiceUnavailable503("Нет доступных сейфов"));

        createTransaction(user, amount, transactionType);
        wallet.setPrizePool(wallet.getPrizePool().subtract(amount));

    }

}
