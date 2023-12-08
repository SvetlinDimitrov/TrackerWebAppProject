package org.example;

import org.example.domain.entity.AchievementTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<AchievementTracker, Long> {
    List<AchievementTracker> findAllByUserId(Long userId);
    Optional<AchievementTracker> findByIdAndUserId(Long id, Long userId);
    Optional<AchievementTracker> findByNameAndUserId(String name, Long userId);
    Optional<AchievementTracker> findByName(String name);
}
