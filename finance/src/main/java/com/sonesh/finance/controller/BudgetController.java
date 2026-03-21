package com.sonesh.finance.controller;

import com.sonesh.finance.dto.BudgetAlertResponse;
import com.sonesh.finance.model.Budget;
import com.sonesh.finance.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<Budget> saveBudget(@RequestBody Map<String, Double> body,
                                             Authentication auth) {
        Double monthlyBudget = body.get("monthlyBudget");
        return ResponseEntity.ok(
                budgetService.saveBudget(auth.getName(), monthlyBudget)
        );
    }

    @GetMapping
    public ResponseEntity<Double> getBudget(Authentication auth) {
        return ResponseEntity.ok(
                budgetService.getMonthlyBudget(auth.getName())
        );
    }

    @GetMapping("/alerts")
    public ResponseEntity<BudgetAlertResponse> getAlert(Authentication auth) {
        return ResponseEntity.ok(
                budgetService.getBudgetAlert(auth.getName())
        );
    }
}