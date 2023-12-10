package org.macronutrient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Macronutrient", description = "The Macronutrient API")
@RequiredArgsConstructor
public class MacronutrientController {

    private final MacronutrientServiceImp macronutrientService;

    @Operation(summary = "Get all macronutrients", responses = {
            @ApiResponse(responseCode = "200", description = "List of all macronutrients", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Macronutrient.class)))
    })
    @GetMapping
    public ResponseEntity<List<Macronutrient>> getAllMacros() {
        return new ResponseEntity<>(macronutrientService.getAllMacros(), HttpStatus.OK);
    }

    @Operation(summary = "Get all macronutrient types", responses = {
            @ApiResponse(responseCode = "200", description = "List of all macronutrient types", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MacronutrientType.class)))
    })
    @GetMapping("/types")
    public ResponseEntity<List<MacronutrientType>> getAllMacroTypes() {
        return new ResponseEntity<>(macronutrientService.getAllMacroTypes(), HttpStatus.OK);
    }

    @Operation(summary = "Get a macronutrient by name", responses = {
            @ApiResponse(responseCode = "200", description = "Macronutrient details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Macronutrient.class))),
            @ApiResponse(responseCode = "400", description = "Macronutrient not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{name}")
    public ResponseEntity<Macronutrient> getMacroByName(
            @Parameter(description = "Name of the macronutrient to be obtained.", example = "Protein") @PathVariable String name)
            throws MacronutrientNotFoundException {
        return new ResponseEntity<>(macronutrientService.getMacroByName(name), HttpStatus.OK);
    }

    @Operation(summary = "Get a macronutrient type by name", responses = {
            @ApiResponse(responseCode = "200", description = "Macronutrient type details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MacronutrientType.class))),
            @ApiResponse(responseCode = "400", description = "Macronutrient type not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/types/{name}")
    public ResponseEntity<MacronutrientType> getMacronutrientTypeByName(
            @Parameter(description = "Name of the macronutrient type to be obtained.", example = "Sugar") @PathVariable String name)
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