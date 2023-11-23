package org.macronutrient;

import java.util.List;

import org.macronutrient.model.entity.Macronutrient;
import org.macronutrient.model.entity.MacronutrientType;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MacronutrientServiceImp {

    private final MacronutrientRepository macronutrientRepository;

    public List<Macronutrient> getAllMacros() {
        return macronutrientRepository
                .getAllMacronutrient()
                .stream()
                .toList();
    }

    public List<MacronutrientType> getAllMacroTypes() {
        return macronutrientRepository
                .getAllMacronutrientTypes()
                .stream()
                .toList();
    }

    public Macronutrient getMacroByName(String name) throws MacronutrientNotFoundException {
        return macronutrientRepository
                .getMacronutrientByName(name)
                .orElseThrow(() -> new MacronutrientNotFoundException(name));
    }

    public MacronutrientType getMacronutrientTypeByName(String name) throws MacronutrientTypeNotFoundException {
        return macronutrientRepository
                .getMacronutrientTypeByName(name)
                .orElseThrow(() -> new MacronutrientTypeNotFoundException(name));
    }

    public List<String> getAllMacrosNames() {
        return macronutrientRepository.getAllMacronutrientNames();
    }

    public List<String> getAllMacrosTypesNames() {
        return macronutrientRepository.getAllMacronutrientTypesNames();
    }

}