package com.sda.peaceverse.controller;

import com.sda.peaceverse.entity.User;
import com.sda.peaceverse.entity.SavedVerse;
import com.sda.peaceverse.repository.UserRepository;
import com.sda.peaceverse.MoodService;
import com.sda.peaceverse.MoodRequest;
import com.sda.peaceverse.VerseResponse;
import com.sda.peaceverse.repository.SavedVerseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for handling mood analysis requests.
 * Delegates business logic to MoodService.
 */
@RestController
@RequestMapping("/api")
public class MoodController {

    private final MoodService moodService;
    private final UserRepository userRepository;
    private final SavedVerseRepository savedVerseRepository;

    @Autowired
    public MoodController(MoodService moodService,
                          UserRepository userRepository,
                          SavedVerseRepository savedVerseRepository) {
        this.moodService = moodService;
        this.userRepository = userRepository;
        this.savedVerseRepository = savedVerseRepository;
    }

    @PostMapping("/analyzeMood")
    public ResponseEntity<VerseResponse> analyzeMood(@RequestBody MoodRequest request) {
        // MoodService will just generate the verse
        VerseResponse response = moodService.analyzeMood(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/saveVerse")
    public ResponseEntity<?> saveVerse(@RequestBody Map<String, Object> requestData) {
        String username = (String) requestData.get("username");
        if (username == null) {
            return ResponseEntity.badRequest().body("Missing username");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SavedVerse verse = new SavedVerse();
        verse.setUser(user);
        verse.setMood((String) requestData.get("mood"));
        verse.setSentiment((String) requestData.get("sentiment"));
        verse.setArabicVerse((String) requestData.get("arabicVerse"));
        verse.setTranslation((String) requestData.get("translation"));

        SavedVerse saved = savedVerseRepository.save(verse);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/userVerses")
    public ResponseEntity<List<SavedVerse>> getUserVerses(@RequestParam String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SavedVerse> verses = savedVerseRepository.findByUserId(user.getId());
        return ResponseEntity.ok(verses);
    }

}
