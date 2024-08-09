package com.practice.project06.account;

import com.practice.project06.balance.BalanceController;
import com.practice.project06.balance.BalanceRepository;
import com.practice.project06.transaction.TransactionRepository;
import com.practice.project06.user.User;
import com.practice.project06.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private BalanceController balanceController;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    private String generateRandomString(int length) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int digit = secureRandom.nextInt(10);
            sb.append(digit);
        }

        return sb.toString();
    }

    public Map<String, Object> createAccount(AccountDTO accountDTO) {
        User user = accountDTO.getUser();

        if (user == null || user.getUserID() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        User existingUser = userRepository.findById(user.getUserID())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setAccountType(accountDTO.getAccountType());
        account.setUser(existingUser);
        account.setAccountNumber(generateRandomString(8));

        Account savedAccount = accountRepository.save(account);
        Map<String, Object> response = new HashMap<>();
        response.put("accountId", savedAccount.getAccountID());
        response.put("accountNum", savedAccount.getAccountNumber());

        //setting balance
        balanceController.createBalance(savedAccount.getAccountID());
        return response;
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public Optional<Account> updateAccount(Long id, Account updatedAccount) {
        Optional<Account> existingAccountOpt = accountRepository.findById(id);

        if (existingAccountOpt.isPresent()) {
            Account existingAccount = existingAccountOpt.get();

            existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
            existingAccount.setAccountType(updatedAccount.getAccountType());

            User updatedUser = updatedAccount.getUser();
            if (updatedUser != null) {
                User existingUser = existingAccount.getUser();
                if (existingUser != null) {
                    existingUser.setUsername(updatedUser.getUsername());
                    existingUser.setRole(updatedUser.getRole());

                    String email = updatedUser.getEmail();
                    if (!isValidEmail(email)) {
                        throw new IllegalArgumentException("Invalid email format");
                    }

                    existingUser.setEmail(email);
                    existingUser.setAddress(updatedUser.getAddress());
                    userRepository.save(existingUser);
                }
            }
            return Optional.of(accountRepository.save(existingAccount));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public boolean deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        balanceRepository.deleteByAccountId(id);
        transactionRepository.deleteByFromAccountId(id);
        transactionRepository.deleteByToAccountId(id);
        accountRepository.delete(account);
        return true;

    }

}