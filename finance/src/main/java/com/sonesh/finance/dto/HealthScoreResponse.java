package com.sonesh.finance.dto;

import java.util.List;

public class HealthScoreResponse {

    private int score;                 // 0 - 100
    private double income;
    private double expense;
    private double savings;
    private int savingsRate;           // %
    private List<String> insights;

    public HealthScoreResponse() {}

    public HealthScoreResponse(int score, double income, double expense,
                               double savings, int savingsRate, List<String> insights) {
        this.score = score;
        this.income = income;
        this.expense = expense;
        this.savings = savings;
        this.savingsRate = savingsRate;
        this.insights = insights;
    }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public double getIncome() { return income; }
    public void setIncome(double income) { this.income = income; }

    public double getExpense() { return expense; }
    public void setExpense(double expense) { this.expense = expense; }

    public double getSavings() { return savings; }
    public void setSavings(double savings) { this.savings = savings; }

    public int getSavingsRate() { return savingsRate; }
    public void setSavingsRate(int savingsRate) { this.savingsRate = savingsRate; }

    public List<String> getInsights() { return insights; }
    public void setInsights(List<String> insights) { this.insights = insights; }
}