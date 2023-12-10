package org.vitamin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vitamin.model.dtos.ErrorResponse;
import org.vitamin.model.entity.Vitamin;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vitamin")
@Tag(name = "Vitamin", description = "The Vitamin API")
@RequiredArgsConstructor
public class VitaminController {

    private final VitaminServiceImp vitaminService;

    @Operation(summary = "Get all vitamins", responses = {
            @ApiResponse(responseCode = "200", description = "List of all vitamins", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Vitamin.class)))
    })
    @GetMapping
    public ResponseEntity<List<Vitamin>> getAllVitamins() {
        return new ResponseEntity<>(vitaminService.getVitamins(), HttpStatus.OK);
    }

    @Operation(summary = "Get a vitamin by name", responses = {
            @ApiResponse(responseCode = "200", description = "Vitamin details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Vitamin.class))),
            @ApiResponse(responseCode = "400", description = "Vitamin not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{name}")
    public ResponseEntity<Vitamin> getVitaminByName(
            @Parameter(description = "Name of the vitamin to be obtained.", example = "Vitamin C") @PathVariable String name) throws VitaminNotFoundException {
        return new ResponseEntity<>(vitaminService.getVitaminByName(name), HttpStatus.OK);
    }

    @ExceptionHandler(VitaminNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundVitamin(VitaminNotFoundException exception) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Vitamin with name " + exception.getMessage() + " does not existed in the data.");
        errorResponse.setAvailableElectrolyteNames(vitaminService.getAllVitaminNames());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}