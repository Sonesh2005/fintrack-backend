package com.sonesh.finance.service;

import com.sonesh.finance.dto.DashboardResponse;
import com.sonesh.finance.dto.MonthlyAnalyticsResponse;
import org.springframework.stereotype.Service;
import com.sonesh.finance.dto.CategoryAnalyticsResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    public DashboardService(IncomeService incomeService,
                            ExpenseService expenseService) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
    }

    // Dashboard summary
    public DashboardResponse getDashboard(String email) {

        Double income = incomeService.getTotalByEmail(email);
        Double expense = expenseService.getTotalByEmail(email);

        return new DashboardResponse(income, expense);
    }

    // Monthly analytics for charts
    public List<MonthlyAnalyticsResponse> getMonthlyAnalytics(String email, int year) {

        List<MonthlyAnalyticsResponse> list = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {

            Double income = incomeService.getMonthlyTotalByEmail(email, year, month);
            Double expense = expenseService.getMonthlyTotalByEmail(email, year, month);

            list.add(new MonthlyAnalyticsResponse(month, income, expense));
        }

        return list;
    }
    public List<CategoryAnalyticsResponse> getCategoryExpense(String email) {
        return expenseService.getCategoryAnalyticsByEmail(email);
    }
}