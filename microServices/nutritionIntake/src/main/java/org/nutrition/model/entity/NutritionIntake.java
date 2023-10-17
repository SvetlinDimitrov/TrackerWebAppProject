package org.nutrition.model.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NutritionIntake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nutrientName;
    private String nutrientType;
    private BigDecimal dailyConsumed = BigDecimal.ZERO;
    private BigDecimal lowerBoundIntake;
    private BigDecimal upperBoundIntake;
    private String measurement;
    private Long recordId;
}
