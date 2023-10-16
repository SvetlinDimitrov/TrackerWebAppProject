package org.nutrivoltamin.model.dtos;

import lombok.Builder;
import lombok.Data;
import org.nutrivoltamin.model.entity.Vitamin;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class VitaminView {

    private String name;
    private String description;
    private List<PairView> functions;
    private List<PairView> sources;
    private BigDecimal maleLowerBoundIntake;
    private BigDecimal maleHigherBoundIntake;
    private BigDecimal femaleLowerBoundIntake;
    private BigDecimal femaleHigherBoundIntake;
    private String measure;

}
