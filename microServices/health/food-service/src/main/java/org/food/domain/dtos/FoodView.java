package org.food.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.food.domain.entity.Food;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodView {

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

    public FoodView(Food entity) {
        this.name = entity.getName();
        this.size = entity.getSize();
        this.measurement = entity.getMeasurement();
        this.calories = entity.getCalories();
        A = entity.getA();
        D = entity.getD();
        E = entity.getE();
        K = entity.getK();
        C = entity.getC();
        B1 = entity.getB1();
        B2 = entity.getB2();
        B3 = entity.getB3();
        B5 = entity.getB5();
        B6 = entity.getB6();
        B7 = entity.getB7();
        B9 = entity.getB9();
        B12 = entity.getB12();
        Sodium = entity.getSodium();
        Potassium = entity.getPotassium();
        Calcium = entity.getCalcium();
        Magnesium = entity.getMagnesium();
        Chloride = entity.getChloride();
        Carbohydrates = entity.getCarbohydrates();
        Protein = entity.getProtein();
        Fat = entity.getFat();
    }
}
