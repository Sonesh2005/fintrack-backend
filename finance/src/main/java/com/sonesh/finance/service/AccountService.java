package com.sonesh.finance.service;

import com.sonesh.finance.model.Account;
import com.sonesh.finance.model.User;
import com.sonesh.finance.repository.AccountRepository;
import com.sonesh.finance.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository,
                          UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<Account> getAccounts(String email) {
        User user = getUser(email);
        return accountRepository.findByUserId(user.getId());
    }

    public Account createAccount(String email, Account account) {
        User user = getUser(email);
        account.setUser(user);
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, String email, Account updated) {
        User user = getUser(email);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        account.setName(updated.getName());
        account.setType(updated.getType());
        account.setBalance(updated.getBalance());

        return accountRepository.save(account);
    }

    public void deleteAccount(Long id, String email) {
        User user = getUser(email);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        accountRepository.delete(account);
    }

    public void transfer(String email, Long fromId, Long toId, Double amount) {

        User user = getUser(email);

        if (fromId == null || toId == null) {
            throw new RuntimeException("Account IDs are required");
        }

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invalid transfer amount");
        }

        if (fromId.equals(toId)) {
            throw new RuntimeException("Source and destination accounts cannot be the same");
        }

        Account from = accountRepository.findById(fromId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        Account to = accountRepository.findById(toId)
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (from.getUser() == null || to.getUser() == null) {
            throw new RuntimeException("Account user mapping missing");
        }

        if (!from.getUser().getId().equals(user.getId()) ||
                !to.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (from.getBalance() == null) {
            throw new RuntimeException("Source account balance is null");
        }

        if (to.getBalance() == null) {
            throw new RuntimeException("Destination account balance is null");
        }

        if (from.getBalance().doubleValue() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        from.setBalance(from.getBalance().subtract(java.math.BigDecimal.valueOf(amount)));
        to.setBalance(to.getBalance().add(java.math.BigDecimal.valueOf(amount)));

        accountRepository.save(from);
        accountRepository.save(to);

    }
}