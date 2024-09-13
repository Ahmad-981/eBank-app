package com.practice.project06.balance;

import com.practice.project06.account.Account;
import com.practice.project06.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class BalanceController {

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private BalanceService  balanceService;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/api/v1/accounts/balance")
    public ResponseEntity<?> createBalance(@RequestBody Long accountId) {

        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Account not found for ID: " + accountId);
        }
        Optional<Balance> existingBalance = balanceRepository.findByAccount(account);
        if (existingBalance.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Balance already exists for this account");
        }

        Balance balance = new Balance();
        balance.setAmount(BigDecimal.valueOf(100));
        balance.setIndicator("CR");
        balance.setAccount(account);
        balance.setDate(new Date());

        Balance savedBalance = balanceService.createBalance(balance);
        return ResponseEntity.ok(savedBalance);
    }


    @GetMapping("/api/v1/accounts/{accountId}/balance")
    public Balance getBalanceById(@PathVariable Long accountId) {
        return balanceRepository.findBalanceByAccountId(accountId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/api/v1/accounts/balance")
    public List<Balance> getAllBalances() {
        return balanceRepository.findAll();
    }
}