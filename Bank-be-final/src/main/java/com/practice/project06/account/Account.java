package com.practice.project06.account;

import com.practice.project06.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountID")
    private Long accountID;

    @OneToOne
    @JoinColumn(name = "userID", nullable = false, unique = true)
    private User user;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "account_type", nullable = false)
    private String accountType;

    public Account() {}

    public Account(Account other) {
        if (other != null) {
            this.accountID = other.accountID;
            this.accountNumber = other.accountNumber;
            this.accountType = other.accountType;
            this.user = (other.user != null) ? new User(other.user) : null;
        }
    }

    public User getUser() {
        return user != null ? new User(user) : null;
    }

    public void setUser(User user) {
        this.user = user != null ? new User(user) : null;
    }
}
