package com.practice.project06.balance;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BalanceDTO {
    private Long accountID;
    private BigDecimal amount;
    private String indicator;
}
