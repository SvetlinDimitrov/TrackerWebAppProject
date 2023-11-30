package org.food.domain.dtos;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Getter
public class CreateCustomFood {
    @NotBlank
    @NotEmpty
    @Length(min = 3, max = 50)
    private String name;
    @Min(0)
    @NotNull
    private BigDecimal size;
    @Min(0)
    @NotNull
    private BigDecimal calories;
    @Min(0)
    @NotNull
    private BigDecimal A;
    @Min(0)
    @NotNull
    private BigDecimal D;
    @Min(0)
    @NotNull
    private BigDecimal E;
    @Min(0)
    @NotNull
    private BigDecimal K;
    @Min(0)
    @NotNull
    private BigDecimal C;
    @Min(0)
    @NotNull
    private BigDecimal B1;
    @Min(0)
    @NotNull
    private BigDecimal B2;
    @Min(0)
    @NotNull
    private BigDecimal B3;
    @Min(0)
    @NotNull
    private BigDecimal B5;
    @Min(0)
    @NotNull
    private BigDecimal B6;
    @Min(0)
    @NotNull
    private BigDecimal B7;
    @Min(0)
    @NotNull
    private BigDecimal B9;
    @Min(0)
    @NotNull
    private BigDecimal B12;
    @Min(0)
    @NotNull
    private BigDecimal Calcium;
    @Min(0)
    @NotNull
    private BigDecimal Phosphorus;
    @Min(0)
    @NotNull
    private BigDecimal Magnesium;
    @Min(0)
    @NotNull
    private BigDecimal Sodium;
    @Min(0)
    @NotNull
    private BigDecimal Potassium;
    @Min(0)
    @NotNull
    private BigDecimal Chloride;
    @Min(0)
    @NotNull
    private BigDecimal Iron;
    @Min(0)
    @NotNull
    private BigDecimal Zinc;
    @Min(0)
    @NotNull
    private BigDecimal Copper;
    @Min(0)
    @NotNull
    private BigDecimal Manganese;
    @Min(0)
    @NotNull
    private BigDecimal Iodine;
    @Min(0)
    @NotNull
    private BigDecimal Selenium;
    @Min(0)
    @NotNull
    private BigDecimal Fluoride;
    @Min(0)
    @NotNull
    private BigDecimal Chromium;
    @Min(0)
    @NotNull
    private BigDecimal Molybdenum;
    @Min(0)
    @NotNull
    private BigDecimal Carbohydrates;
    @Min(0)
    @NotNull
    private BigDecimal Protein;
    @Min(0)
    @NotNull
    private BigDecimal Fat;
    @Min(0)
    @NotNull
    private BigDecimal Fiber;
    @Min(0)
    @NotNull
    private BigDecimal TransFat;
    @Min(0)
    @NotNull
    private BigDecimal SaturatedFat;
    @Min(0)
    @NotNull
    private BigDecimal Sugar;
    @Min(0)
    @NotNull
    private BigDecimal PolyunsaturatedFat;
    @Min(0)
    @NotNull
    private BigDecimal MonounsaturatedFat;

}
