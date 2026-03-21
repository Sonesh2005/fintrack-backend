package com.sonesh.finance.dto;

public class BudgetAlertResponse {

    private double budget;
    private double spent;
    private int percentage;
    private String status;
    private String message;

    public BudgetAlertResponse(double budget, double spent, int percentage, String status, String message) {
        this.budget = budget;
        this.spent = spent;
        this.percentage = percentage;
        this.status = status;
        this.message = message;
    }

    public double getBudget() {
        return budget;
    }

    public double getSpent() {
        return spent;
    }

    public int getPercentage() {
        return percentage;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}