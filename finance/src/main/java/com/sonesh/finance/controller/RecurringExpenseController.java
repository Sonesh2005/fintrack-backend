package com.sonesh.finance.controller;

import com.sonesh.finance.model.RecurringExpense;
import com.sonesh.finance.service.RecurringExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recurring-expenses")
public class RecurringExpenseController {

    private final RecurringExpenseService recurringExpenseService;

    public RecurringExpenseController(RecurringExpenseService recurringExpenseService) {
        this.recurringExpenseService = recurringExpenseService;
    }

    @PostMapping
    public ResponseEntity<RecurringExpense> create(
            @RequestBody RecurringExpense expense,
            Authentication auth) {

        return ResponseEntity.ok(
                recurringExpenseService.create(auth.getName(), expense)
        );
    }

    @GetMapping
    public ResponseEntity<List<RecurringExpense>> getAll(Authentication auth) {

        return ResponseEntity.ok(
                recurringExpenseService.getAll(auth.getName())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        recurringExpenseService.delete(id);

        return ResponseEntity.ok("Deleted successfully");
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecurringExpense(@PathVariable Long id,
                                                    @RequestBody RecurringExpense updatedExpense) {
        return ResponseEntity.ok(recurringExpenseService.updateRecurringExpense(id, updatedExpense));
    }
}