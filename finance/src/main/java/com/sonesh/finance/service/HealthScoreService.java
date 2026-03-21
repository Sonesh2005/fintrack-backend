package com.sonesh.finance.service;

import com.sonesh.finance.dto.HealthScoreResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HealthScoreService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    public HealthScoreService(IncomeService incomeService, ExpenseService expenseService) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
    }

    public HealthScoreResponse getMonthlyHealthScore(String email, int year, int month) {

        double income = safe(incomeService.getMonthlyTotalByEmail(email, year, month));
        double expense = safe(expenseService.getMonthlyTotalByEmail(email, year, month));

        double savings = income - expense;
        int savingsRate = (income <= 0) ? 0 : (int) Math.round((savings / income) * 100);

        // ✅ score logic (simple + looks professional)
        int score = 0;
        List<String> insights = new ArrayList<>();

        // Rule 1: income available
        if (income <= 0) {
            insights.add("No income recorded for this month. Add income to get accurate score.");
            score = 20; // minimum
            return new HealthScoreResponse(score, income, expense, savings, savingsRate, insights);
        }

        // Rule 2: savings rate (biggest factor)
        // ideal savingsRate: 20% to 40%+
        if (savingsRate >= 40) score += 50;
        else if (savingsRate >= 25) score += 40;
        else if (savingsRate >= 10) score += 30;
        else if (savingsRate >= 0) score += 20;
        else score += 5; // negative savings

        // Rule 3: expense control
        double expenseRatio = expense / income; // 0.0 - 2.0+
        if (expenseRatio <= 0.6) score += 35;
        else if (expenseRatio <= 0.8) score += 25;
        else if (expenseRatio <= 1.0) score += 15;
        else score += 0;

        // Rule 4: positive savings bonus / negative penalty
        if (savings >= 0) score += 15;
        else score -= 10;

        // Clamp 0-100
        score = Math.max(0, Math.min(100, score));

        // Insights
        if (savingsRate < 10) insights.add("Try to save at least 10% of your income.");
        if (expenseRatio > 0.8) insights.add("Your expenses are high compared to income. Reduce non-essential spending.");
        if (savings < 0) insights.add("You spent more than your income. Consider setting a strict monthly budget.");
        if (score >= 80) insights.add("Excellent financial control. Keep maintaining this discipline!");
        else if (score >= 60) insights.add("Good. A little more savings can improve your score.");
        else insights.add("Needs improvement. Track expenses category-wise and cut down unnecessary spending.");

        return new HealthScoreResponse(score, income, expense, savings, savingsRate, insights);
    }

    private double safe(Double v) {
        return v == null ? 0.0 : v;
    }
}