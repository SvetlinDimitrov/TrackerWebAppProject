package org.example.utils;

import org.example.domain.dto.AchievementEditDto;
import org.example.domain.entity.Achievement;

import java.math.BigDecimal;

public class AchievementUtils {
    public static void updateAchievement(Achievement achievement, AchievementEditDto dto) {

        if(dto.getDescription() != null && !dto.getDescription().isEmpty()) {
            achievement.setDescription(dto.getDescription());
        }
        if(dto.getProgress() != null && dto.getProgress().compareTo(BigDecimal.ZERO) >= 0){
            achievement.setProgress(dto.getProgress());
        }
        if(dto.getGoal() != null && dto.getGoal().compareTo(BigDecimal.ZERO) >= 0){
            achievement.setGoal(dto.getGoal());
        }
        if(dto.getMeasurement() != null && !dto.getMeasurement().isEmpty()){
            achievement.setMeasurement(dto.getMeasurement());
        }

    }
}
