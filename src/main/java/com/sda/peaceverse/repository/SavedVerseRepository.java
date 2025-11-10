package com.sda.peaceverse.repository;

import com.sda.peaceverse.entity.SavedVerse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SavedVerseRepository extends JpaRepository<SavedVerse, Long> {
    List<SavedVerse> findByUserId(Long userId);
}
