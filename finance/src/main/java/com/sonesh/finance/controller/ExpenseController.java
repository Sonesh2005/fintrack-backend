package com.sonesh.finance.controller;

import com.sonesh.finance.model.Expense;
import com.sonesh.finance.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<Expense> add(@RequestBody Expense expense,
                                       Authentication authentication) {
        return ResponseEntity.ok(
                expenseService.addExpenseByEmail(authentication.getName(), expense)
        );
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAll(Authentication authentication) {
        return ResponseEntity.ok(
                expenseService.getAllByEmail(authentication.getName())
        );
    }

    @GetMapping("/total")
    public ResponseEntity<Double> total(Authentication authentication) {
        return ResponseEntity.ok(
                expenseService.getTotalByEmail(authentication.getName())
        );
    }

    @GetMapping("/monthly-total")
    public ResponseEntity<Double> monthlyTotal(Authentication authentication,
                                               @RequestParam int year,
                                               @RequestParam int month) {
        return ResponseEntity.ok(
                expenseService.getMonthlyTotalByEmail(authentication.getName(), year, month)
        );
    }

    @GetMapping("/category")
    public ResponseEntity<List<Expense>> byCategory(Authentication authentication,
                                                    @RequestParam String category) {
        return ResponseEntity.ok(
                expenseService.getByCategoryByEmail(authentication.getName(), category)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        expenseService.delete(id);
        return ResponseEntity.ok("Deleted ✅");
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        return ResponseEntity.ok(expenseService.updateExpense(id, expense));
    }

}
