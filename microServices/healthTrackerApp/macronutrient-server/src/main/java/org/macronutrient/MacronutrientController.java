package org.macronutrient;

import lombok.RequiredArgsConstructor;
import org.macronutrient.model.dtos.ErrorResponse;
import org.macronutrient.model.entity.Macronutrient;
import org.macronutrient.model.entity.MacronutrientType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/macronutrient")
@RequiredArgsConstructor
public class MacronutrientController {

    private final MacronutrientServiceImp macronutrientService;

    @GetMapping
    public ResponseEntity<List<Macronutrient>> getAllMacros() {
        return new ResponseEntity<>(macronutrientService.getAllMacros(), HttpStatus.OK);
    }

    @GetMapping("/types")
    public ResponseEntity<List<MacronutrientType>> getAllMacroTypes() {
        return new ResponseEntity<>(macronutrientService.getAllMacroTypes(), HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Macronutrient> getMacroByName(@PathVariable String name)
            throws MacronutrientNotFoundException {
        return new ResponseEntity<>(macronutrientService.getMacroByName(name), HttpStatus.OK);
    }

    @GetMapping("/types/{name}")
    public ResponseEntity<MacronutrientType> getMacronutrientTypeByName(@PathVariable String name)
            throws MacronutrientTypeNotFoundException {
        return new ResponseEntity<>(macronutrientService.getMacronutrientTypeByName(name), HttpStatus.OK);
    }

    @ExceptionHandler(MacronutrientNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchMacroNotFoundError(MacronutrientNotFoundException e) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Macronutrient with name '" + e.getMessage() + "' does not existed in the data.");
        errorResponse.setAvailableElectrolyteNames(macronutrientService.getAllMacrosNames());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MacronutrientTypeNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchMacroNotFoundError(MacronutrientTypeNotFoundException e) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Macronutrient with name '" + e.getMessage() + "' does not existed in the data.");
        errorResponse.setAvailableElectrolyteNames(macronutrientService.getAllMacrosTypesNames());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
