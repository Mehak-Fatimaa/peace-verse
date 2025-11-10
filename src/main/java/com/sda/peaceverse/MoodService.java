package com.sda.peaceverse;

import com.sda.peaceverse.repository.SavedVerseRepository;
import com.sda.peaceverse.entity.User;
import com.sda.peaceverse.entity.SavedVerse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Service class for mood analysis business logic.
 * Orchestrates sentiment classification and verse retrieval.
 */
@Service
@Transactional
public class MoodService {

    private final GeminiService geminiService;
    private final QuranService quranService;
    private final SavedVerseRepository savedVerseRepository;

    @Autowired
    public MoodService(GeminiService geminiService, QuranService quranService, SavedVerseRepository savedVerseRepository) {
        this.geminiService = geminiService;
        this.quranService = quranService;
        this.savedVerseRepository = savedVerseRepository;
    }

    /**
     * Analyzes the user's mood, determines sentiment, fetches verse reference from Gemini,
     * and retrieves Arabic and English verses from Quran API.
     * @param request The mood request.
     * @return VerseResponse with analyzed data.
     */
    public VerseResponse analyzeMood(MoodRequest request) {
        String moodText = request.mood().toLowerCase();
        String sentiment = classifySentiment(moodText);

        int surah = 1, ayah = 1;
        String arabicVerse = "اَللّٰهُ أَكْبَرُ";
        String translation = "Allah is Greatest";

        try {
            Map<String, Integer> verseRef = geminiService.getVerseReference(moodText);
            surah = verseRef.getOrDefault("surah", 1);
            ayah = verseRef.getOrDefault("ayah", 1);

            arabicVerse = quranService.getArabicVerse(surah, ayah);
            translation = quranService.getEnglishTranslation(surah, ayah);
        } catch(Exception e) {
            System.err.println("Verse fetch failed: " + e.getMessage());
        }

        return new VerseResponse(moodText, sentiment, String.valueOf(surah), ayah, arabicVerse, translation);
    }
//    public VerseResponse analyzeMood(MoodRequest request) {
//        // Mock verse for testing
//        return new VerseResponse(
//                request.mood(),
//                "positive",
//                "1",
//                1,
//                "اَللّٰهُ أَكْبَرُ",
//                "Allah is Greatest"
//        );
//    }

    /**
     * Classifies sentiment based on keywords in the mood text.
     * @param moodText The user's mood input.
     * @return Sentiment string: "positive", "negative", or "neutral".
     */
    private String classifySentiment(String moodText) {
        moodText = moodText.toLowerCase(); // make sure it's case-insensitive

        if (moodText.matches(".*\\b(happy|grateful|joy|good|peace|glad|excited|hopeful|relaxed|content|blessed|thankful|calm|motivated|love|cheerful|amazing|smiling|positive|peaceful|energetic|inspired)\\b.*")) {
            return "positive";
        } else if (moodText.matches(".*\\b(sad|tired|exhausted|anxious|scared|worried|depressed|angry|lonely|hopeless|stressed|broken|upset|crying|frustrated|hurt|lost|empty|disappointed|unloved|guilty|fearful|nervous)\\b.*")) {
            return "negative";
        }
        return "neutral";
    }
}