package org.example.breakoutdrop.Repositories;

import org.example.breakoutdrop.Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
