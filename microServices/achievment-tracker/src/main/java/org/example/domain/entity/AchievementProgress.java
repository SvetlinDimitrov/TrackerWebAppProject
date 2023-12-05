package org.example.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AchievementProgress {

    private LocalDate date;
    private BigDecimal bestProgress;
    private BigDecimal worstProgress;
    private BigDecimal totalProgress;
    
}
