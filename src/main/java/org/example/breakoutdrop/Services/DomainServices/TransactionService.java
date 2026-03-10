package org.example.breakoutdrop.Services.DomainServices;

import lombok.extern.slf4j.Slf4j;
import org.example.breakoutdrop.Entities.Transaction;
import org.example.breakoutdrop.Repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j

public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void createTransactionLog(Long userId,
                                     Long caseId,
                                     Long wonSkinId,
                                     BigDecimal oldBalance,
                                     BigDecimal newBalance,
                                     BigDecimal deltaBalance) {

        Transaction transaction = new Transaction();

        transaction.setUserId(userId);
        transaction.setCaseId(caseId);
        transaction.setWonSkinId(wonSkinId);
        transaction.setOldBalance(oldBalance);
        transaction.setNewBalance(newBalance);
        transaction.setDeltaBalance(deltaBalance);

        transactionRepository.save(transaction);
        log.info("Транзакция сохранена: ID {}; Date {}", transaction.getId(), transaction.getLocalDateTime());

    }

}
