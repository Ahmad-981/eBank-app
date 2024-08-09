package com.practice.project06.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/by-account")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@RequestParam Long id) {
        List<Transaction> transactions = transactionService.findTransactionsByAccountId(id);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        try {
            Transaction transaction = transactionService.createTransaction(
                    transactionDTO.getFromAccountID(),
                    transactionDTO.getToAccountNumber(),
                    transactionDTO.getAmount());
            return ResponseEntity.ok(transaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

}