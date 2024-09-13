package com.practice.project06.transaction;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionDTO {
    private Long fromAccountID;
    private String toAccountNumber;
    private BigDecimal amount;
    private String indicator;
}