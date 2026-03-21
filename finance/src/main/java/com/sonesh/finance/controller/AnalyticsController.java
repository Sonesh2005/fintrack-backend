package com.sonesh.finance.controller;

import com.sonesh.finance.dto.HeatmapResponse;
import com.sonesh.finance.dto.InsightsResponse;
import com.sonesh.finance.dto.PredictionResponse;
import com.sonesh.finance.dto.RiskResponse;
import com.sonesh.finance.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/heatmap")
    public ResponseEntity<List<HeatmapResponse>> heatmap(
            Authentication auth,
            @RequestParam int year,
            @RequestParam int month) {

        return ResponseEntity.ok(
                analyticsService.getHeatmap(auth.getName(), year, month)
        );
    }

    @GetMapping("/insights")
    public ResponseEntity<InsightsResponse> insights(
            Authentication auth,
            @RequestParam int year,
            @RequestParam int month) {

        return ResponseEntity.ok(
                analyticsService.getInsights(auth.getName(), year, month)
        );
    }
    @GetMapping("/risk")
    public ResponseEntity<RiskResponse> risk(
            Authentication auth,
            @RequestParam int year,
            @RequestParam int month) {

        return ResponseEntity.ok(
                analyticsService.calculateRisk(auth.getName(), year, month)
        );
    }
    @GetMapping("/predict-expense")
    public ResponseEntity<PredictionResponse> predictExpense(
            Authentication auth,
            @RequestParam int year,
            @RequestParam int month) {

        return ResponseEntity.ok(
                analyticsService.predictExpense(auth.getName(), year, month)
        );
    }
}