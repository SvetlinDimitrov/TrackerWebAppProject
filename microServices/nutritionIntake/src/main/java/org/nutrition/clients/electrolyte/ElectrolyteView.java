package org.nutrition.clients.electrolyte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nutrition.clients.vitamin.PairView;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
