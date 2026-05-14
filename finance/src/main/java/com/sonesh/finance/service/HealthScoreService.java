package com.sonesh.finance.service;

import com.sonesh.finance.dto.AiRequest;
import com.sonesh.finance.dto.HealthScoreResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HealthScoreService {

    public HealthScoreResponse calculate(AiRequest request) {

        double income = request.getIncome();
        double expense = request.getExpenses();
        double savings = request.getSavings();

        int savingsRate = (int) ((savings / income) * 100);

        int score;

        if (savingsRate >= 30) {
            score = 90;
        } else if (savingsRate >= 20) {
            score = 75;
        } else if (savingsRate >= 10) {
            score = 60;
        } else {
            score = 40;
        }

        List<String> insights = new ArrayList<>();

        if (savingsRate >= 30) {
            insights.add("Excellent savings habit.");
        } else {
            insights.add("Try increasing monthly savings.");
        }

        if (expense > income * 0.8) {
            insights.add("Expenses are very high compared to income.");
        }

        if ("Shopping".equalsIgnoreCase(request.getTopCategory())) {
            insights.add("Shopping expenses are affecting your savings.");
        }

        if (score >= 80) {
            insights.add("Financial health is strong.");
        } else {
            insights.add("Focus on budgeting and expense tracking.");
        }

        return new HealthScoreResponse(
                score,
                income,
                expense,
                savings,
                savingsRate,
                insights
        );
    }
    public HealthScoreResponse getMonthlyHealthScore(
            String email,
            int year,
            int month
    ) {

        return new HealthScoreResponse(
                30,
                0,
                0,
                0,
                0,
                List.of("No financial data available yet.")
        );
    }
}