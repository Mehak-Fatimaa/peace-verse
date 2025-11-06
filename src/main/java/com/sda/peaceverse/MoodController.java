package com.sda.peaceverse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling mood analysis requests.
 * Delegates business logic to MoodService.
 */
@RestController
@RequestMapping("/api")
public class MoodController {

    private final MoodService moodService;

    @Autowired
    public MoodController(MoodService moodService) {
        this.moodService = moodService;
    }

    /**
     * Endpoint to analyze user mood and return a Quranic verse.
     * @param request The mood input from the user.
     * @return ResponseEntity with verse data or error.
     */
    @PostMapping("/analyzeMood")
    public ResponseEntity<VerseResponse> analyzeMood(@RequestBody MoodRequest request) {
        VerseResponse response = moodService.analyzeMood(request);
        return ResponseEntity.ok(response);
    }
}