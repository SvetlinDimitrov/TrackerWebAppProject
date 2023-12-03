package org.example.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.entity.Achievement;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AchievementCreateDto {

    @NotEmpty
    @NotBlank
    @Length(min = 3)
    private String name;
    @NotEmpty
    @NotBlank
    @Length(min = 3)
    private String description;
    @NotNull
    @Min(0)
    private BigDecimal goal;
    private String measurement;

    public Achievement toEntity() {
        Achievement achievement = new Achievement();
        achievement.setName(name);
        achievement.setDescription(description);
        achievement.setGoal(goal);
        achievement.setMeasurement(measurement);
        return achievement;
    }
}
