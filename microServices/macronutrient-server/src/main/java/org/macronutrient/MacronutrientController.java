package org.macronutrient;

import lombok.RequiredArgsConstructor;
import org.macronutrient.model.dtos.ErrorResponse;
import org.macronutrient.model.dtos.MacronutrientView;
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
    public ResponseEntity<List<MacronutrientView>> getAllMacrosView() {
        return new ResponseEntity<>(macronutrientService.getAllMacrosView(), HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<MacronutrientView> getMacroViewByName(@PathVariable String name) throws MacronutrientNotFoundException {
        return new ResponseEntity<>(macronutrientService.getMacroViewByName(name), HttpStatus.OK);
    }

    @ExceptionHandler(MacronutrientNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchMacroNotFoundError(MacronutrientNotFoundException e) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Macronutrient with name '" + e.getMessage() + "' does not existed in the data.");
        errorResponse.setAvailableElectrolyteNames(macronutrientService.getAllMacrosNames());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
