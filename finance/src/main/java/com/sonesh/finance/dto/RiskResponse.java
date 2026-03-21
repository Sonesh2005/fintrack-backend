package com.sonesh.finance.dto;

public class RiskResponse {

    private double income;
    private double expense;
    private double ratio;
    private String riskLevel;
    private String message;

    public RiskResponse(double income, double expense, double ratio,
                        String riskLevel, String message) {
        this.income = income;
        this.expense = expense;
        this.ratio = ratio;
        this.riskLevel = riskLevel;
        this.message = message;
    }

    public double getIncome() {
        return income;
    }

    public double getExpense() {
        return expense;
    }

    public double getRatio() {
        return ratio;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public String getMessage() {
        return message;
    }
}