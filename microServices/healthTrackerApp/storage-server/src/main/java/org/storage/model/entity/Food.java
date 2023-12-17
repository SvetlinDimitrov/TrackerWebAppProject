package org.storage.model.entity;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Embeddable
public class Food {

    private Long id;
    private String name;
    private String measurement;
    private BigDecimal size;
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
    private BigDecimal Calcium;
    private BigDecimal Phosphorus;
    private BigDecimal Magnesium;
    private BigDecimal Sodium;
    private BigDecimal Potassium;
    private BigDecimal Chloride;
    private BigDecimal Iron;
    private BigDecimal Zinc;
    private BigDecimal Copper;
    private BigDecimal Manganese;
    private BigDecimal Iodine;
    private BigDecimal Selenium;
    private BigDecimal Fluoride;
    private BigDecimal Chromium;
    private BigDecimal Molybdenum;
    private BigDecimal Carbohydrates;
    private BigDecimal Protein;
    private BigDecimal Fat;
    private BigDecimal Fiber;
    private BigDecimal TransFat;
    private BigDecimal SaturatedFat;
    private BigDecimal Sugar;
    private BigDecimal PolyunsaturatedFat;
    private BigDecimal MonounsaturatedFat;

}
