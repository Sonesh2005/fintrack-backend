package com.sonesh.finance.controller;

import com.sonesh.finance.service.InsightService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/insights")
public class InsightController {

    private final InsightService insightService;

    public InsightController(InsightService insightService) {
        this.insightService = insightService;
    }

    @GetMapping
    public List<String> getInsights(Authentication authentication) {
        String email = authentication.getName();
        return insightService.generateInsights(email);
    }
}