package org.mineral;

import lombok.RequiredArgsConstructor;
import org.mineral.model.dtos.ErrorResponse;
import org.mineral.model.entity.Mineral;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mineral")
@RequiredArgsConstructor
public class MineralController {

    private final MineralServiceImp mineralServiceImp;

    @GetMapping
    public ResponseEntity<List<Mineral>> getAllMinerals() {
        return new ResponseEntity<>(mineralServiceImp.getAllViewMinerals(), HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Mineral> getMineralByName(@PathVariable String name) throws MineralNotFoundException {
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
