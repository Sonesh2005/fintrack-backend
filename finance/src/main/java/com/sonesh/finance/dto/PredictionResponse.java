package com.sonesh.finance.dto;

public class PredictionResponse {

    private double predictedExpense;
    private String trend;
    private String message;

    public PredictionResponse(double predictedExpense, String trend, String message) {
        this.predictedExpense = predictedExpense;
        this.trend = trend;
        this.message = message;
    }

    public double getPredictedExpense() {
        return predictedExpense;
    }

    public String getTrend() {
        return trend;
    }

    public String getMessage() {
        return message;
    }
}