package com.sonesh.finance.dto;

public class AiRequest {

    private String question;
    private double income;
    private double expenses;
    private double savings;
    private String topCategory;
    private double topCategoryAmount;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    public double getSavings() {
        return savings;
    }

    public void setSavings(double savings) {
        this.savings = savings;
    }

    public String getTopCategory() {
        return topCategory;
    }

    public void setTopCategory(String topCategory) {
        this.topCategory = topCategory;
    }

    public double getTopCategoryAmount() {
        return topCategoryAmount;
    }

    public void setTopCategoryAmount(double topCategoryAmount) {
        this.topCategoryAmount = topCategoryAmount;
    }
}