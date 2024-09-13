package com.practice.project06.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/api/v1/accounts/transactions/by-account")
    public ResponseEntity<?> getTransactionsByAccountId(@RequestParam Long id) {
        try{
            List<Transaction> transactions = transactionService.findTransactionsByAccountId(id);
                 return ResponseEntity.ok(transactions);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/api/v1/accounts/transactions")
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