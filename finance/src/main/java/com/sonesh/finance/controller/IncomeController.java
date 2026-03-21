package com.sonesh.finance.controller;

import com.sonesh.finance.model.Income;
import com.sonesh.finance.service.IncomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    public ResponseEntity<Income> add(@RequestBody Income income,
                                      Authentication authentication) {
        return ResponseEntity.ok(
                incomeService.addIncomeByEmail(authentication.getName(), income)
        );
    }

    @GetMapping
    public ResponseEntity<List<Income>> getAll(Authentication authentication) {
        return ResponseEntity.ok(
                incomeService.getAllByEmail(authentication.getName())
        );
    }

    @GetMapping("/total")
    public ResponseEntity<Double> total(Authentication authentication) {
        return ResponseEntity.ok(
                incomeService.getTotalByEmail(authentication.getName())
        );
    }

    @GetMapping("/monthly-total")
    public ResponseEntity<Double> monthlyTotal(Authentication authentication,
                                               @RequestParam int year,
                                               @RequestParam int month) {
        return ResponseEntity.ok(
                incomeService.getMonthlyTotalByEmail(authentication.getName(), year, month)
        );
    }

    @GetMapping("/category")
    public ResponseEntity<List<Income>> byCategory(Authentication authentication,
                                                   @RequestParam String category) {
        return ResponseEntity.ok(
                incomeService.getByCategoryByEmail(authentication.getName(), category)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Income> update(@PathVariable Long id,
                                         @RequestBody Income income,
                                         Authentication authentication) {
        return ResponseEntity.ok(
                incomeService.updateIncomeByEmail(authentication.getName(), id, income)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.ok("Income deleted");
    }
}