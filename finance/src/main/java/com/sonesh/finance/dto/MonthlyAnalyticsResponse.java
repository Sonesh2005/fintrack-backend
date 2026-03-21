package com.sonesh.finance.dto;

public class MonthlyAnalyticsResponse {

    private int month;
    private Double income;
    private Double expense;

    public MonthlyAnalyticsResponse(int month, Double income, Double expense) {
        this.month = month;
        this.income = income;
        this.expense = expense;
    }

    public int getMonth() {
        return month;
    }

    public Double getIncome() {
        return income;
    }

    public Double getExpense() {
        return expense;
    }
}