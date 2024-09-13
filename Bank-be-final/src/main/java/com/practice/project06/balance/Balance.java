package com.practice.project06.balance;
import com.practice.project06.account.Account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity(name = "balances")
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "balanceID")
    private Long balanceID;

    @OneToOne
    @JoinColumn(name = "accountID", nullable = false, unique = true)
    private Account account;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount = BigDecimal.valueOf(100);

    @Column(name = "indicator", nullable = false)
    private String indicator;

    // Defensive copy for mutable fields

    public Account getAccount() {
        return new Account(account); // Return a copy to avoid exposure
    }

    public void setAccount(Account account) {
        this.account = new Account(account); // Store a copy to avoid exposure
    }

    public Date getDate() {
        return new Date(date.getTime()); // Return a copy to avoid exposure
    }

    public void setDate(Date date) {
        this.date = new Date(date.getTime()); // Store a copy to avoid exposure
    }
}
