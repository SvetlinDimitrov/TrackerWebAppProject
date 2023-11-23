package org.macronutrient.model.entity;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MacronutrientType {
    private String name;
    private String description;
    private List<Pair> functions;
    private List<Pair> sources;
    private List<Pair> healthConsiderations;
    private BigDecimal maleLowerBoundIntake;
    private BigDecimal maleHigherBoundIntake;
    private BigDecimal femaleLowerBoundIntake;
    private BigDecimal femaleHigherBoundIntake;
    private String measure;
}
