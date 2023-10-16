package org.nutrivoltamin.model.dtos;

import lombok.Builder;
import lombok.Data;
import org.nutrivoltamin.model.entity.Electrolyte;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ElectrolyteView {

    private String name;
    private String description;
    private List<PairView> functions;
    private List<PairView> sources;
    private List<PairView> healthConsiderations;
    private BigDecimal maleLowerBoundIntake;
    private BigDecimal maleHigherBoundIntake;
    private BigDecimal femaleLowerBoundIntake;
    private BigDecimal femaleHigherBoundIntake;
    private String measure;

}
