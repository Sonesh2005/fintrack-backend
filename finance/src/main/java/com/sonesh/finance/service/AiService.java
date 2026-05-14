package com.sonesh.finance.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonesh.finance.dto.AiRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class AiService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.create();

    public String askFinanceAI(AiRequest request) {

        String prompt = """
You are FinTrack AI, a smart financial assistant.

Rules:
- Give SHORT and clean answers
- No markdown
- No ### symbols
- No ** symbols
- No bullet overload
- Keep response professional and easy to read
- Maximum 8-10 lines
- Only answer finance-related questions
- If user says "hi" or "hello", greet normally
- If financial data is empty or zero, do not invent numbers
- Politely tell user to add income and expenses first
- always the amount should be in rupees only
-Always start sentences properly.
-Never omit the first letter of any word.
-Never use markdown symbols.
-Reply in clean readable plain text.

Financial Data:
Income: %s
Expenses: %s
Savings: %s
Highest Spending Category: %s
Highest Category Amount: %s

User Question:
%s
""".formatted(
                request.getIncome(),
                request.getExpenses(),
                request.getSavings(),
                request.getTopCategory(),
                request.getTopCategoryAmount(),
                request.getQuestion()
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
                    .header(HttpHeaders.AUTHORIZATION,
                            "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(response);

            return root
                    .get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText();

        } catch (Exception e) {

            e.printStackTrace();

            return "AI service temporarily unavailable.";
        }
    }
}