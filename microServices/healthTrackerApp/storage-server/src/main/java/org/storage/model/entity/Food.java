package org.storage.model.entity;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
public class Food {

    private Boolean isCustom;
    private String name;
    @Builder.Default
    private BigDecimal size = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal calories = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal A = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal D = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal E = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal K = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal C = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal B1 = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal B2 = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal B3 = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal B5 = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal B6 = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal B7 = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal B9 = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal B12 = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Calcium = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Phosphorus = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Magnesium = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Sodium = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Potassium = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Chloride = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Iron = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Zinc = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Copper = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Manganese = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Iodine = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Selenium = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Fluoride = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Chromium = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Molybdenum = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Carbohydrates = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Protein = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Fat = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Fiber = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal TransFat = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal SaturatedFat = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal Sugar = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal PolyunsaturatedFat = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal MonounsaturatedFat = BigDecimal.ZERO;

}
