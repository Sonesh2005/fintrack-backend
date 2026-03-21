package com.sonesh.finance.controller;
import com.sonesh.finance.dto.AccountTransferRequest;
import com.sonesh.finance.model.Account;
import com.sonesh.finance.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAccounts(Authentication auth) {
        return ResponseEntity.ok(
                accountService.getAccounts(auth.getName())
        );
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(
            @RequestBody Account account,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                accountService.createAccount(auth.getName(), account)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable Long id,
            @RequestBody Account account,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                accountService.updateAccount(id, auth.getName(), account)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(
            @PathVariable Long id,
            Authentication auth
    ) {
        accountService.deleteAccount(id, auth.getName());
        return ResponseEntity.ok("Account deleted");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(
            @RequestBody AccountTransferRequest request,
            Authentication auth
    ) {

        accountService.transfer(
                auth.getName(),
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount()
        );

        return ResponseEntity.ok("Transfer successful");
    }
}