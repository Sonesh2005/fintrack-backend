package com.sonesh.finance.controller;

import com.sonesh.finance.dto.AiRequest;
import com.sonesh.finance.dto.HealthScoreResponse;
import com.sonesh.finance.service.AiService;
import com.sonesh.finance.service.HealthScoreService;
import com.sonesh.finance.dto.PredictionResponse;
import com.sonesh.finance.service.PredictionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;
    private final HealthScoreService healthScoreService;
    private final PredictionService predictionService;
    public AiController(AiService aiService,
                        HealthScoreService healthScoreService,
                        PredictionService predictionService) {

        this.aiService = aiService;
        this.healthScoreService = healthScoreService;
        this.predictionService = predictionService;
    }
    @PostMapping("/chat")
    public String chat(@RequestBody AiRequest request) {
        return aiService.askFinanceAI(request);
    }

    @PostMapping("/health-score")
    public HealthScoreResponse healthScore(
            @RequestBody AiRequest request) {

        return healthScoreService.calculate(request);
    }
    @PostMapping("/predict")
    public PredictionResponse predict(
            @RequestBody AiRequest request) {

        return predictionService.predict(request);
    }
}
