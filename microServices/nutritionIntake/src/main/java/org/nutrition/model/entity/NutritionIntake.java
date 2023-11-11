package org.nutrition.model.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Builder.Default
    private BigDecimal dailyConsumed = BigDecimal.ZERO;
    private BigDecimal lowerBoundIntake;
    private BigDecimal upperBoundIntake;
    private String measurement;
    private Long recordId;
}
