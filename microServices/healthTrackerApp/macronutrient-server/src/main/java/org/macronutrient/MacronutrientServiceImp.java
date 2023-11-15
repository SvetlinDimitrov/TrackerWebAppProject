package org.macronutrient;

import java.util.List;

import org.macronutrient.model.dtos.MacronutrientView;
import org.macronutrient.model.dtos.PairView;
import org.macronutrient.model.entity.Macronutrient;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MacronutrientServiceImp {

    private final MacronutrientRepository macronutrientRepository;

    public List<MacronutrientView> getAllMacrosView() {
        return macronutrientRepository
                .getAllMacronutrient()
                .stream()
                .map(this::toMacronutrientView)
                .toList();
    }

    public MacronutrientView getMacroViewByName(String name) throws MacronutrientNotFoundException {
        return macronutrientRepository
                .getMacronutrientByName(name)
                .map(this::toMacronutrientView)
                .orElseThrow(() -> new MacronutrientNotFoundException(name));
    }

    public List<String> getAllMacrosNames() {
        return macronutrientRepository.getAllMacronutrientNames();
    }

    private MacronutrientView toMacronutrientView(Macronutrient entity) {
        return MacronutrientView.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .activeState(entity.getActiveState())
                .inactiveState(entity.getInactiveState())
                .functions(entity.getFunctions().stream().map(PairView::new).toList())
                .sources(entity.getSources().stream().map(PairView::new).toList())
                .types(entity.getTypes().stream().map(PairView::new).toList())
                .dietaryConsiderations(
                        entity.getDietaryConsiderations().stream().map(PairView::new).toList())
                .build();
    }

}