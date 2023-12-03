package org.example;

import org.example.domain.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    List<Achievement> findAllByUserId(Long userId);
    Optional<Achievement> findByIdAndUserId(Long id, Long userId);
    Optional<Achievement> findByName(String name);
}
