package org.mineral;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.mineral.model.dtos.ErrorResponse;
import org.mineral.model.entity.Mineral;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mineral")
@Tag(name = "Mineral", description = "The Mineral API")
@RequiredArgsConstructor
public class MineralController {

    private final MineralServiceImp mineralServiceImp;

    @Operation(summary = "Get all minerals", responses = {
            @ApiResponse(responseCode = "200", description = "List of all minerals", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Mineral.class)))
    })
    @GetMapping
    public ResponseEntity<List<Mineral>> getAllMinerals() {
        return new ResponseEntity<>(mineralServiceImp.getAllViewMinerals(), HttpStatus.OK);
    }

    @Operation(summary = "Get a mineral by name", responses = {
            @ApiResponse(responseCode = "200", description = "Mineral details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Mineral.class))),
            @ApiResponse(responseCode = "400", description = "Mineral not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{name}")
    public ResponseEntity<Mineral> getMineralByName(
            @Parameter(description = "Name of the mineral to be obtained.", example = "Calcium") @PathVariable String name) throws MineralNotFoundException {
        return new ResponseEntity<>(mineralServiceImp.getMineralViewByName(name), HttpStatus.OK);
    }

    @ExceptionHandler(MineralNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchMineralNotFoundException(MineralNotFoundException e) {

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setMessage("Mineral with name '" + e.getMessage() + "' does not exist in the data.");
        errorResponse.setAvailableMineralNames(mineralServiceImp.getAllMineralNames());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}