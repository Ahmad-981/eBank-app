package com.practice.project06.transaction;

import com.practice.project06.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "transactions")
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionID")
    private Long transactionID;

    @ManyToOne
    @JoinColumn(name = "fromAccount", nullable = false)
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "toAccount", nullable = false)
    private Account toAccount;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "indicator", nullable = false)
    private String indicator;

    public Transaction() {
        this.date = new Date();
    }

    public Transaction(Account fromAccount, Account toAccount, BigDecimal amount) {
        this();
        this.fromAccount = new Account(fromAccount);
        this.toAccount = new Account(toAccount);
        this.amount = amount;
        this.indicator = "DB";
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    public void setDate(Date date) {
        this.date = new Date(date.getTime());
    }

    public Account getFromAccount() {
        return new Account(fromAccount);
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = new Account(fromAccount);
    }

    public Account getToAccount() {
        return new Account(toAccount);
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = new Account(toAccount);
    }
}