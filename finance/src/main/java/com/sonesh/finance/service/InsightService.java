package com.sonesh.finance.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InsightService {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    public InsightService(ExpenseService expenseService,
                          IncomeService incomeService) {
        this.expenseService = expenseService;
        this.incomeService = incomeService;
    }

    public List<String> generateInsights(String email) {
        LocalDate now = LocalDate.now();

        double currentExpense = expenseService.getMonthlyTotalByEmail(
                email,
                now.getYear(),
                now.getMonthValue()
        );

        LocalDate lastMonth = now.minusMonths(1);

        double previousExpense = expenseService.getMonthlyTotalByEmail(
                email,
                lastMonth.getYear(),
                lastMonth.getMonthValue()
        );

        double currentIncome = incomeService.getMonthlyTotalByEmail(
                email,
                now.getYear(),
                now.getMonthValue()
        );

        List<String> insights = new ArrayList<>();

        // 1. Expense comparison
        if (previousExpense > 100) { // ignore tiny values
            double expenseChange = ((currentExpense - previousExpense) / previousExpense) * 100;

            // cap extreme values
            if (expenseChange > 200) expenseChange = 200;
            if (expenseChange < -100) expenseChange = -100;

            if (expenseChange > 20) {
                insights.add("⚠️ Expenses increased by " + (int) expenseChange + "% compared to last month.");
            } else if (expenseChange < -10) {
                insights.add("✅ Good job! Expenses reduced by " + (int) Math.abs(expenseChange) + "% compared to last month.");
            } else {
                insights.add("📊 Your expenses are stable compared to last month.");
            }
        } else {
            insights.add("📊 Not enough data from last month to compare expenses.");
        }

        // 2. Spending ratio
        if (currentIncome > 0) {
            double spendingRatio = (currentExpense / currentIncome) * 100;

            if (spendingRatio > 80) {
                insights.add("⚠️ You are spending " + (int) spendingRatio + "% of your income this month.");
            } else if (spendingRatio < 50) {
                insights.add("💰 Great savings habit! You spent only " + (int) spendingRatio + "% of your income.");
            } else {
                insights.add("👍 Your spending is balanced at " + (int) spendingRatio + "% of your income.");
            }
        } else {
            insights.add("ℹ️ No income recorded this month yet.");
        }

        // 3. Savings insight
        double savings = currentIncome - currentExpense;
        if (savings > 0) {
            insights.add("💡 You saved ₹" + (int) savings + " this month — great job building financial stability.");
        } else if (savings < 0) {
            insights.add("🚨 You are overspending by ₹" + (int) Math.abs(savings) + " this month.");
        } else {
            insights.add("ℹ️ Your income and expenses are currently equal this month.");
        }

        // 4. End-of-month projection
        int currentDay = now.getDayOfMonth();
        if (currentDay > 0) {
            double dailyAverageExpense = currentExpense / currentDay;
            double projectedExpense = dailyAverageExpense * now.lengthOfMonth();

            insights.add("📈 At this pace, your total monthly expense may reach ₹" + (int) projectedExpense + ".");
        }

        return insights;
    }
}