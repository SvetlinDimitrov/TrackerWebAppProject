package org.example.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AchievementTracker {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private Long userId;
        private String name;
        private String description;
        private BigDecimal goal;
        private String measurement;
        private LocalDate startDate;

        @ElementCollection
        private SortedMap<LocalDate, Achievement> dailyProgress = new TreeMap<>();
        @ElementCollection
        private SortedMap<YearMonth, AchievementProgress> monthlyProgress = new TreeMap<>();
        @ElementCollection
        private SortedMap<Integer, AchievementProgress> yearlyProgresses = new TreeMap<>();
}
