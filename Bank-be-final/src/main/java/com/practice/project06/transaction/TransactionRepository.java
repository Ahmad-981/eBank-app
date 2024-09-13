package com.practice.project06.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccount_AccountID(Long accountId);
    List<Transaction> findByToAccount_AccountID(Long toAccountId);
    @Modifying
    @Query("DELETE FROM transactions t WHERE t.fromAccount.accountID = :accountId")
    void deleteByFromAccountId(@Param("accountId") Long accountId);
    @Modifying
    @Query("DELETE FROM transactions t WHERE t.toAccount.accountID = :accountId")
    void deleteByToAccountId(@Param("accountId") Long accountId);
}