package org.nutrivoltamin.util;

import org.nutrivoltamin.model.dtos.ElectrolyteView;
import org.nutrivoltamin.model.dtos.MacronutrientView;
import org.nutrivoltamin.model.dtos.PairView;
import org.nutrivoltamin.model.dtos.VitaminView;
import org.nutrivoltamin.model.entity.Electrolyte;
import org.nutrivoltamin.model.entity.Macronutrient;
import org.nutrivoltamin.model.entity.Vitamin;

public class Converter {
    static public ElectrolyteView toElectrolyteView(Electrolyte entity) {
        return ElectrolyteView.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .functions(entity.getFunctions().stream().map(PairView::new).toList())
                .sources(entity.getSources().stream().map(PairView::new).toList())
                .healthConsiderations(entity.getHealthConsiderations().stream().map(PairView::new).toList())
                .maleHigherBoundIntake(entity.getMaleHigherBoundIntake())
                .maleLowerBoundIntake(entity.getMaleLowerBoundIntake())
                .femaleHigherBoundIntake(entity.getFemaleHigherBoundIntake())
                .femaleLowerBoundIntake(entity.getFemaleLowerBoundIntake())
                .measure(entity.getMeasure())
                .build();
    }

    static public MacronutrientView toMacronutrientView(Macronutrient entity) {
        return MacronutrientView.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .activeState(entity.getActiveState())
                .inactiveState(entity.getInactiveState())
                .functions(entity.getFunctions().stream().map(PairView::new).toList())
                .sources(entity.getSources().stream().map(PairView::new).toList())
                .types(entity.getTypes().stream().map(PairView::new).toList())
                .dietaryConsiderations(entity.getDietaryConsiderations().stream().map(PairView::new).toList())
                .build();
    }

    static public VitaminView toVitaminView(Vitamin entity) {
        return VitaminView.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .functions(entity.getFunctions().stream().map(PairView::new).toList())
                .sources(entity.getSources().stream().map(PairView::new).toList())
                .maleHigherBoundIntake(entity.getMaleHigherBoundIntake())
                .maleLowerBoundIntake(entity.getMaleLowerBoundIntake())
                .femaleHigherBoundIntake(entity.getFemaleHigherBoundIntake())
                .femaleLowerBoundIntake(entity.getFemaleLowerBoundIntake())
                .measure(entity.getMeasure())
                .build();
    }
}
