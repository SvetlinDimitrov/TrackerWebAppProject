package org.example.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.entity.Achievement;

import java.math.BigDecimal;
import java.util.LinkedList;

@Getter
@Setter
@NoArgsConstructor
public class AchievementView {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private BigDecimal progress;
    private BigDecimal goal;
    private String measurement;
    private String startDate;

    private LinkedList<BigDecimal> dailyProgress = new LinkedList<>();
    private LinkedList<BigDecimal> weaklyProgress = new LinkedList<>();
    private LinkedList<BigDecimal> monthlyProgress = new LinkedList<>();
    private LinkedList<BigDecimal> yearlyProgress = new LinkedList<>();

    public AchievementView(Achievement achievement) {
        this.id = achievement.getId();
        this.userId = achievement.getUserId();
        this.name = achievement.getName();
        this.description = achievement.getDescription();
        this.progress = achievement.getProgress();
        this.goal = achievement.getGoal();
        this.measurement = achievement.getMeasurement();
        this.startDate = achievement.getStartDate().toString();

        this.dailyProgress = achievement.getDailyProgress();
        this.weaklyProgress = achievement.getWeaklyProgress();
        this.monthlyProgress = achievement.getMonthlyProgress();
        this.yearlyProgress = achievement.getYearlyProgress();
    }
}
