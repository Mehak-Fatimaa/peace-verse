package com.sda.peaceverse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Service for interacting with the Gemini API.
 * Handles API calls and response parsing.
 */
@Service
public class GeminiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Autowired
    public GeminiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Calls Gemini API to get a Quranic verse reference based on mood.
     * @param moodText The user's mood.
     * @return Map with "surah" and "ayah" keys.
     * @throws RuntimeException if API call or parsing fails.
     */
    public Map<String, Integer> getVerseReference(String moodText) {
        String prompt = "Based on this mood: \"" + moodText + "\", recommend one relevant Quranic verse (Surah number and Ayah number). Return only JSON: {\"surah\": number, \"ayah\": number}.";

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(geminiApiUrl + "?key=" + geminiApiKey, entity, Map.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Gemini API error: " + response.getStatusCode());
        }

        // Parse response
        var candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
        if (candidates == null || candidates.isEmpty()) {
            throw new RuntimeException("Invalid Gemini response");
        }
        var content = (Map<String, Object>) candidates.get(0).get("content");
        var parts = (List<Map<String, Object>>) content.get("parts");
        String jsonContent = (String) parts.get(0).get("text");
        System.out.println("Raw Gemini response: " + jsonContent);
        // Clean markdown
        jsonContent = jsonContent.replaceAll("```json", "").replaceAll("```", "").trim();

        try {
            return objectMapper.readValue(jsonContent, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini response: " + e.getMessage());
        }
    }
}