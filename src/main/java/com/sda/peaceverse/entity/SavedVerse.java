package com.sda.peaceverse.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "saved_verses")
public class SavedVerse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mood;
    private String sentiment;

    @Column(name = "arabic_verse", columnDefinition = "TEXT")
    private String arabicVerse;

    @Column(name = "translation", columnDefinition = "TEXT")
    private String translation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    public String getSentiment() { return sentiment; }
    public void setSentiment(String sentiment) { this.sentiment = sentiment; }

    public String getArabicVerse() { return arabicVerse; }
    public void setArabicVerse(String arabicVerse) { this.arabicVerse = arabicVerse; }

    public String getTranslation() { return translation; }
    public void setTranslation(String translation) { this.translation = translation; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
