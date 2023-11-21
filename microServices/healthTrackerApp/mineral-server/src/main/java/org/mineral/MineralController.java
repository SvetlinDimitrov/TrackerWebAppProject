package org.mineral;

import lombok.RequiredArgsConstructor;
import org.mineral.model.dtos.MineralView;
import org.mineral.model.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/electrolyte")
@RequiredArgsConstructor
public class MineralController {

    private final MineralServiceImp mineralServiceImp;

    @GetMapping
    public ResponseEntity<List<MineralView>> getAllElectrolytes(){
        return new ResponseEntity<>(mineralServiceImp.getAllViewMinerals() , HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<MineralView> getElectrolyteByName(@PathVariable String name) throws MineralNotFoundException {
        return new ResponseEntity<>(mineralServiceImp.getMineralViewByName(name) , HttpStatus.OK);
    }

    @ExceptionHandler(MineralNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchElectrolyteNotFound(MineralNotFoundException e) {

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setMessage("Mineral with name '" + e.getMessage() + "' does not exist in the data.");
        errorResponse.setAvailableElectrolyteNames(mineralServiceImp.getAllMineralNames());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
