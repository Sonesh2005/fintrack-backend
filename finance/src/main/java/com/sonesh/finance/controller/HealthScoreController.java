package com.sonesh.finance.controller;

import com.sonesh.finance.dto.HealthScoreResponse;
import com.sonesh.finance.service.HealthScoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health-score")
public class HealthScoreController {

    private final HealthScoreService healthScoreService;

    public HealthScoreController(HealthScoreService healthScoreService) {
        this.healthScoreService = healthScoreService;
    }

    @GetMapping
    public ResponseEntity<HealthScoreResponse> monthlyScore(Authentication authentication,
                                                            @RequestParam int year,
                                                            @RequestParam int month) {
        return ResponseEntity.ok(
                healthScoreService.getMonthlyHealthScore(authentication.getName(), year, month)
        );
    }
}