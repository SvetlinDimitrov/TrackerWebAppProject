package org.food.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "custom_foods")
@Getter
@Setter
@NoArgsConstructor

public class CustomFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String name;
    private final String measurement = "grams";
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
