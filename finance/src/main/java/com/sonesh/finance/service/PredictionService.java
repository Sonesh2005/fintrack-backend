package com.sonesh.finance.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonesh.finance.dto.AiRequest;
import com.sonesh.finance.dto.PredictionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class PredictionService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.create();

    public PredictionResponse predict(AiRequest request) {

        String prompt = """
                You are an AI financial forecasting assistant.

                Analyze the user's financial data and predict:

                - next month expense
                - spending trend
                - future financial risk
                - possible savings outcome

                Financial Data:

                Income: %s
                Expenses: %s
                Savings: %s
                Highest Spending Category: %s
                Highest Category Amount: %s

                Give concise and professional prediction.
                """.formatted(
                request.getIncome(),
                request.getExpenses(),
                request.getSavings(),
                request.getTopCategory(),
                request.getTopCategoryAmount()
        );

        Map<String, Object> body = Map.of(
                "model", "deepseek/deepseek-chat",
                "messages", new Object[]{
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                }
        );

        try {

            String response = webClient.post()
                    .uri("https://openrouter.ai/api/v1/chat/completions")
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            "Bearer " + apiKey
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(response);

            String aiPrediction =
                    root.get("choices")
                            .get(0)
                            .get("message")
                            .get("content")
                            .asText()
                            .replace("###", "")
                            .replace("**", "")
                            .replace("\\n", "\n");

            double predictedExpense =
                    request.getExpenses() * 1.08;

            String trend =
                    predictedExpense > request.getExpenses()
                            ? "Increasing"
                            : "Stable";

            return new PredictionResponse(
                    predictedExpense,
                    trend,
                    aiPrediction
            );

        } catch (Exception e) {

            e.printStackTrace();

            return new PredictionResponse(
                    0,
                    "Unavailable",
                    "Prediction service unavailable."
            );
        }
    }
}