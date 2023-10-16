package org.nutrivoltamin.model.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MacronutrientView {

    private String name;
    private String description;
    private Double activeState;
    private Double inactiveState;
    private List<PairView> functions;
    private List<PairView> sources;
    private List<PairView> types;
    private List<PairView> dietaryConsiderations;

}
