package com.sonesh.finance.dto;

import java.util.List;

public class InsightsResponse {

    private Double income;
    private Double expense;
    private Double savings;
    private Integer savingsRate;
    private List<String> insights;

    public InsightsResponse() {}

    public InsightsResponse(Double income, Double expense, Double savings, Integer savingsRate, List<String> insights) {
        this.income = income;
        this.expense = expense;
        this.savings = savings;
        this.savingsRate = savingsRate;
        this.insights = insights;
    }

    public Double getIncome() { return income; }
    public void setIncome(Double income) { this.income = income; }

    public Double getExpense() { return expense; }
    public void setExpense(Double expense) { this.expense = expense; }

    public Double getSavings() { return savings; }
    public void setSavings(Double savings) { this.savings = savings; }

    public Integer getSavingsRate() { return savingsRate; }
    public void setSavingsRate(Integer savingsRate) { this.savingsRate = savingsRate; }

    public List<String> getInsights() { return insights; }
    public void setInsights(List<String> insights) { this.insights = insights; }
}