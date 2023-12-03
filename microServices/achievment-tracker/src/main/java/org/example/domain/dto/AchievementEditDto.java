package org.example.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AchievementEditDto {

    private String description;
    private BigDecimal progress;
    private BigDecimal goal;
    private String measurement;

}
