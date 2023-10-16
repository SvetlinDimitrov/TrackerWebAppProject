package org.nutrivoltamin.model.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Macronutrient {

    private String name;
    private String description;
    private Double activeState ;
    private Double inactiveState ;
    private List<Pair> functions;
    private List<Pair> sources;
    private List<Pair> types;
    private List<Pair> dietaryConsiderations;

}
