package org.storage.model.entity;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
public class Food {

    private String name;
    private BigDecimal size = BigDecimal.ZERO;
    private BigDecimal calories = BigDecimal.ZERO;
    private BigDecimal A = BigDecimal.ZERO;
    private BigDecimal D = BigDecimal.ZERO;
    private BigDecimal E = BigDecimal.ZERO;
    private BigDecimal K = BigDecimal.ZERO;
    private BigDecimal C = BigDecimal.ZERO;
    private BigDecimal B1 = BigDecimal.ZERO;
    private BigDecimal B2 = BigDecimal.ZERO;
    private BigDecimal B3 = BigDecimal.ZERO;
    private BigDecimal B5 = BigDecimal.ZERO;
    private BigDecimal B6 = BigDecimal.ZERO;
    private BigDecimal B7 = BigDecimal.ZERO;
    private BigDecimal B9 = BigDecimal.ZERO;
    private BigDecimal B12 = BigDecimal.ZERO;
    private BigDecimal Calcium = BigDecimal.ZERO;
    private BigDecimal Phosphorus = BigDecimal.ZERO;
    private BigDecimal Magnesium = BigDecimal.ZERO;
    private BigDecimal Sodium = BigDecimal.ZERO;
    private BigDecimal Potassium = BigDecimal.ZERO;
    private BigDecimal Chloride = BigDecimal.ZERO;
    private BigDecimal Iron = BigDecimal.ZERO;
    private BigDecimal Zinc = BigDecimal.ZERO;
    private BigDecimal Copper = BigDecimal.ZERO;
    private BigDecimal Manganese = BigDecimal.ZERO;
    private BigDecimal Iodine = BigDecimal.ZERO;
    private BigDecimal Selenium = BigDecimal.ZERO;
    private BigDecimal Fluoride = BigDecimal.ZERO;
    private BigDecimal Chromium = BigDecimal.ZERO;
    private BigDecimal Molybdenum = BigDecimal.ZERO;
    private BigDecimal Carbohydrates = BigDecimal.ZERO;
    private BigDecimal Protein = BigDecimal.ZERO;
    private BigDecimal Fat = BigDecimal.ZERO;
    private BigDecimal Fiber = BigDecimal.ZERO;
    private BigDecimal TransFat = BigDecimal.ZERO;
    private BigDecimal SaturatedFat = BigDecimal.ZERO;
    private BigDecimal Sugar = BigDecimal.ZERO;
    private BigDecimal PolyunsaturatedFat = BigDecimal.ZERO;
    private BigDecimal MonounsaturatedFat = BigDecimal.ZERO;

}
