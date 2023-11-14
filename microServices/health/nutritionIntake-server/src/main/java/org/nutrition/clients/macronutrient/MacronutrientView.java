package org.nutrition.clients.macronutrient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nutrition.clients.vitamin.PairView;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
