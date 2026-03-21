package com.sonesh.finance.dto;

public class CategoryAnalyticsResponse {

    private String category;
    private Double total;

    public CategoryAnalyticsResponse(String category, Double total) {
        this.category = category;
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public Double getTotal() {
        return total;
    }
}