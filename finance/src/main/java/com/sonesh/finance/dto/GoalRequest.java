package com.sonesh.finance.dto;

import java.math.BigDecimal;

public class GoalRequest {

    private String title;
    private BigDecimal targetAmount;
    private BigDecimal savedAmount;
    private String category;

    public GoalRequest() {
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public BigDecimal getSavedAmount() {
        return savedAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public void setSavedAmount(BigDecimal savedAmount) {
        this.savedAmount = savedAmount;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}