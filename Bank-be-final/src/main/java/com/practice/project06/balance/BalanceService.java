package com.practice.project06.balance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;

    public Balance createBalance(Balance balance) {
        return balanceRepository.save(balance);
    }
}
