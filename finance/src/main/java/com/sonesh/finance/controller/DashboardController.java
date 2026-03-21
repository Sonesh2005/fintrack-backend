package com.sonesh.finance.controller;

import com.sonesh.finance.dto.CategoryAnalyticsResponse;
import com.sonesh.finance.dto.DashboardResponse;
import com.sonesh.finance.dto.MonthlyAnalyticsResponse;
import com.sonesh.finance.service.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardResponse getDashboard(Authentication authentication) {
        return dashboardService.getDashboard(authentication.getName());
    }

    @GetMapping("/monthly")
    public List<MonthlyAnalyticsResponse> monthlyAnalytics(Authentication authentication,
                                                           @RequestParam int year) {
        return dashboardService.getMonthlyAnalytics(authentication.getName(), year);
    }

    @GetMapping("/category-expense")
    public List<CategoryAnalyticsResponse> categoryExpense(Authentication authentication) {
        return dashboardService.getCategoryExpense(authentication.getName());
    }
}