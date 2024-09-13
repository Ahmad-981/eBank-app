package com.practice.project06.account;


import com.practice.project06.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {
    private User user;
    private String accountNumber;
    private String accountType;

    public AccountDTO() {
    }

    public AccountDTO(AccountDTO accountDTO) {
        if (accountDTO != null) {
            this.user = (accountDTO.user != null) ? new User(accountDTO.user) : null;
            this.accountNumber = accountDTO.accountNumber;
            this.accountType = accountDTO.accountType;
        }
    }

    public User getUser() {
        return user != null ? new User(user) : null;
    }

    public void setUser(User user) {
        this.user = user != null ? new User(user) : null;
    }
}
