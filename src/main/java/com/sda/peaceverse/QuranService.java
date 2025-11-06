package com.sda.peaceverse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Service for interacting with the Quran API.
 * Fetches Arabic verses and English translations.
 */
@Service
public class QuranService {

    private final RestTemplate restTemplate;

    @Value("${quran.api.arabic.url}")
    private String arabicUrlTemplate;

    @Value("${quran.api.translation.url}")
    private String translationUrlTemplate;

    @Autowired
    public QuranService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches the Arabic verse for the given Surah and Ayah.
     * @param surah Surah number.
     * @param ayah Ayah number.
     * @return Arabic verse text.
     * @throws RuntimeException if API call fails.
     */
    public String getArabicVerse(int surah, int ayah) {
        String url = String.format(arabicUrlTemplate, surah, ayah);
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response == null || !"OK".equals(response.get("status"))) {
            throw new RuntimeException("Failed to fetch Arabic verse");
        }
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        return (String) data.get("text");
    }

    /**
     * Fetches the English translation for the given Surah and Ayah.
     * @param surah Surah number.
     * @param ayah Ayah number.
     * @return English translation text.
     * @throws RuntimeException if API call fails.
     */
    public String getEnglishTranslation(int surah, int ayah) {
        String url = String.format(translationUrlTemplate, surah, ayah);
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response == null || !"OK".equals(response.get("status"))) {
            throw new RuntimeException("Failed to fetch English translation");
        }
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        return (String) data.get("text");
    }
}