package org.example.domain.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.YearMonth;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AchievementProgressMonthlyView {
    
    private YearMonth month;
    private BigDecimal bestProgress;
    private BigDecimal worstProgress;
    private BigDecimal totalProgress;
    
}
