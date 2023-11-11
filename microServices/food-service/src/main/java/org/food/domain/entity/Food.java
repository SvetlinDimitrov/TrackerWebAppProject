package org.food.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Food {

    private String name;
    private BigDecimal size;
    private String measurement;
    private BigDecimal calories;
    private BigDecimal A;
    private BigDecimal D;
    private BigDecimal E;
    private BigDecimal K;
    private BigDecimal C;
    private BigDecimal B1;
    private BigDecimal B2;
    private BigDecimal B3;
    private BigDecimal B5;
    private BigDecimal B6;
    private BigDecimal B7;
    private BigDecimal B9;
    private BigDecimal B12;
    private BigDecimal Sodium;
    private BigDecimal Potassium;
    private BigDecimal Calcium;
    private BigDecimal Magnesium;
    private BigDecimal Chloride;
    private BigDecimal Carbohydrates;
    private BigDecimal Protein;
    private BigDecimal Fat;

}
