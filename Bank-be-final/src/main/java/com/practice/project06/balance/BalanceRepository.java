package com.practice.project06.balance;

import com.practice.project06.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    @Query("SELECT b FROM balances b WHERE b.account.accountID = :accountID")
    Balance findBalanceByAccountId(@Param("accountID") Long accountID);
    Optional<Balance> findByAccount(Account account);
    @Modifying
    @Query("DELETE FROM balances b WHERE b.account.accountID = :accountId")
    void deleteByAccountId(@Param("accountId") Long accountId);
    List<Balance> findAll();

}
