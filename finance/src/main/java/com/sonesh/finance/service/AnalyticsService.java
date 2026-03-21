package com.sonesh.finance.service;

import com.sonesh.finance.dto.HeatmapResponse;
import com.sonesh.finance.dto.InsightsResponse;
import com.sonesh.finance.dto.PredictionResponse;
import com.sonesh.finance.dto.RiskResponse;
import com.sonesh.finance.repository.ExpenseRepository;
import com.sonesh.finance.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;

    public AnalyticsService(ExpenseRepository expenseRepository,
                            IncomeRepository incomeRepository) {
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
    }

    public List<HeatmapResponse> getHeatmap(String email, int year, int month) {
        List<Object[]> data = expenseRepository.getDailyExpenseHeatmap(email, year, month);
        List<HeatmapResponse> result = new ArrayList<>();

        if (data == null || data.isEmpty()) {
            return result;
        }

        for (Object[] row : data) {
            if (row == null || row.length < 2) {
                continue;
            }

            String date = row[0] == null ? "" : row[0].toString();
            double amount = row[1] == null ? 0.0 : ((Number) row[1]).doubleValue();

            result.add(new HeatmapResponse(date, amount));
        }

        return result;
    }

    public InsightsResponse getInsights(String email, int year, int month) {
        Double incomeValue = incomeRepository.getMonthlyIncomeTotal(email, year, month);
        Double expenseValue = expenseRepository.getMonthlyExpenseTotal(email, year, month);

        double income = incomeValue == null ? 0.0 : incomeValue;
        double expense = expenseValue == null ? 0.0 : expenseValue;

        double savings = income - expense;

        int savingsRate = (income == 0)
                ? 0
                : (int) Math.round((savings / income) * 100);

        List<String> tips = new ArrayList<>();

        if (income == 0 && expense > 0) {
            tips.add("No income recorded for this month, but you have expenses. Add income entries for better analysis.");
        }

        if (expense == 0 && income > 0) {
            tips.add("Great! No expenses recorded this month. If this is incorrect, add your expenses to track properly.");
        }

        if (income > 0) {
            if (savingsRate >= 50) {
                tips.add("Excellent financial control. Your savings rate is strong.");
            } else if (savingsRate >= 20) {
                tips.add("Good savings. You can still reduce non-essential spending.");
            } else if (savingsRate > 0) {
                tips.add("Savings are low. Try cutting unnecessary expenses.");
            }
        }

        if (expense > income && income > 0) {
            tips.add("Warning: Expenses exceeded income this month. Consider budgeting to avoid overspending.");
        }

        tips.add("Tip: Track expenses daily for better monthly control.");

        return new InsightsResponse(income, expense, savings, savingsRate, tips);
    }

    public RiskResponse calculateRisk(String email, int year, int month) {
        Double incomeValue = incomeRepository.getMonthlyIncomeTotal(email, year, month);
        Double expenseValue = expenseRepository.getMonthlyExpenseTotal(email, year, month);

        double income = incomeValue == null ? 0.0 : incomeValue;
        double expense = expenseValue == null ? 0.0 : expenseValue;

        double ratio = (income == 0) ? 0 : expense / income;

        String risk;
        String message;

        if (income == 0 && expense == 0) {
            risk = "SAFE";
            message = "No income or expense data recorded for this month yet.";
        } else if (ratio < 0.4) {
            risk = "SAFE";
            message = "Excellent financial balance.";
        } else if (ratio < 0.7) {
            risk = "MODERATE";
            message = "Spending is moderate.";
        } else if (ratio < 1) {
            risk = "HIGH";
            message = "Your expenses are getting high.";
        } else {
            risk = "CRITICAL";
            message = "Expenses exceed income!";
        }

        return new RiskResponse(income, expense, ratio, risk, message);
    }

    public PredictionResponse predictExpense(String email, int year, int month) {
        YearMonth current = YearMonth.of(year, month);
        YearMonth previous1 = current.minusMonths(1);
        YearMonth previous2 = current.minusMonths(2);

        Double m1Value = expenseRepository.getMonthlyExpenseTotal(
                email, current.getYear(), current.getMonthValue()
        );
        Double m2Value = expenseRepository.getMonthlyExpenseTotal(
                email, previous1.getYear(), previous1.getMonthValue()
        );
        Double m3Value = expenseRepository.getMonthlyExpenseTotal(
                email, previous2.getYear(), previous2.getMonthValue()
        );

        double m1 = m1Value == null ? 0.0 : m1Value;
        double m2 = m2Value == null ? 0.0 : m2Value;
        double m3 = m3Value == null ? 0.0 : m3Value;

        double predicted = (m1 + m2 + m3) / 3.0;

        String trend;
        String message;

        if (m1 == 0 && m2 == 0 && m3 == 0) {
            trend = "NO_DATA";
            message = "Not enough expense data to predict next month yet.";
        } else if (m1 > m2 && m2 > m3) {
            trend = "INCREASING";
            message = "Your spending trend is increasing. Try controlling expenses.";
        } else if (m1 < m2 && m2 < m3) {
            trend = "DECREASING";
            message = "Great! Your spending trend is decreasing.";
        } else {
            trend = "STABLE";
            message = "Your spending trend is stable.";
        }

        return new PredictionResponse(predicted, trend, message);
    }
}