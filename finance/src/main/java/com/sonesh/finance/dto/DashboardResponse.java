package com.sonesh.finance.dto;

public class DashboardResponse {

    private Double totalIncome;
    private Double totalExpense;
    private Double savings;

    public DashboardResponse(Double totalIncome, Double totalExpense) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.savings = totalIncome - totalExpense;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public Double getSavings() {
        return savings;
    }
}