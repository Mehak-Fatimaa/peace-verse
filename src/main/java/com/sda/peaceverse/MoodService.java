package com.sda.peaceverse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service class for mood analysis business logic.
 * Orchestrates sentiment classification and verse retrieval.
 */
@Service
public class MoodService {

    private final GeminiService geminiService;
    private final QuranService quranService;

    @Autowired
    public MoodService(GeminiService geminiService, QuranService quranService) {
        this.geminiService = geminiService;
        this.quranService = quranService;
    }

    /**
     * Analyzes the user's mood, determines sentiment, fetches verse reference from Gemini,
     * and retrieves Arabic and English verses from Quran API.
     * @param request The mood request.
     * @return VerseResponse with analyzed data.
     */
    public VerseResponse analyzeMood(MoodRequest request) {
        String moodText = request.mood().toLowerCase();

        // Perform basic sentiment analysis
        String sentiment = classifySentiment(moodText);

        // Get verse reference from Gemini API
        Map<String, Integer> verseRef = geminiService.getVerseReference(moodText);
        int surah = verseRef.getOrDefault("surah", 1);
        int ayah = verseRef.getOrDefault("ayah", 1);

        // Fetch Arabic and English verses
        String arabicVerse = quranService.getArabicVerse(surah, ayah);
        String translation = quranService.getEnglishTranslation(surah, ayah);

        return new VerseResponse(moodText, sentiment, String.valueOf(surah), ayah, arabicVerse, translation);
    }

    /**
     * Classifies sentiment based on keywords in the mood text.
     * @param moodText The user's mood input.
     * @return Sentiment string: "positive", "negative", or "neutral".
     */
    private String classifySentiment(String moodText) {
        if (moodText.matches(".*\\b(happy|grateful|joy|good|peace|glad)\\b.*")) {
            return "positive";
        } else if (moodText.matches(".*\\b(sad|anxious|scared|worried|depressed|angry)\\b.*")) {
            return "negative";
        }
        return "neutral";
    }
}